package com.example.submissionkedua.di

import android.content.Context
import com.example.submissionkedua.data.FavouriteUsersRepository
import com.example.submissionkedua.data.local.room.FavouriteUserDatabase

object Injection {

    fun provideRepository(context: Context): FavouriteUsersRepository{
        val database = FavouriteUserDatabase.getInstance(context)
        val dao = database.favouriteUsersDao()
        return FavouriteUsersRepository.getInstance(dao)
    }

}