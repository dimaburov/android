package com.example.firstprogramkotlin.dataroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.InitDatabase
import com.example.firstprogramkotlin.databinding.FragmentDataRoomBinding
import com.example.firstprogramkotlin.title.TitleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataRoomFragment : Fragment() {
    private lateinit var viewModel: DataRoomViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDataRoomBinding>(inflater,
            R.layout.fragment_data_room, container, false)

        val application = requireActivity().application

        val dao = InitDatabase.getInstance(application).getRoomDao()
        val viewModelFactory = DataRoomViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DataRoomViewModel::class.java)

        //Если объект в редактировании вывести стартовые данные
        viewModel.inputDataModifiApartamentInRoom(binding)

        val adapter = AdapterMaterial(
            viewModel::deleteMaterial
        )

        binding.itemMaterial.adapter = adapter

        //Добавления материала
        binding.buttonAddMaterial.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_dataRoomFragment_to_materialRoomFragment)
        }

        viewModel.materials.observe(viewLifecycleOwner, Observer { materials ->
            if(materials != null){
                adapter.data = materials
            }
        })

        //Прописать все фрагменты с окна для сохранения
        var checkF: Boolean = true
        binding.buttonSave.setOnClickListener {
            checkF = true
            //Добавляем проверку вводимого
            if (binding.editLengthRoom.text.toString().equals("") ||
                binding.editHeightRoom.text.toString().equals("") ||
                binding.editWidthRoom.text.toString().equals(""))
                checkF = false
            if (binding.radioButton.isChecked == false && binding.radioButton2.isChecked == false)
                checkF = false
            if (binding.radioButtonPar.isChecked == false && binding.radioButtonLam.isChecked == false)
                checkF = false

            //Проверка ламината
            if (binding.radioButtonPar.isChecked == true){
                if (binding.editBoardSize.text.toString().equals("")||
                    binding.editBoardCount.text.toString().equals(""))
                    checkF = false
            }


            if (checkF) {
                val lenght = binding.editLengthRoom.text.toString().toDouble()
                val height = binding.editHeightRoom.text.toString().toDouble()
                val width = binding.editWidthRoom.text.toString().toDouble()
                val checkUtilsFloorLamOrLin = binding.radioButtonPar.isChecked
                val checkUtilsMOrSM = binding.radioButton.isChecked
                //Проверка ламината
                var size = ""
                var boardCount = 0
                if (checkUtilsFloorLamOrLin == true){
                    size = binding.editBoardSize.text.toString()
                    boardCount = binding.editBoardCount.text.toString().toInt()
                }

                viewModel.onSaveRoom(
                    lenght, height, width, checkUtilsFloorLamOrLin,
                    checkUtilsMOrSM, size, boardCount
                )

                //test
                //test связь двух таблиц
                viewModel.addMaterialInMaterialBasic()
                //Добавляем ключ созданной комнаты в материалы
                //Получаем списко материалов с пустым ключом комнаты и вставляем ключ последней созданной комнаты
                //viewModel.setIdApartamentIntoMaterialBasic()
                viewModel.onClearMaterial()

            }
            else {
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, "Не все поля заполнены", duration)
                toast.show()
            }
        }

        //Переход на title
        if(checkF){
            viewModel.navigateAfterNewRecipe.observe(viewLifecycleOwner, Observer { navigate ->
                if (navigate!!){
                    this.findNavController().navigateUp()
                    viewModel.doneNavigating()
                }
            })

        }

        return binding.root
    }


}