package com.example.laba222.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laba222.Data.Faculty
import com.example.laba222.repository.FacultyRepository
import java.util.UUID

class GroupViewModel : ViewModel() {
    var faculty:MutableLiveData<Faculty?> = MutableLiveData()
    private var facultyID:UUID?=null
    init{
        FacultyRepository.get().university.observeForever{
            faculty.postValue(it.find { faculty -> faculty.id==facultyID })
        }
    }
    fun setFacultyID(facultyID:UUID){
        this.facultyID=facultyID
        faculty.postValue(FacultyRepository.get().university.value?.find{faculty ->faculty.id==facultyID  })
    }
}