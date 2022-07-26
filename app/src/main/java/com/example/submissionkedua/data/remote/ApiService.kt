package com.example.submissionkedua.data.remote

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getSearchData(
        @Query("q") id: String,
        @Header("Authorization") token: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getEachSearchDataDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    fun getUserFollowingDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/followers")
    fun getUserFollowersDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<ItemsItem>>

}