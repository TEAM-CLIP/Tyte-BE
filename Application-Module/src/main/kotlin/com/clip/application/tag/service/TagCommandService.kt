package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.CreateTagUsecase
import com.clip.application.tag.port.`in`.UpdateTagUsecase
import org.springframework.stereotype.Service

@Service
class TagCommandService : CreateTagUsecase, UpdateTagUsecase{
    override fun create(command: CreateTagUsecase.Command) {
        TODO("Not yet implemented")
    }

    override fun update(command: UpdateTagUsecase.Command) {
        TODO("Not yet implemented")
    }
}