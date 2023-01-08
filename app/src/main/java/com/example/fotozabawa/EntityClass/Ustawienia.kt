package com.example.fotozabawa.EntityClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ustawienia")

data class Ustawienia (
    @PrimaryKey(autoGenerate=true) var id:Int?,
    @ColumnInfo (name="czas")val czas: String,
    @ColumnInfo (name="tryb")val tryb: String)