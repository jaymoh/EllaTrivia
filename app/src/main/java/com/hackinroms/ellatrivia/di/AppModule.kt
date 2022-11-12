package com.hackinroms.ellatrivia.di

import com.hackinroms.ellatrivia.api.QuestionApi
import com.hackinroms.ellatrivia.repositories.QuestionRepository
import com.hackinroms.ellatrivia.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Singleton
  @Provides
  fun provideQuestionApi(): QuestionApi {
    return Retrofit.Builder()
      .baseUrl(Constants.QUESTIONS_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(QuestionApi::class.java)
  }

  @Singleton
  @Provides
  fun provideQuestionRepository(api: QuestionApi) = QuestionRepository(api)
}