package com.clip.domain.user.entity

import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.RequestStatus

class FriendRequest(
    val id : DomainId,
    val receiverId: String,
    val requesterId: String,
    var requestStatus: RequestStatus
) {
    companion object {
        fun create(
            id: DomainId = DomainId.generate(),
            receiverId: String,
            requesterId: String,
        ): FriendRequest =
            FriendRequest(id, receiverId, requesterId, RequestStatus.PENDING)
    }

    fun accept(){
        this.requestStatus = RequestStatus.ACCEPTED
    }

    fun isPending(): Boolean {
        return this.requestStatus == RequestStatus.PENDING
    }

    fun isIncoming(receiverId: String): Boolean {
        return this.receiverId == receiverId
    }
}