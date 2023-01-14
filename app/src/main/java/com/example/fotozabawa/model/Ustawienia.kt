package com.example.fotozabawa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="ustawienia")

data class Ustawienia (
    @PrimaryKey(autoGenerate=true) var _id:Int?,
    @ColumnInfo (name="czas")var czas: Int?,
    @ColumnInfo (name="czas_position")var czas_position: Int?,
    @ColumnInfo (name="tryb")var tryb: Int?,
    @ColumnInfo (name="banner")var banner: String?,
    @ColumnInfo (name="tryb_position")var tryb_position: Int?){
    @Ignore
    constructor(czas: Int,czas_position: Int, tryb: Int,banner: String?,tryb_position: Int?) : this (null, czas,czas_position, tryb,banner,tryb_position)
}