package com.example.firstprogramkotlin.materialroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.InitDatabase
import com.example.firstprogramkotlin.databinding.FragmentDataRoomBinding
import com.example.firstprogramkotlin.databinding.FragmentMaterialRoomBinding
import com.example.firstprogramkotlin.dataroom.DataRoomViewModel
import com.example.firstprogramkotlin.dataroom.DataRoomViewModelFactory

class MaterialRoomFragment : Fragment() {
    private lateinit var viewModel: MaterialRoomViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMaterialRoomBinding>(inflater,
            R.layout.fragment_material_room, container, false)
        //Просит содздать первый объект сделаем его пока тестовым
        val application = requireActivity().application

        val dao = InitDatabase.getInstance(application).getRoomDao()
        val viewModelFactory = MaterialRoomViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MaterialRoomViewModel::class.java)

        var checkF: Boolean = true
        binding.saveMaterial.setOnClickListener {
            //Проверка вводимого
            checkF = true
            if (binding.nameMaterial.text.toString().equals("") ||
                binding.unitsMeasurement.text.toString().equals("") ||
                binding.materialCount.text.toString().equals(""))
                    checkF = false

            if (checkF){
                val nameMaterial = binding.nameMaterial.text.toString()
                val utilsMaterial = binding.unitsMeasurement.text.toString()
                val countMaterial = binding.materialCount.text.toString().toInt()
                // save item material
                viewModel.onSaveMaterial(
                    nameMaterial, utilsMaterial, countMaterial
                )

                viewModel.navigateAfterNewRecipe.observe(viewLifecycleOwner, Observer { navigate ->
                    if (navigate!!){
                        this.findNavController().navigateUp()
                        viewModel.doneNavigating()
                    }
                })
            }
            else{
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, "Не все поля заполнены", duration)
                toast.show()
            }

        }

        return binding.root
    }

}