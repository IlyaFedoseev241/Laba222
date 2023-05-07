package com.example.laba222

import androidx.lifecycle.ViewModel
import com.example.laba222.Data.Student
import com.example.laba222.repository.FacultyRepository
import java.util.*

class GroupListViewModel: ViewModel() {
    fun deleteStudent(groupID: UUID, student: Student)=
        FacultyRepository.get().deleteStudent(groupID,student)

}