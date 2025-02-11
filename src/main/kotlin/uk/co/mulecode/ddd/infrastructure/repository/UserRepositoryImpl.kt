package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.model.UserStatus
import uk.co.mulecode.ddd.domain.repository.UserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.UserEntity
import java.util.UUID
import java.util.UUID.randomUUID


@Component
class UserRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun create(name: String, email: String): UserModel {
        log.info { "Creating new user" }
        return UserModel(
            jpaUserRepository.save(
                UserEntity(
                    id = randomUUID(),
                    name = name,
                    email = email,
                    status = UserStatus.INACTIVE
                )
            )
        )
    }

    @Transactional(readOnly = true)
    override fun findById(userId: UUID): UserModel {
        log.info { "Repository: Loading user $userId" }
        return jpaUserRepository.findByIdOrNull(userId)
            ?.let { UserModel(it) }
            ?: throw IllegalArgumentException("User not found for id $userId")
    }

    @Transactional
    override fun save(userModel: UserModel): UserModel {
        log.info { "Saving user: ${userModel.data.id}" }
        val entity = jpaUserRepository.save(userModel.data as UserEntity)
        log.info { "User saved: ${entity.id}" }
        userModel.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        userModel.clearDomainEvents()
        return userModel
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<UserModel> {
        log.info { "Repository: Getting all users ${Thread.currentThread().name}" }
        return jpaUserRepository.findAll()
            .map { UserModel(it) }
    }

}
