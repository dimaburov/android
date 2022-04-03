package com.example.firstprogramkotlin.dataroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firstprogramkotlin.database.RoomDao
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.Material
import com.example.firstprogramkotlin.databinding.FragmentDataRoomBinding
import kotlinx.coroutines.*

class DataRoomViewModel(
    val dao: RoomDao,
    application: Application) : AndroidViewModel(application){

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val materials = dao.getAllMaterial()

    private var roomNow = MutableLiveData<Apartment?>()
    private var modify = MutableLiveData<Apartment?>()
    val modifyApartament:LiveData<Apartment?>
        get() = modify

    private var materialNow = MutableLiveData<Material?>()

    private val _navigateAfterNewRecipe= MutableLiveData<Boolean>()
    val navigateAfterNewRecipe: LiveData<Boolean>
        get() = _navigateAfterNewRecipe

    init{
        initRoomNow()
        initMaterialNow()
    }
//----------  ROOM  -----------
    private fun initRoomNow() {
        uiScope.launch {
            roomNow.value = getRoomNowFromDao()
        }
    }

    private suspend fun getRoomNowFromDao(): Apartment? {
        return withContext(Dispatchers.IO){
            var room = dao.getRoomNow()
            return@withContext room
        }
    }

    fun inputDataModifiApartamentInRoom(binding: FragmentDataRoomBinding){
        uiScope.launch {
            val modifyApartament = getModifyApartament()
            if (modifyApartament != null) {
                binding.editHeightRoom.setText(modifyApartament.height.toString())
                binding.editLengthRoom.setText(modifyApartament.length.toString())
                binding.editWidthRoom.setText(modifyApartament.wight.toString())
                if (modifyApartament.utilsMSm){
                    binding.radioButton.isChecked = true
                }
                else binding.radioButton2.isChecked = true

                if(modifyApartament.floorF){
                    binding.radioButtonPar.isChecked = true
                    binding.radioButtonPar.hasSelection()
                    binding.editBoardSize.setText(modifyApartament.size.toString())
                    binding.editBoardCount.setText(modifyApartament.countBoard.toString())
                }
                else binding.radioButtonLam.isChecked = true
            }
        }
    }
    fun onClearJob(){
        onCleared()
    }

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    //Передавать сюда все парамеры комнаты
    fun onSaveRoom(length:Double,height:Double,wight:Double, checkUtilsFloorLamOrLin:Boolean, checkUtilsMOrSM:Boolean, size:String, boardCount:Int){
        uiScope.launch {
            val newRoom = Apartment()
            newRoom.length = length
            newRoom.height = height
            newRoom.wight = wight
            newRoom.floorF = checkUtilsFloorLamOrLin
            newRoom.size = size
            newRoom.utilsMSm = checkUtilsMOrSM
            newRoom.countBoard = boardCount
            //Проверяем изменяется запись ли сейчас
            val modifyApartament = getModifyApartament()
            //Если редактируемая запись найдена - переписываем значения
            if (modifyApartament != null){
                println("!!!!Запись редактируется!!!!")
                updateModifyApartament(newRoom, modifyApartament.roomId)
            }
            else{
                println("!!!!Запись не редактируется!!!!")
                insert(newRoom)
            }
            //Получение числа добавленных материалов в комнате
            roomNow.value = getRoomNowFromDao()
            setIdApartament(modifyApartament)
            //Добавление значения в поле кол-ва материалов
            getCountMaterialInApartament(modifyApartament)
            _navigateAfterNewRecipe.value = true
        }
    }

    fun getCheckModifyApartament(){
        uiScope.launch {
            modify.value = getModifyApartament()
        }
    }

    //Ищем редактируемую запись комнаты
    private suspend fun getModifyApartament():Apartment?{
        return withContext(Dispatchers.IO){
            val apartament = dao.getModifyApartament(true)
            return@withContext apartament
        }
    }

    //редактирование записи
    private suspend fun updateModifyApartament(newRoom: Apartment, id_apartament: Long){
        withContext(Dispatchers.IO){
            dao.updateModifyApartament(id_apartament, newRoom.length, newRoom.wight, newRoom.height, newRoom.floorF, newRoom.size, newRoom.countBoard, newRoom.utilsMSm, 0, false)
        }
    }

    private suspend fun getCountMaterialInApartament(modifyApartament: Apartment?){
        withContext(Dispatchers.IO){
            //Последняя созданная запись
            var  apartament = dao.getRoomNow()
            //Если запись модифицируется то смотрим по её id
            if (modifyApartament != null){
                 apartament = modifyApartament
            }

            var count = apartament?.let { dao.getCountMaterialInApartament(it.roomId) }
            if (count == null) {
                count = 0
            }
            if (apartament != null) {
                dao.setCountMaterial(apartament.roomId, count)
            }
        }
    }

    private suspend fun insert(newRoom: Apartment) {
        withContext(Dispatchers.IO){
            dao.insert(newRoom)
        }
    }

    private suspend fun update(oldRoom: Apartment){
        withContext(Dispatchers.IO){
            dao.update(oldRoom)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            roomNow.value=null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            dao.clear()
        }
    }
//--------  MATERIAL  -----------


    private fun initMaterialNow() {
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

    fun onClearMaterial(){
        uiScope.launch {
            clearMaterial()
            materialNow.value=null
        }
    }

    private suspend fun clearMaterial(){
        withContext(Dispatchers.IO){
            dao.clearMaterial()
        }
    }


    fun doneNavigating(){
        _navigateAfterNewRecipe.value = false
    }


    fun deleteMaterial(material: Material){
        uiScope.launch {
            removeMaterial(material)
        }
    }

    private suspend fun removeMaterial(material: Material) {
        withContext(Dispatchers.IO){
            dao.deleteItemMaterial(material)
        }
    }

    //TMATERIAL - MATERIAL BASIC
    fun addMaterialInMaterialBasic(){
        uiScope.launch {
            addMaterialBasic()
        }
    }

    private suspend fun addMaterialBasic(){
        withContext(Dispatchers.IO){
            dao.addMaterialIntoMaterialBasic()
        }
    }

    private suspend fun setIdApartament(modifyApartament: Apartment?){
        withContext(Dispatchers.IO){
            var room = roomNow.value
            if (modifyApartament!=null){
                room = modifyApartament
            }
            if (room != null) {
                dao.creadKeyMaterialBasicAndApartament(room.roomId)
            }
        }
    }
}