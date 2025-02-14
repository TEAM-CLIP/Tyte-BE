package com.clip.application.todo.port.out

import com.clip.application.todo.vo.TodoInfo

interface TodoInfoRetrievePort {
    fun getTodoInfo(todoTexts: List<String>, tagNames: List<String>): List<TodoInfo>
}