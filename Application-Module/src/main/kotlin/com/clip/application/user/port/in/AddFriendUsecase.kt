package com.clip.application.user.port.`in`

interface AddFriendUsecase {
    fun addFriendRequest(command: Command.AddFriend)

    fun acceptFriendRequest(command: Command.AcceptFriendRequest)

    sealed class Command {
        data class AddFriend(
            val userId: String,
            val friendId: String,
        ) : Command()

        data class AcceptFriendRequest(
            val receiverId: String,
            val requestId: String,
        ) : Command()
    }
}