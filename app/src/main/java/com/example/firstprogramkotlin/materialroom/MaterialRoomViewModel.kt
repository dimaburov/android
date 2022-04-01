package com.example.firstprogramkotlin.materialroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.Material
import com.example.firstprogramkotlin.database.RoomDao
import kotlinx.coroutines.*

class MaterialRoomViewModel(val dao: RoomDao,
                            application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val materials = dao.getAllRoom()

//    var steps = ArrayList<Apartment>()

    private var materialNow = MutableLiveData<Material?>()

    private val _navigateAfterNewRecipe= MutableLiveData<Boolean>()
    val navigateAfterNewRecipe: LiveData<Boolean>
        get() = _navigateAfterNewRecipe

    init{
        initRoomNow()
    }

    private fun initRoomNow() {
        uiScope.launch {
            materialNow.value = getMaterialNowFromDao()
        }
    }

    private suspend fun getMaterialNowFromDao(): Material? {
        return withContext(Dispatchers.IO){
            var material = dao.MaterialNow()
            return@withContext material
        }
    }

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    //Передавать сюда все парамеры комнаты
    fun onSaveMaterial(name:String, utils:String, count:Int){
        uiScope.launch {
            val newMaterial = Material()
            newMaterial.materialName = name
            newMaterial.materialUtils = utils
            newMaterial.materialCount = count
            insert(newMaterial)
            _navigateAfterNewRecipe.value = true
        }
    }

    private suspend fun insert(newMaterial: Material) {
        withContext(Dispatchers.IO){
            dao.insertMaterial(newMaterial)
        }
    }

    private suspend fun update(oldMaterial: Material){
        withContext(Dispatchers.IO){
            dao.updateMaterial(oldMaterial)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            materialNow.value=null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            dao.clearMaterial()
        }
    }

    fun doneNavigating(){
        _navigateAfterNewRecipe.value = false
    }
}