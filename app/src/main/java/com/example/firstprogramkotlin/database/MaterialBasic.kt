package com.example.firstprogramkotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material_basic_room")
class MaterialBasic {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id_material")
    var materialId: Long = 0L

    var materialName: String = ""

    var materialUtils: String = ""

    var materialCount: Int = 0
}