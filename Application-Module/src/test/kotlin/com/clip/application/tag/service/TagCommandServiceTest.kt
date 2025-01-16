package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.usecase.CreateTagUsecase
import com.clip.application.tag.port.`in`.usecase.DeleteTagUsecase
import com.clip.application.tag.port.`in`.usecase.UpdateTagUsecase
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.TagFixture
import com.clip.domain.common.DomainId
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify


class TagCommandServiceTest : BehaviorSpec({
    val mockTagManagementPort = mockk<TagManagementPort>(relaxed = true)

    val tagCommandService = TagCommandService(mockTagManagementPort)

    given("태그를 생성할 때") {
        val command = CreateTagUsecase.Command(
            userId = "userId",
            name = "name",
            color = "color"
        )
        `when`("사용자 아이디, 이름, 색상을 입력하면") {
            tagCommandService.create(command)
            then("태그를 생성한다") {
                verify { mockTagManagementPort.saveTag(any()) }
            }
        }
    }

    given("태그를 수정할 때") {
        val command =  UpdateTagUsecase.Command(
            tagId = "tagId",
            userId = "userId",
            name = "updated_name",
            color = "color"
        )
        val tag = TagFixture.createTag(tagId = DomainId("tagId"), userId = DomainId("userId"), name = "name", color = "color")
        every { mockTagManagementPort.getTagNotNull(tag.id, tag.userId) } returns tag
        `when`("사용자 아이디, 이름, 색상을 입력하면") {
            tagCommandService.update(command)
            then("태그를 수정한다") {
                verify {
                    mockTagManagementPort.updateTag(withArg { savedTag ->
                        savedTag.name shouldBe "updated_name"
                        savedTag.color shouldBe "color"
                    })
                }
            }
        }
    }

    given("태그를 삭제할 때") {
        val command =  DeleteTagUsecase.Command(
            tagId = "tagId",
            userId = "userId"
        )
        val tag = TagFixture.createTag(tagId = DomainId("tagId"), userId = DomainId("userId"))
        every { mockTagManagementPort.getTagNotNull(tag.id, tag.userId) } returns tag
        `when`("사용자 아이디, 태그 아이디를 입력하면") {
            tagCommandService.delete(command)
            then("태그를 삭제한다") {
                verify { mockTagManagementPort.deleteTag(tag) }
            }
        }
    }
})

