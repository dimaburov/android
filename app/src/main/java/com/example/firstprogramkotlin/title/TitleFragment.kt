package com.example.firstprogramkotlin.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.InitDatabase
import com.example.firstprogramkotlin.databinding.FragmentTitleBinding
import com.example.firstprogramkotlin.dataroom.DataRoomViewModel
import com.example.firstprogramkotlin.dataroom.DataRoomViewModelFactory

class TitleFragment : Fragment() {
    private lateinit var viewModel: TitleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_title, container, false)
        val application = requireActivity().application

        val dao = InitDatabase.getInstance(application).getRoomDao()

        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title, container, false)
        val viewModelFactory = TitleViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TitleViewModel::class.java)

        val adapter = TitleAdapter(
            viewModel::deleteApartament,
            viewModel::editApartament,
            viewModel::getMaterialOutMaterialBasic,
            viewModel::modifyApartament
        )

        binding.listRooms.adapter = adapter

        binding.buttonAdd.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_titleFragment_to_dataRoomFragment)
        }

        binding.buttonClear.setOnClickListener {
            viewModel.onClear()
        }

        viewModel.rooms.observe(viewLifecycleOwner, Observer { rooms ->
            if(rooms != null){
                adapter.data = rooms
            }
        })

        viewModel.navigateAfterUpdateRoom.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate!!){
               this.findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }

}