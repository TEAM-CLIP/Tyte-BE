package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.event.TagEventHandler
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.tag.entity.Tag
import com.clip.domain.user.event.UserEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class TagEventService (
    private val tagManagementPort: TagManagementPort
) : TagEventHandler{

    @EventListener
    override fun initializeTags(event: UserEvent.UserCreatedEvent) {
        val tags = Tag.default(event.user.id)
        tagManagementPort.saveAll(tags)
    }
}