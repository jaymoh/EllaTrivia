package com.hackinroms.ellatrivia.components

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackinroms.ellatrivia.data.ManagePreferences
import com.hackinroms.ellatrivia.models.QuestionItem
import com.hackinroms.ellatrivia.screens.QuestionViewModel
import com.hackinroms.ellatrivia.utils.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun QuestionScreen(viewModel: QuestionViewModel) {
  val questions = viewModel.data.value.data?.toMutableList() ?: mutableListOf()

  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val savedIndex = ManagePreferences(context)
    .getIntData("questionIndex")
    .collectAsState(0)

  val questionIndex = remember {
    mutableStateOf(0)
  }

  questionIndex.value = savedIndex.value

  if (viewModel.data.value.loading == true) {
    CircularProgressIndicator()
  } else {

    val question = try {
      questions[questionIndex.value]
    } catch (e: Exception) {
      null
    }

    if (question != null) {
      QuestionDisplay(
        question = question,
        questionIndex = questionIndex,
        viewModel = viewModel
      ) {
        // is not less than 0 and is not greater than the size of the list
        if (it >= 0 && it < questions.size) {
          questionIndex.value = it
        } else {
          questionIndex.value = 0
        }

        scope.launch {
          ManagePreferences(context)
            .storeIntData(key = "questionIndex", value = questionIndex.value)
        }
      }
    }
  }
}

