package com.example.fotozabawa

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="ustawienia")

data class Ustawienia (
    @PrimaryKey(autoGenerate=true) var _id:Int?,
    @ColumnInfo (name="czas")var czas: String?,
    @ColumnInfo (name="czas_position")var czas_position: Int?,
    @ColumnInfo (name="tryb")var tryb: String?,
    @ColumnInfo (name="tryb_position")var tryb_position: Int?){
    @Ignore
    constructor(czas: String,czas_position: Int, tryb: String,tryb_position: Int?) : this (null, czas,czas_position, tryb,tryb_position)
}