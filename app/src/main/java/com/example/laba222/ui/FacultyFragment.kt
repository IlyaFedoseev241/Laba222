package com.example.laba222.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba222.Data.Faculty
import com.example.laba222.R
import com.example.laba222.databinding.FragmentFacultyBinding
import com.example.laba222.repository.FacultyRepository
import java.util.*

const val FACULTY_TAG="FacultyFragment"
const val FACULTY_TITLE="Зоопарк"

class FacultyFragment : Fragment() {
    private lateinit var viewModel: FacultyViewModel
    private lateinit var facultyName: TextView
    private var _binding: FragmentFacultyBinding? = null
    val binding
        get() = _binding!!

    companion object {
        fun newInstance() = FacultyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFacultyBinding.inflate(inflater, container, false)
        binding.recView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
    private var adapter:FacultyListAdapter=FacultyListAdapter(emptyList())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FacultyViewModel::class.java)
        viewModel.university.observe(viewLifecycleOwner) {
            adapter = FacultyListAdapter(it)
            binding.recView.adapter = adapter
        }
        callbacks?.setTitle(FACULTY_TITLE)
    }

    private inner class FacultyHolder(view: View)
        : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var faculty: Faculty
        fun bind(faculty: Faculty) {
            this.faculty = faculty
            val a=faculty.name
            val b =faculty.area
            val c="$a $b"
            itemView.findViewById<TextView>(R.id.element_tv).text =c
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            callbacks?.showGroupFragment(faculty.id)
        }
    }
    private inner class FacultyListAdapter(private val items:List<Faculty>)
        :RecyclerView.Adapter<FacultyHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FacultyHolder {
            val view=layoutInflater.inflate(R.layout.element_faculty_list,parent,false)
            return FacultyHolder(view)
        }

        override fun getItemCount(): Int= items.size



        override fun onBindViewHolder(holder: FacultyHolder, position: Int) {
            holder.bind(items[position])
        }
    }
    interface Callbacks{
        fun setTitle(_title:String)
        fun showGroupFragment(facultyID: UUID)
    }
    var callbacks: Callbacks?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach() {
        callbacks=null
        super.onDetach()
    }
}