package com.clip.application.tag

import com.clip.application.tag.port.out.TagManagementPort
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TagApplicationConfig(
    private val tagManagementPort: TagManagementPort
) {

    @Bean
    fun tagMockGenerator(): TagMockManager = TagMockManager(tagManagementPort)
}