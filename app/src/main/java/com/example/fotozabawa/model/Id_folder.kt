package com.example.fotozabawa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="folder_number")

data class Id_folder (
    @PrimaryKey(autoGenerate=true) var _id:Int?,
    @ColumnInfo (name="folder_number")var folder_number: Int?){
    @Ignore
    constructor(folder_number: Int?) : this (null, folder_number)
}