package com.example.laba222.Data

import java.util.*

data class Faculty(val id:UUID= UUID.randomUUID(), var name:String="",var area:String=""){
    constructor():this(UUID.randomUUID())
    var groups: List<Group> = emptyList()
}
