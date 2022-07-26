package com.example.submissionkedua.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submissionkedua.data.local.entity.FavouriteUserEntity

@Dao
interface FavouriteUsersDao {

    @Query("SELECT * FROM favouriteusers ORDER BY dateAdded DESC")
    fun getFavouriteUsers(): LiveData<List<FavouriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteUsers(favouriteUsers: List<FavouriteUserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteUser(favouriteUser: FavouriteUserEntity)

    @Query("DELETE FROM favouriteusers")
    suspend fun deleteAll()

    @Query("DELETE FROM favouriteusers WHERE username = :username")
    suspend fun deleteFavouriteUser(username: String)

    @Query("SELECT EXISTS(SELECT * FROM favouriteusers WHERE username = :username)")
    suspend fun isFavouriteUser(username: String): Boolean

}