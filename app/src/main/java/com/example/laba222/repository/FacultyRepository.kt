package com.example.laba222.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import com.example.laba222.Data.Student
import com.example.laba222.Data.University
import com.example.laba222.Laba222aplication
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class FacultyRepository private constructor(){
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()

    companion object{
        private var INSTANCE: FacultyRepository?=null
        fun newInstance(){
            if(INSTANCE==null){
                INSTANCE= FacultyRepository()
            }
        }
        fun get():FacultyRepository{
            return INSTANCE?:
            throw IllegalStateException("Репозиторий FacultyRepository не инициализирован")
        }
    }
    fun newFaculty(name:String,area:String){
        val faculty=Faculty(name="$name ($area m)")
        val list : ArrayList<Faculty>
        if(university.value!=null){
            list=(university.value as ArrayList<Faculty>)
        }
        else
            list=ArrayList<Faculty>()
        list.add(faculty)
        university.postValue(list)
    }
    fun newGroup(facultyID: UUID,name:String){
        if(university.value==null)return
        val u=university.value!!
        val faculty=u.find{it.id==facultyID} ?: return
        val group=Group(name=name)
        val list : ArrayList<Group> = if(faculty.groups.isEmpty()) ArrayList()
                                       else (faculty.groups as ArrayList<Group>)
        list.add(group)
        faculty.groups=list
        university.postValue(u)
    }
    fun newStudent(groupID: UUID,student:Student){
        Log.d("TAG","$groupID $student")

        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}
        val list : ArrayList<Student> = if(group!!.students.isEmpty())
            ArrayList()
        else
            group.students as ArrayList<Student>
        list.add(student)
        group.students=list
        Log.d("TAG","$list$")
        Log.d("TAG","${group.students}$")
        university.postValue(u)
    }
    fun deleteStudent(groupID: UUID,student:Student){
        Log.d("TAG","$groupID $student")

        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}
        val list : ArrayList<Student> = if(group!!.students.isEmpty())
           return
        else
            group.students as ArrayList<Student>
        list.remove(student)
        group.students=list
        Log.d("TAG","$list$")
        Log.d("TAG","${group.students}$")
        university.postValue(u)
    }
    fun editStudent(groupID: UUID,student:Student){
        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}?:return
        val _student=group.students.find{it.id==student.id}
        if(_student==null){
            newStudent(groupID,student)
            return
        }
        val list=group.students as ArrayList<Student>
        val i=list.indexOf(_student)
        list.remove(student)
        list.add(i,student)
        group.students=list
        university.postValue(u)
    }
    fun saveFaculty(){
        if(university.value==null)return
        val u=University(university.value!!)
        val s= Gson().toJson(u)
        val context=Laba222aplication.applicationContext()
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().apply {
            putString("university",s)
            apply()
        }
    }
    fun loadingFaculty(){
        val context=Laba222aplication.applicationContext()
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        val s=sharedPreferences.getString("university","")
        if(s.isNullOrBlank())return
        val u=Gson().fromJson(s,University::class.java)
        university.postValue(u.items)
    }

}