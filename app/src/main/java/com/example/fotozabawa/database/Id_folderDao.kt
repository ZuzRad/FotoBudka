package com.example.fotozabawa.database

import androidx.room.*
import com.example.fotozabawa.model.Id_folder
import com.example.fotozabawa.model.Ustawienia

@Dao
interface Id_folderDao {

    @Query("SELECT EXISTS (SELECT 1 FROM folder_number)")
    fun exists(): Boolean

    @Query("UPDATE folder_number SET folder_number = :value1 WHERE _id = 1")
    fun update(value1: Int)

    @Query("SELECT folder_number FROM folder_number")
    fun getiD(): Int


    @Insert
    suspend fun insert(idFolder: Id_folder)
}