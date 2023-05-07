package com.example.laba222

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.example.laba222.Data.Student
import com.example.laba222.repository.FacultyRepository
import com.example.laba222.ui.*
import org.w3c.dom.Text
import java.util.*



class MainActivity() : AppCompatActivity(),GroupFragment.Callbacks,GroupListFragment.Callbacks,FacultyFragment.Callbacks{

    private var mItemFaculty: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain,FacultyFragment.newInstance(), FACULTY_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
        onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(supportFragmentManager.backStackEntryCount>0){
                    supportFragmentManager.popBackStack()
                }else
                    finish()
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        mItemFaculty = menu?.findItem(R.id.mItemFacultyGroup)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mItemFacultyGroup -> {
                val myFragment=supportFragmentManager.findFragmentByTag(GROUP_TAG)
                if(myFragment==null)
                    showNameInputDialog(0)
                else
                    showNameInputDialog(1)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun showNameInputDialog(index:Int=-1){
        val builder=AlertDialog.Builder(this)
        builder.setCancelable(true)
        val dialogView=LayoutInflater.from(this).inflate(R.layout.input_name,null)
        builder.setView(dialogView)
        val nameInput=dialogView.findViewById(R.id.editName) as EditText
        val areaInput=dialogView.findViewById(R.id.editArea) as EditText
        val tvInfo=dialogView.findViewById(R.id.tvInfo) as TextView
        when(index){
            0 -> {
                tvInfo.text=getString(R.string.inputFaculty)

                areaInput.isEnabled=true
                areaInput.visibility=View.VISIBLE
                builder.setPositiveButton(getString(R.string.commit)){ _, _ ->
                    val s=nameInput.text.toString()
                    val a=areaInput.text.toString()
                    if(s.isNotBlank()){
                        FacultyRepository.get().newFaculty(s,a)
                    }
                }
            }
            1 -> {
                tvInfo.text=getString(R.string.inputGroup)
                areaInput.isEnabled=false
                areaInput.visibility=View.INVISIBLE
                builder.setPositiveButton("Подтверждение"){_, _ ->
                    val s=nameInput.text.toString()
                    if(s.isNotBlank()){
                        FacultyRepository.get().newGroup(GroupFragment.getFacultyID,s)
                    }

                }

            }
        }
        builder.setNegativeButton("отмена",null)
        val alert=builder.create()
        alert.show()
    }

    override fun setTitle(_title: String) {
        title=_title
    }

    override fun showStudent(groudID:UUID,student: Student?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain, StudentFragment.newInstance(groudID ,student),STUDENT_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }

    override fun showGroupFragment(facultyID: UUID) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain,GroupFragment.newInstance(facultyID),GROUP_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onStop() {
        FacultyRepository.get().saveFaculty()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        FacultyRepository.get().loadingFaculty()
    }


}