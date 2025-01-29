package com.clip.application.user.port.`in`

interface DeleteFriendUsecase {
    fun deleteFriend(command: Command.DeleteFriend)

    sealed class Command {
        data class DeleteFriend(
            val userId: String,
            val friendId: String,
        ) : Command()
    }
}