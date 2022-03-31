package com.example.firstprogramkotlin.title

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firstprogramkotlin.database.RoomDao

class TitleViewModelFactory(
    private val dao: RoomDao,
    private val application: Application):ViewModelProvider.Factory{
        @Suppress("unchecked_cast")
        override fun<T : ViewModel> create(modelClass: Class<T>): T{
            if (modelClass.isAssignableFrom(TitleViewModel::class.java)){
                return TitleViewModel(dao, application) as T
            }
            throw IllegalAccessException("Unknown ViewModel class")
        }
}