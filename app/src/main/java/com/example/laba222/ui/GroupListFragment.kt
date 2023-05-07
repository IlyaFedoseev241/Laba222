package com.example.laba222.ui

import android.content.Context
import android.os.Bundle
import android.os.RecoverySystem
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba222.Data.Group
import com.example.laba222.Data.Student
import com.example.laba222.GroupListViewModel
import com.example.laba222.R
import com.example.laba222.StudentViewModel
import com.example.laba222.databinding.FragmentGroupBinding
import com.example.laba222.databinding.FragmentGroupListBinding
import java.util.*

class GroupListFragment(private val group: Group):Fragment(){
    private var _binding: FragmentGroupListBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel: GroupListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentGroupListBinding.inflate(inflater,container,false)
        binding.rvGroupList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this).get(GroupListViewModel::class.java)
        binding.rvGroupList.adapter=GroupListAdapter(group.students)

    }
    private var lastItemView:View?=null
    private inner class GroupHolder(view:View)
        :RecyclerView.ViewHolder(view),View.OnClickListener{
            lateinit var student: Student
            fun bind(student: Student){
                this.student=student
                val s="${student.firstName} (${student.middleName})"
                itemView.findViewById<TextView>(R.id.tvElement).text=s
                itemView.findViewById<ConstraintLayout>(R.id.clButtons).visibility=View.GONE
            }
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val cl=itemView.findViewById<ConstraintLayout>(R.id.clButtons)
            cl.visibility=View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.clButtons)?.visibility=View.GONE
            lastItemView=if(lastItemView==itemView)null else itemView
            if(cl.visibility==View.VISIBLE)
                itemView.findViewById<ImageButton>(R.id.ibDelete).setOnClickListener {
                    commitDeleteDialog(student)
                }
            itemView.findViewById<ImageButton>(R.id.ibEdit).setOnClickListener {
                callbacks?.showStudent(group.id,student)
            }
        }
        }

    private fun commitDeleteDialog(student: Student) {
        val builder=AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить животное")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)){_, _ ->
            viewModel.deleteStudent(group.id,student)

        }
        builder.setNegativeButton("Отмена",null)
        val alert=builder.create()
        alert.show()

    }

    private inner class GroupListAdapter(private val items:List<Student>):RecyclerView.Adapter<GroupHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
            val view=layoutInflater.inflate(R.layout.layout_student_list_element,parent,false)
            return GroupHolder(view)
        }
        override fun getItemCount():Int=items.size

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(items[position])
        }
    }
    interface Callbacks{
       fun showStudent(groupID:UUID,student: Student?)
    }
    var callbacks:Callbacks?=null

    override fun onAttach(context: Context){
        super.onAttach(context)
        callbacks=context as Callbacks

    }

    override fun onDetach() {
        callbacks=null
        super.onDetach()
    }

}