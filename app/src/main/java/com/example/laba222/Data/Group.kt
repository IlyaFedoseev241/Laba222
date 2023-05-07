package com.example.laba222.Data

import java.util.*

data class Group(val id: UUID= UUID.randomUUID(),var name:String=""){
    constructor():this(UUID.randomUUID())
    var students: List<Student> = emptyList<Student>()
}
