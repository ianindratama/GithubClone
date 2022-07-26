package com.example.submissionkedua.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouriteusers")
class FavouriteUserEntity (

    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @field:ColumnInfo(name = "name")
    val name: String?,

    @field:ColumnInfo(name = "repos")
    val repos: Int,

    @field:ColumnInfo(name = "following")
    val following: Int,

    @field:ColumnInfo(name = "followers")
    val followers: Int,

    @field:ColumnInfo(name = "company")
    val company: String?,

    @field:ColumnInfo(name = "location")
    val location: String?,

    @field:ColumnInfo(name = "followingUrl")
    val followingUrl: String,

    @field:ColumnInfo(name = "followersUrl")
    val followersUrl: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: String? = null

)