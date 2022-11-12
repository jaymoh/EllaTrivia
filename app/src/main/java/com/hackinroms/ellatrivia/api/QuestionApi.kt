package com.hackinroms.ellatrivia.api

import com.hackinroms.ellatrivia.models.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
  @GET("animals.json")
  suspend fun getAnimalQuestions(): Question

  @GET("brain-teasers.json")
  suspend fun getBrainTeaserQuestions(): Question

  @GET("celebrities.json")
  suspend fun getCelebrityQuestions(): Question

  @GET("entertainment.json")
  suspend fun getEntertainmentQuestions(): Question

  @GET("for-kids.json")
  suspend fun getKidsQuestions(): Question

  @GET("general.json")
  suspend fun getGeneralQuestions(): Question

  @GET("geography.json")
  suspend fun getGeographyQuestions(): Question

  @GET("history.json")
  suspend fun getHistoryQuestions(): Question

  @GET("hobbies.json")
  suspend fun getHobbiesQuestions(): Question

  @GET("humanities.json")
  suspend fun getHumanitiesQuestions(): Question

  @GET("literature.json")
  suspend fun getLiteratureQuestions(): Question

  @GET("movies.json")
  suspend fun getMoviesQuestions(): Question

  @GET("music.json")
  suspend fun getMusicQuestions(): Question

  @GET("newest.json")
  suspend fun getNewestQuestions(): Question

  @GET("people.json")
  suspend fun getPeopleQuestions(): Question

  @GET("rated.json")
  suspend fun getRatedQuestions(): Question

  @GET("religion-faith.json")
  suspend fun getReligionQuestions(): Question

  @GET("science-technology.json")
  suspend fun getScienceQuestions(): Question

  @GET("sports.json")
  suspend fun getSportsQuestions(): Question

  @GET("television.json")
  suspend fun getTelevisionQuestions(): Question

  @GET("video-games.json")
  suspend fun getVideoGamesQuestions(): Question

  @GET("world.json")
  suspend fun getWorldQuestions(): Question
}