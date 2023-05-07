package com.example.laba222

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.laba222.repository.FacultyRepository

class Laba222aplication :Application(){
    override fun onCreate() {
        super.onCreate()
        FacultyRepository.newInstance()
    }
    init {
        instance=this
    }
    companion object{
        private var instance:Laba222aplication? = null
        fun applicationContext():Context{
            return instance!!.applicationContext
        }
    }
}