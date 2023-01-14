package com.example.fotozabawa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fotozabawa.model.Id_folder
import com.example.fotozabawa.model.Ustawienia

@Database(entities = [Ustawienia::class, Id_folder::class], version = 5, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun ustawieniaDao(): UstawieniaDao
    abstract fun id_folderDao(): Id_folderDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Ustawienia"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }
}