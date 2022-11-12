package com.hackinroms.ellatrivia.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.hackinroms.ellatrivia.components.QuestionScreen

@Composable
fun TriviaHome(viewModel: QuestionViewModel = hiltViewModel()) {
  QuestionScreen(viewModel)
}