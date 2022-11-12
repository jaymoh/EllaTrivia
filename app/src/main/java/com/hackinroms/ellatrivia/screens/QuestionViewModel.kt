package com.hackinroms.ellatrivia.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackinroms.ellatrivia.data.DataOrException
import com.hackinroms.ellatrivia.data.ManagePreferences
import com.hackinroms.ellatrivia.models.QuestionItem
import com.hackinroms.ellatrivia.repositories.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository) :
  ViewModel() {
  val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> =
    mutableStateOf(DataOrException(null, true, Exception("")))

  init {
    getAllQuestions()
  }

  private fun getAllQuestions() {
    viewModelScope.launch {
      data.value.loading = true
      data.value = repository.getQuestions()
      if (data.value.data.toString().isNotEmpty()) data.value.loading = false
    }
  }

}