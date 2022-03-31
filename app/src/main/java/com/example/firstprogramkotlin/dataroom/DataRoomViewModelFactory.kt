package com.example.firstprogramkotlin.dataroom

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firstprogramkotlin.database.RoomDao

class DataRoomViewModelFactory(
    private val dao: RoomDao,
    private val application: Application):ViewModelProvider.Factory{
        @Suppress("unchecked_cast")
        override fun<T : ViewModel> create(modelClass: Class<T>): T{
            if (modelClass.isAssignableFrom(DataRoomViewModel::class.java)){
                return DataRoomViewModel(dao, application) as T
            }
            throw IllegalAccessException("Unknown ViewModel class")
        }
}