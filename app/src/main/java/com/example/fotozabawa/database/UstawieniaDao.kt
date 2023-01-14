package com.example.fotozabawa.database

import androidx.room.*
import com.example.fotozabawa.model.Ustawienia

@Dao
interface UstawieniaDao {
    @Query("SELECT czas FROM ustawienia")
    suspend fun getCzas(): Int

    @Query("SELECT czas_position FROM ustawienia")
    fun getCzas_position(): Int

    @Query("SELECT banner FROM ustawienia")
    fun get_banner(): String

    @Query("SELECT tryb FROM ustawienia")
    suspend fun getTryb(): Int

    @Query("SELECT tryb_position FROM ustawienia")
    fun getTryb_position(): Int

    @Query("SELECT EXISTS (SELECT 1 FROM ustawienia)")
    fun exists(): Boolean

    @Query("SELECT _id FROM ustawienia")
    fun getiD(): Int


    @Insert
    suspend fun insert(ustawienia: Ustawienia)

    @Update
    suspend fun update(ustawienia: Ustawienia)

    @Delete
    suspend fun delete(ustawienia: Ustawienia)

    @Query("DELETE FROM ustawienia")
    suspend fun deleteAll()
}
