package com.clip.application.user

import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.application.user.port.out.UserAuthManagementPort
import com.clip.application.user.port.out.UserManagementPort
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class UserApplicationConfig(
    private val userManagementPort: UserManagementPort,
    private val userAuthManagementPort: UserAuthManagementPort,
    private val friendManagementPort: FriendManagementPort,
    private val friendRequestManagementPort: FriendRequestManagementPort,
) {
    @Bean
    fun userMockGenerator(): UserMockManager = UserMockManager(userManagementPort, userAuthManagementPort)

    @Bean
    fun friendMockGenerator(): FriendMockManager = FriendMockManager(friendManagementPort, friendRequestManagementPort)
}
