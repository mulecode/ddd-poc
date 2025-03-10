package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.criteria.Predicate
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.UserFilter
import uk.co.mulecode.ddd.domain.model.UserListModel
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.model.UserStatus
import uk.co.mulecode.ddd.domain.repository.UserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository
import java.util.UUID


@Component
class UserRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun findById(userId: UUID): UserModel {
        log.info { "Repository: Loading user $userId" }
        return jpaUserRepository.findByIdOrNull(userId)
            ?.let { UserModel(it) }
            ?: throw IllegalArgumentException("User not found for id $userId")
    }

    @Transactional
    override fun save(userModel: UserModel): UserModel {
        log.info { "Saving user: ${userModel.data.id}" }
        val entity = if (userModel.data is JpaUserEntity) {
            jpaUserRepository.save(userModel.data)
        } else {
            jpaUserRepository.save(
                JpaUserEntity(
                    id = userModel.data.id,
                    name = userModel.data.name,
                    email = userModel.data.email,
                    status = userModel.data.status
                )
            )
        }
        log.info { "User saved: ${entity.id}" }
        userModel.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        userModel.clearDomainEvents()
        return findById(entity.id)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, filter: UserFilter?): UserListModel {
        log.info { "Repository: Getting all users ${Thread.currentThread().name}" }
        return jpaUserRepository.findAll(filter?.toSpecification(), pageable)
            .let {
                UserListModel(
                    userList = it.content.map { user -> UserModel(user) },
                    page = it.number,
                    totalPages = it.totalPages,
                    size = it.size,
                    totalElements = it.totalElements
                )
            }
    }
}


fun UserFilter.toSpecification(): Specification<JpaUserEntity> {
    return UserSpecification.withFilter(this)
}

object UserSpecification {

    fun withFilter(filter: UserFilter): Specification<JpaUserEntity> {
        return Specification { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            filter.id?.let { predicates.add(criteriaBuilder.equal(root.get<UUID>("id"), it)) }
            filter.name?.let {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%${it.lowercase()}%"
                    )
                )
            }
            filter.email?.let {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("email")),
                        it.lowercase()
                    )
                )
            }
            filter.status?.let { predicates.add(criteriaBuilder.equal(root.get<UserStatus>("status"), it)) }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}
