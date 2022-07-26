package com.example.submissionkedua.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionkedua.data.local.entity.FavouriteUserEntity

@Database(entities = [FavouriteUserEntity::class], version = 1, exportSchema = false)
abstract class FavouriteUserDatabase : RoomDatabase() {

    abstract fun favouriteUsersDao(): FavouriteUsersDao

    companion object{
        @Volatile
        private var instance: FavouriteUserDatabase? = null
        fun getInstance(context: Context): FavouriteUserDatabase =
            instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteUserDatabase::class.java, "FavouriteUsers.db"
                ).build()
            }
    }

}