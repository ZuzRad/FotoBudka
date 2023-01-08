package com.example.fotozabawa

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="ustawienia")

data class Ustawienia (
    @PrimaryKey(autoGenerate=true) var _id:Int?,
    @ColumnInfo (name="czas")var czas: String,
    @ColumnInfo (name="tryb")var tryb: String) {
    @Ignore
    constructor(czas: String, tryb: String) : this (null, czas, tryb)
}