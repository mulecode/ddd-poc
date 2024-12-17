package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.repository.UserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.UserEntity

@Component
class UserRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun registerUser(userModel: UserModel): UserModel {
        log.info { "Saving user: ${userModel.id} name ${userModel.name}" }
        val savedUser = toModel(jpaUserRepository.save(toEntity(userModel)))
        userModel.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        userModel.clearDomainEvents()
        return savedUser
    }

    @Transactional(readOnly = true)
    override fun getAllUsers(): List<UserModel> {
        log.info { "Repository: Getting all users ${Thread.currentThread().name}" }
        return jpaUserRepository.findAll().map {
            toModel(it)
        }
    }

    companion object {
        fun toModel(userEntity: UserEntity) = UserModel(
            id = userEntity.id,
            name = userEntity.name,
            email = userEntity.email,
            status = userEntity.status
        )

        fun toEntity(userModel: UserModel) = UserEntity(
            id = userModel.id,
            name = userModel.name,
            email = userModel.email,
            status = userModel.status
        )
    }
}
