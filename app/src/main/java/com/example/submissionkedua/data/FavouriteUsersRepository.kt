package com.example.submissionkedua.data

import androidx.lifecycle.LiveData
import com.example.submissionkedua.data.local.entity.FavouriteUserEntity
import com.example.submissionkedua.data.local.room.FavouriteUsersDao

class FavouriteUsersRepository private constructor(
    private val favouriteUsersDao: FavouriteUsersDao
){

    fun getFavouriteUsers(): LiveData<List<FavouriteUserEntity>>{
        return favouriteUsersDao.getFavouriteUsers()
    }

    suspend fun isFavouriteUser(username: String): Boolean{
        return favouriteUsersDao.isFavouriteUser(username)
    }

    suspend fun setFavouriteUsers(favouriteUser: FavouriteUserEntity){
        favouriteUsersDao.insertFavouriteUser(favouriteUser)
    }

    suspend fun deleteFavouriteUser(username: String){
        favouriteUsersDao.deleteFavouriteUser(username)
    }

    companion object{
        @Volatile
        private var instance: FavouriteUsersRepository? = null
        fun getInstance(
            favouriteUsersDao: FavouriteUsersDao
        ): FavouriteUsersRepository = instance ?: synchronized(this){
            instance ?: FavouriteUsersRepository(favouriteUsersDao)
        }.also { instance = it }
    }

}