@Composable
fun QuestionDisplay(
  question: QuestionItem,
  questionIndex: MutableState<Int>,
  viewModel: QuestionViewModel,
  onBtnClicked: (Int) -> Unit = {},
) {
  val choicesState = remember(question) {
    question.choices.toMutableList()
  }

  val answerState = remember(question) {
    mutableStateOf<Int?>(null)
  }

  val correctAnswerState = remember(question) {
    mutableStateOf<Boolean?>(null)
  }

  val updateAnswer: (Int) -> Unit = remember(question) {
    {
      answerState.value = it
      correctAnswerState.value = choicesState[it] == question.answer
    }
  }

  val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    color = AppColors.mDarkPurple
  ) {
    Column(
      modifier = Modifier
        .padding(12.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start
    ) {

      if (questionIndex.value >= 3) {
        ShowProgress(
          score = questionIndex.value,
          total = viewModel.data.value.data?.size ?: 0
        )
      }
      QuestionTracker(
        counter = questionIndex.value + 1,
        outOf = viewModel.data.value.data?.size ?: 0
      )

      DrawDottedLine(pathEffect)

      Column {
        // Question
        Text(
          text = question.question,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 22.sp,
          color = AppColors.mOffWhite,
          modifier = Modifier
            .padding(6.dp)
            .align(alignment = Alignment.Start)
            .fillMaxHeight(0.3f)
        )
        // choices
        choicesState.forEachIndexed { index, answerText ->
          Row(
            modifier = Modifier
              .padding(6.dp)
              .fillMaxWidth()
              //.height(45.dp)
              .border(
                width = 4.dp, brush = Brush.linearGradient(
                  colors = listOf(AppColors.mDarkPurple, AppColors.mOffDarkPurple)
                ),
                shape = RoundedCornerShape(15.dp)
              )
              .clip(
                RoundedCornerShape(
                  topStartPercent = 50,
                  topEndPercent = 50,
                  bottomStartPercent = 50,
                  bottomEndPercent = 50
                )
              )
              .background(Color.Transparent)
              .clickable { updateAnswer(index) },
            verticalAlignment = Alignment.CenterVertically,
          ) {
            RadioButton(
              selected = (answerState.value == index),
              onClick = {
                updateAnswer(index)
              },
              modifier = Modifier.padding(start = 16.dp),
              colors = RadioButtonDefaults
                .colors(
                  selectedColor =
                  if (correctAnswerState.value == true && answerState.value == index) {
                    Color.Green.copy(alpha = 0.2f)
                  } else {
                    Color.Red.copy(alpha = 0.2f)
                  }
                ),
            ) // RadioButton
            // Answer Text
            val annotatedString = buildAnnotatedString {
              withStyle(
                style = SpanStyle(
                  fontWeight = FontWeight.Light,
                  color = if (correctAnswerState.value == true && index == answerState.value) {
                    Color.Green
                  } else if (correctAnswerState.value == false && index == answerState.value) {
                    Color.Red
                  } else {
                    AppColors.mOffWhite
                  },
                  fontSize = 17.sp
                )
              ) {
                append(answerText)
              }
            }
            Text(
              text = annotatedString,
              modifier = Modifier
                .padding(6.dp)
            )
          }
        }
        // Next and Previous Buttons
        Row(
          modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Button(
            onClick = {
              onBtnClicked(questionIndex.value - 1)
            },
            modifier = Modifier
              .padding(3.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults
              .buttonColors(
                backgroundColor = AppColors.mLightBlue,
                contentColor = AppColors.mOffWhite
              )
          ) {
            Text(
              text = "< Prev",
              fontSize = 17.sp,
              lineHeight = 22.sp,
              color = AppColors.mOffWhite,
              modifier = Modifier
                .padding(4.dp)
            )
          }


          Button(
            onClick = {
              onBtnClicked(questionIndex.value + 1)
            },
            modifier = Modifier
              .padding(3.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults
              .buttonColors(
                backgroundColor = AppColors.mLightBlue,
                contentColor = AppColors.mOffWhite
              )
          ) {
            Text(
              text = "Next >",
              fontSize = 17.sp,
              lineHeight = 22.sp,
              color = AppColors.mOffWhite,
              modifier = Modifier
                .padding(4.dp)
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
  Text(
    text = buildAnnotatedString {
      withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
        withStyle(
          style = SpanStyle(
            color = AppColors.mLightGray,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
          )
        ) {
          append("Question $counter/")
          withStyle(
            style = SpanStyle(
              color = AppColors.mLightGray,
              fontWeight = FontWeight.Medium,
              fontSize = 14.sp
            )
          ) {
            append("$outOf")
          }
        }
      }
    },
    modifier = Modifier.padding(20.dp)
  )
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
  Canvas(
    modifier = Modifier
      .fillMaxWidth()
      .height(1.dp),
  ) {
    drawLine(
      color = AppColors.mLightGray,
      start = Offset(0f, 0f),
      end = Offset(size.width, 0f),
      pathEffect = pathEffect
    )
  }
}

@Preview
@Composable
fun ShowProgress(score: Int = 1, total: Int = 100) {

  val gradient = Brush.linearGradient(
    listOf(
      Color(0xFFF95075),
      Color(0xFFBE6BE5)
    ),
  )

  val progressFactor = remember(score) {
    mutableStateOf(score.toFloat() / total.toFloat())
  }

  val scoreString = remember(score) {
    val perc = score.toFloat() / total.toFloat() * 100
    mutableStateOf("%.2f".format(perc))
  }

  Row(
    modifier = Modifier
      .padding(3.dp)
      .fillMaxWidth()
      .height(45.dp)
      .border(
        width = 4.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            AppColors.mLightPurple,
            AppColors.mLightPurple
          )
        ),
        shape = RoundedCornerShape(20.dp)
      )
      .clip(RoundedCornerShape(50))
      .background(Color.Transparent),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Button(
      onClick = { },
      contentPadding = PaddingValues(1.dp),
      modifier = Modifier
        .fillMaxWidth(progressFactor.value)
        .background(brush = gradient),
      enabled = false,
      elevation = null,
      colors = buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
      ),
    ) {
      Text(
        text = scoreString.value,
        modifier = Modifier
          .clip(RoundedCornerShape(21.dp))
          .fillMaxHeight(0.87f)
          .fillMaxWidth()
          .padding(6.dp),
        color = AppColors.mOffWhite,
        textAlign = TextAlign.Start,
      )
    }
  }
}


fun saveCurrentState(index: Int, scope: CoroutineScope, context: Context) {
  scope.launch {
    ManagePreferences(context)
      .storeIntData(key = "questionIndex", value = index)
  }
}
