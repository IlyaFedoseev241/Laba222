package com.example.laba222.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laba222.Data.Faculty
import com.example.laba222.repository.FacultyRepository

class FacultyViewModel : ViewModel() {
    var university:MutableLiveData<List<Faculty>> = MutableLiveData()

    init {
        FacultyRepository.get().university.observeForever{
            university.postValue(it)
        }
    }
    fun newFaculty(name:String,area:String){
        FacultyRepository.get().newFaculty(name,area)
    }
}