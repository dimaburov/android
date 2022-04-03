package com.example.firstprogramkotlin.database

import androidx.annotation.LongDef
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {
    //  base - Room
    @Insert
    fun insert(room: Apartment)

    @Update
    fun update(room: Apartment)

    @Query("SELECT * FROM data_room WHERE roomId = :key")
    fun get(key: Long): Apartment?

    @Query("SELECT * FROM data_room ORDER BY roomId DESC")
    fun getAllRoom(): LiveData<List<Apartment>>

    @Query("SELECT * FROM data_room ORDER BY roomId DESC LIMIT 1")
    fun getRoomNow(): Apartment?

    @Query("DELETE FROM data_room")
    fun clear()

    @Delete
    fun deleteItem(apartment: Apartment)

    //Тестовое добавление изменения кол-ва материалов
    @Query("UPDATE data_room SET countMaterial = :count WHERE roomId = :id")
    fun setCountMaterial(id: Long, count:Int)

    //  base - Material
    @Insert
    fun insertMaterial(material: Material)

    @Update
    fun updateMaterial(material: Material)

    @Query("SELECT * FROM material_room WHERE id_material = :key")
    fun getMaterial(key: Long): Material?

    @Query("SELECT * FROM material_room ORDER BY id_material DESC")
    fun getAllMaterial(): LiveData<List<Material>>

    @Query("SELECT * FROM material_room ORDER BY id_material DESC LIMIT 1")
    fun MaterialNow(): Material?

    @Query("DELETE FROM material_room")
    fun clearMaterial()

    @Delete
    fun deleteItemMaterial(material: Material)

    // base - Material Basic
    @Insert
    fun insertMaterialBasic(materialBasic: MaterialBasic)

    @Update
    fun updateMaterialBasic(materialBasic: MaterialBasic)

    @Query("SELECT * FROM material_basic_room WHERE id_material = :key")
    fun getMaterialBasic(key: Long): MaterialBasic?

    @Query("SELECT * FROM material_basic_room ORDER BY id_material DESC")
    fun getAllMaterialBasic(): LiveData<List<MaterialBasic>>

    @Query("SELECT * FROM material_basic_room ORDER BY id_material DESC LIMIT 1")
    fun MaterialBasicNow(): MaterialBasic?

    @Query("DELETE FROM material_basic_room")
    fun clearMaterialBasic()

    @Delete
    fun deleteItemMaterialBasic(materialBasic: MaterialBasic)

    @Query("INSERT INTO material_basic_room " +
            "SELECT * FROM material_room")
    fun addMaterialIntoMaterialBasic()

    //Пробник связи между таблицами

    @Query("SELECT * FROM material_basic_room WHERE id_material = :id_apartament")
    fun getSchoolAndDirectorWithSchoolName(id_apartament: Long): LiveData<List<MaterialBasic>>

    @Query("UPDATE material_basic_room SET roomId = :id_apartament WHERE roomId = 0")
    fun creadKeyMaterialBasicAndApartament(id_apartament: Long)

    //Определяем кол-во материалов добавленных к комнате
    @Query("SELECT COUNT(*) FROM material_basic_room WHERE roomId = :id_apartament")
    fun getCountMaterialInApartament(id_apartament: Long):Int

    //Запрос переноса материалов из общей таблицы в отображаемую
    @Query("INSERT INTO material_room " +
            "SELECT * FROM material_basic_room WHERE roomId = :id_room")
    fun moveMaterialBasicInMaterial(id_room: Long)

    //Удаляем у перенесённых записей связь с таблицей комнат
    @Query("UPDATE material_room SET roomId = 0")
    fun deleteValueRoomIDInMaterial()

    //Удаляем перенесённые материалыиз основной таблицы
    @Query("DELETE FROM material_basic_room WHERE roomId = :id_room")
    fun clearMoveMaterialBasicInMaterial(id_room: Long)

}