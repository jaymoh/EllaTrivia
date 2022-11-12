package com.hackinroms.ellatrivia.repositories

import android.util.Log
import com.hackinroms.ellatrivia.api.QuestionApi
import com.hackinroms.ellatrivia.data.DataOrException
import com.hackinroms.ellatrivia.models.QuestionItem
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

  private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

  suspend fun getQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
    dataOrException.loading = true

    try {
      dataOrException.data = api.getWorldQuestions()
      if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

    } catch (e: Exception) {
      dataOrException.exception = e
      dataOrException.loading = false

      Log.e("QuestionRepository", "getQuestions: ${e.localizedMessage}")
    }

    return dataOrException
  }
}