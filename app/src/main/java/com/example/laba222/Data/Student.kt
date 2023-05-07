package com.example.laba222.Data

import android.provider.ContactsContract
import java.util.*


data class Student(
    val id:UUID = UUID.randomUUID(),
    var firstName: String="",
    var lastName: String="",
    var middleName: String="",
    var phone: String="",
    var description:String=""
) {
    override fun toString(): String {
        return "Student(id=$id, firstName='$firstName', lastName='$lastName', middleName='$middleName', phone='$phone', description=$description)"
    }
}
