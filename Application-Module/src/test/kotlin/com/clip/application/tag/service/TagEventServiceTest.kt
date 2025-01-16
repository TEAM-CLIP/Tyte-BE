package com.clip.application.tag.service

import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.event.UserEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import io.mockk.verify

class TagEventServiceTest: BehaviorSpec({
    val tagManagementPort = mockk<TagManagementPort>(relaxed = true)
    val tagEventService = TagEventService(tagManagementPort)

    given("태그 이벤트가 발생했을 때") {
        `when`("유저가 생성되었을 때") {
            val userId = "userId"
            val event = UserEvent.UserCreatedEvent(
                user = User.create(id = DomainId(userId), nickname = "nickname", email = "email")
            )

            tagEventService.initializeTags(event)

            then("기본 태그를 생성한다") {
                verify {
                    tagManagementPort.saveAll(match { tags ->
                        tags.all { it.userId == DomainId(userId) }
                    })
                }
            }
        }
    }
})