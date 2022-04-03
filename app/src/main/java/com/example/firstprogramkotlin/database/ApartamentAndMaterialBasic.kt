package com.example.firstprogramkotlin.database

import androidx.room.Embedded
import androidx.room.Relation

data class ApartamentAndMaterialBasic (
    @Embedded
    val apartment: Apartment,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "roomId"
    )
    val materialBasic: MaterialBasic
)