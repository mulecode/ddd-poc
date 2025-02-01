package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.repository.UserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.UserEntity
import java.util.UUID
import kotlin.math.log

private const val AGGREGATE_ROOT_NAME = "entity"

@Component
class UserRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    private val log = KotlinLogging.logger {}

    @Transactional(readOnly = true)
    override fun loadUser(userId: String): UserModel {
        log.info { "Repository: Loading user $userId" }
        return jpaUserRepository.findByIdOrNull(userId)
            ?.let { toModel(it, detached = false) }
            ?: throw IllegalArgumentException("User not found for id $userId")
    }

    @Transactional
    override fun registerUser(userModel: UserModel): UserModel {
        log.info { "Registering user name ${userModel.name}" }
        return saveUser(userModel)
    }

    @Transactional
    override fun updateUser(userModel: UserModel): UserModel {
        log.info { "Updating user: ${userModel.id} name ${userModel.name}" }
        return saveUser(userModel)
    }

    @Transactional(readOnly = true)
    override fun getAllUsers(): List<UserModel> {
        log.info { "Repository: Getting all users ${Thread.currentThread().name}" }
        return jpaUserRepository.findAll().map {
            toModel(it)
        }
    }

    private fun saveUser(userModel: UserModel): UserModel {
        val newEntity = toEntity(userModel)
        log.info { "User new: ${newEntity.id}" }
        val entity = jpaUserRepository.save(newEntity)
        log.info { "User saved: ${entity.id}" }
        val savedUser = toModel(entity, detached = false)
        userModel.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        userModel.clearDomainEvents()
        return savedUser
    }

    companion object {

        private val log = KotlinLogging.logger {}

        fun toModel(userEntity: UserEntity, detached: Boolean = true): UserModel {
            val model = UserModel(
                id = userEntity.id,
                name = userEntity.name,
                email = userEntity.email,
                status = userEntity.status
            )
            if (!detached) {
                model.setInfraContext(AGGREGATE_ROOT_NAME, userEntity)
            }
            return model
        }

        fun toEntity(userModel: UserModel): UserEntity {
            return if (userModel.getInfraContext().containsKey(AGGREGATE_ROOT_NAME)) {
                log.info { "Updating existing user entity" }
                val entity = userModel.getInfraContext()[AGGREGATE_ROOT_NAME] as UserEntity
                entity.apply {
                    name = userModel.name
                    email = userModel.email
                    status = userModel.status
                }
            } else {
                log.info { "Creating new user entity" }
                UserEntity(
                    id = UUID.randomUUID().toString(),
                    name = userModel.name,
                    email = userModel.email,
                    status = userModel.status
                )
            }
        }
    }
}
