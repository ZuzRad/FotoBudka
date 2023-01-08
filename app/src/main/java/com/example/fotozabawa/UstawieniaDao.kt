package com.example.fotozabawa;

import androidx.room.*;

@Dao
interface UstawieniaDao {
    @Query("SELECT * FROM ustawienia")
    fun getAll(): List<Ustawienia>

    @Insert
    suspend fun insert(ustawienia: Ustawienia)

    @Update
    suspend fun update(ustawienia: Ustawienia)

    @Delete
    suspend fun delete(ustawienia: Ustawienia)

    @Query("DELETE FROM ustawienia")
    suspend fun deleteAll()
}
