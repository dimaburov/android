package com.example.firstprogramkotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_room")
class Apartment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var roomId: Long = 0L

    var length: Double = 0.0

    var wight: Double = 0.0

    var height: Double = 0.0

    @ColumnInfo(name="floor_flag")
    var floorF: Boolean = true

    var size: String = ""

    @ColumnInfo(name="count_board")
    var countBoard: Int = 0

    @ColumnInfo(name="utils_m_sm")
    var utilsMSm: Boolean = true
}
