package com.example.laba222

import androidx.lifecycle.ViewModel
import com.example.laba222.Data.Student
import com.example.laba222.repository.FacultyRepository
import java.util.*

class StudentViewModel: ViewModel() {
    fun newStudent(groupID: UUID,student: Student)=FacultyRepository.get().newStudent(groupID,student)
    fun editStudent(groupID: UUID,student: Student)=FacultyRepository.get().editStudent(groupID,student)


}