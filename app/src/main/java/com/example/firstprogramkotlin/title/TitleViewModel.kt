package com.example.firstprogramkotlin.title

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.Navigation
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.RoomDao
import kotlinx.coroutines.*

class TitleViewModel(
    val dao: RoomDao,
    application: Application) : AndroidViewModel(application) {

        private var viewModelJob = Job()

        private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
        val rooms = dao.getAllRoom()

        private suspend fun getAllRooms(){
            return withContext(Dispatchers.IO){
                var rooms = dao.getAllRoom()
                rooms
            }
        }

        fun onClear(){
            uiScope.launch {
                clear()
            }
        }

        fun deleteApartament(apartment: Apartment){
            uiScope.launch {
                removeApartament(apartment)
            }
        }

        private suspend fun removeApartament(apartment: Apartment) {
            withContext(Dispatchers.IO){
                dao.deleteItem(apartment)
            }
        }

        private val _navigateAfterUpdateRoom = MutableLiveData<Boolean>()
        val navigateAfterUpdateRoom: LiveData<Boolean>
        get() = _navigateAfterUpdateRoom

        fun editApartament(apartment: Apartment) {
            uiScope.launch {
                updateApartament(apartment)
            }
        }

        private suspend fun updateApartament(apartment: Apartment) {
            //Сначала вызываем новое окно добавляем элемент как обычно
            //Перезаписываем его данные в наш и удаляем его
            //Navigation.findNavController().navigate(R.id.action_titleFragment_to_dataRoomFragment)
//            withContext(Dispatchers.IO){
                _navigateAfterUpdateRoom.value = true
//            }
        }

    private suspend fun clear(){
            withContext(Dispatchers.IO){
                dao.clear()
            }
        }

        override fun onCleared() {
            super.onCleared()
            viewModelJob.cancel()
        }

    fun doneNavigating(){
        _navigateAfterUpdateRoom.value = false
    }

    //Передаём по ключу материалы из общей таблицы в отображаемую
    //Удаляем эти материалы из общей таблицы
    fun getMaterialOutMaterialBasic(apartment: Apartment){
        uiScope.launch {
            createDataMaterialOutMaterialBasic(apartment)
        }
    }

    private suspend fun createDataMaterialOutMaterialBasic(apartment: Apartment){
        withContext(Dispatchers.IO){
            //Запрос - по id ереносим материалы из одной таблицы в другую
            dao.moveMaterialBasicInMaterial(apartment.roomId)
            //Удаляем у перенесённых записей связь с таблицей комнат
            dao.deleteValueRoomIDInMaterial()
            //Запрос - по id удалёем материалы из общей таблицы
            dao.clearMoveMaterialBasicInMaterial(apartment.roomId)
        }
    }

    //Помечаем запись как редактируемую
    fun modifyApartament(apartment: Apartment){
        uiScope.launch {
            modifyApartamentTrue(apartment)
        }
    }

    private suspend fun modifyApartamentTrue(apartment: Apartment){
        withContext(Dispatchers.IO) {
            dao.modifyFlagApartament(apartment.roomId, true)
        }
    }


}