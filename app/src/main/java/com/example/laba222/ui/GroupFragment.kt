package com.example.laba222.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Student
import com.example.laba222.R
import com.example.laba222.databinding.FragmentGroupBinding
import com.example.laba222.databinding.FragmentGroupListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.UUID
import javax.security.auth.callback.Callback
const val GROUP_TAG="GroupFragment"
class GroupFragment private constructor(): Fragment() {

    private var _binding:FragmentGroupBinding?=null
    private val binding get()=_binding!!

    companion object {
        private lateinit var _facultyID: UUID
        fun newInstance(facultyID: UUID): GroupFragment {
            _facultyID = facultyID
            return GroupFragment()
        }
        val getFacultyID
        get()= _facultyID
    }

        private lateinit var viewModel: GroupViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentGroupBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
            viewModel.setFacultyID(_facultyID)
            viewModel.faculty.observe(viewLifecycleOwner) {
                updateUI(it)
                callbacks?.setTitle(it?.name ?: "")
            }

        }

    private var tabPosition :Int=0
        private fun updateUI(faculty: Faculty?) {
            binding.tabGroup.clearOnTabSelectedListeners()
            binding.tabGroup.removeAllTabs()
            binding.faAddNewStudent.visibility=if((faculty?.groups?.size?:0)>0) {
                binding.faAddNewStudent.setOnClickListener{
                    callbacks?.showStudent(faculty?.groups!![tabPosition].id,null)
                }
                View.VISIBLE
            }else
                                                   View.GONE
            for (i in 0  until  (faculty?.groups?.size?: 0)){
                binding.tabGroup.addTab(binding.tabGroup.newTab().apply {
                    text = i.toString()
                })
            }

            val adapter = GroupPageAdapter(requireActivity(), faculty!!)
            binding.vpGroup.adapter=adapter
            TabLayoutMediator(binding.tabGroup,binding.vpGroup,true,true){
                tab,pos -> tab.text= faculty?.groups?.get(pos)?.name
            }.attach()


            binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabPosition=tab?.position!!
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }
    inner class GroupPageAdapter(fa: FragmentActivity, private val faculty:Faculty):FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (faculty.groups?.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment {
               return GroupListFragment(faculty.groups!![position])

        }
    }
    interface Callbacks{
        fun setTitle(title:String)
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
    override fun onDestroy(){
        super.onDestroy()
        _binding=null
    }

}