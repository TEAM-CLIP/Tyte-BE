package com.clip.application.tag.port.`in`.event

import com.clip.domain.user.event.UserEvent

interface TagEventHandler {
    fun initializeTags(event: UserEvent.UserCreatedEvent)
}