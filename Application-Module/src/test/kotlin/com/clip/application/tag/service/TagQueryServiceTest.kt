package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.usecase.GetTagUsecase
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TagQueryServiceTest: BehaviorSpec({
    val mockTagManagementPort = mockk<TagManagementPort>()

    val tagQueryService = TagQueryService(mockTagManagementPort)

    given("사용자가 태그를 조회할 때") {
        val userId = "userId"
        val query = GetTagUsecase.GetAllQuery(userId)
        val tags = Tag.default(DomainId(userId))
        every { mockTagManagementPort.getAllTagBy(DomainId(userId)) } returns tags
        `when`("사용자 아이디를 입력하면") {
            val response = tagQueryService.getAll(query)
            then("사용자의 태그를 조회한다") {
                response.shouldNotBeNull {
                    this.tags.size shouldBe tags.size
                    this.tags.forEachIndexed { index, tag ->
                        tag.tagId shouldBe tags[index].id.value
                        tag.name shouldBe tags[index].name
                        tag.color shouldBe tags[index].color
                    }
                }
            }
        }
    }
})