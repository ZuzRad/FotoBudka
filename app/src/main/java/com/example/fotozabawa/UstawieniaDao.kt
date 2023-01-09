package com.example.fotozabawa

import androidx.room.*

@Dao
interface UstawieniaDao {
    @Query("SELECT czas FROM ustawienia")
    suspend fun getCzas(): Int

    @Query("SELECT czas_position FROM ustawienia")
    fun getCzas_position(): Int

    @Query("SELECT tryb FROM ustawienia")
    suspend fun getTryb(): Int

    @Query("SELECT tryb_position FROM ustawienia")
    fun getTryb_position(): Int

    @Insert
    suspend fun insert(ustawienia: Ustawienia)

    @Update
    suspend fun update(ustawienia: Ustawienia)

    @Delete
    suspend fun delete(ustawienia: Ustawienia)

    @Query("DELETE FROM ustawienia")
    suspend fun deleteAll()
}
