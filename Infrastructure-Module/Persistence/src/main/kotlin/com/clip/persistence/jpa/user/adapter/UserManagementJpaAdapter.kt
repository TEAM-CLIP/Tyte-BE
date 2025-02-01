package com.clip.persistence.jpa.user.adapter

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.out.UserManagementPort
import com.clip.common.event.EventPublisher
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.persistence.jpa.user.UserMapper
import com.clip.persistence.jpa.user.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserManagementJpaAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val eventPublisher: EventPublisher,
) : UserManagementPort {
    override fun saveUser(user: User): User {
        val userEntity = UserMapper.toUserEntity(user)
        userJpaRepository.save(userEntity)
        eventPublisher.publishAll(user.pullEvents())
        return user
    }

    override fun getUser(userId: DomainId): User? = userJpaRepository.findActiveUserById(userId.value)?.let { UserMapper.toUser(it) }

    override fun getUserNotNullByEmail(email: String): User {
        return userJpaRepository.findActiveUserByEmail(email)?.let { UserMapper.toUser(it) }
            ?: throw UserException.UserNotFoundException()
    }

    override fun getUserNotNull(userId: DomainId): User =
        userJpaRepository.findActiveUserById(userId.value)?.let { UserMapper.toUser(it) }
            ?: throw UserException.UserNotFoundException()

    override fun delete(user: User) {
        userJpaRepository.deleteUserById(user.id.value)
    }

    override fun getUsersByIds(userIds: List<DomainId>): List<User> {
        return userJpaRepository.findAllActiveUserByIds(userIds.map { it.value }).map { UserMapper.toUser(it) }
    }

    override fun getSearchedUsersByNickname(nickname: String, userId: DomainId): List<User> {
        return userJpaRepository.findAllActiveUserByNicknameContaining(nickname).map { UserMapper.toUser(it) }.filter { it.id != userId}
    }

}
