package com.clip.common.event

interface EventPublisher {
    fun <T : Any> publishAll(events: List<T>)
}
