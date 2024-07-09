package com.example.quizappfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizappfinal.ui.theme.QuizAppFinalTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppFinalTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(onNextScreen = {
                navController.navigate("questionOne")
            })
        }

        composable(route = "questionOne") {
            QuestionOneScreen(onNextScreen = {userPoints: Int ->
                navController.navigate("questionTwo/${userPoints}")
            })
        }

        composable(route = "questionTwo/{userPoints}") { backStackEntry ->
            val userPoints = backStackEntry.arguments?.getString("userPoints")?.toIntOrNull() ?: 0
            QuestionTwoScreen(userPoints = userPoints, onNextScreen = { updatedPoints: Int ->
                navController.navigate("finalScreen/$updatedPoints")
            })
        }

        composable(route = "finalScreen") {
            FinalScreen()
        }

    }
}

@Composable
fun HomeScreen(onNextScreen: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.quiz_header),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally),
            color = Color.Blue,
            fontSize = 50.sp
        )
        Button(onClick = onNextScreen ) {
            Text("Start Quiz")
        }
    }
}

@Composable
fun QuestionOneScreen(onNextScreen: (Int) -> Unit, modifier: Modifier = Modifier) {
    var userAnswer by remember { mutableStateOf("") }
    var isAnswerCorrect: Boolean? by remember { mutableStateOf(null) }
    var userPoints by  remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question 1",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally),
            color = Color.Blue,
            fontSize = 50.sp
        )
        Text(
            text = stringResource(R.string.question_1),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(30.dp),
            color = Color.Gray,
            fontSize = 30.sp,
            lineHeight = 40.sp
        )
        AnswerField(
            value = userAnswer,
            onValueChange = {userAnswer = it},
            modifier = Modifier
                .padding(bottom = 32.dp)

        )
        SubmitButton(
            value = userAnswer,
            changeIsAnswerCorrectState = {validatedAnswer: Boolean -> isAnswerCorrect = validatedAnswer},
            changeUserPoints = {isAnswerCorrect -> if (isAnswerCorrect) {
                userPoints += 1
                println("User points updated: $userPoints")
            } },
            onClick = {onNextScreen(userPoints)}
        )
    }
}

@Composable
fun AnswerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            println("Current Answer: $it")},
        singleLine = true,
        label = {Text("Add answer")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = modifier)
}

@Composable
fun SubmitButton(
    value: String,
    changeIsAnswerCorrectState: (Boolean) -> Unit,
    changeUserPoints: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = {
        if (value.lowercase() == "yes") {
            changeIsAnswerCorrectState(true)
            changeUserPoints(true)
        } else {
            changeIsAnswerCorrectState(false)
        }
        onClick()
    },
        modifier = Modifier
    ) {
        Text(text = "Next Question")
    }
}



@Composable
fun QuestionTwoScreen(userPoints: Int, onNextScreen: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your points are $userPoints",
            color = Color.Blue,
            fontSize = 40.sp,
            lineHeight = 60.sp,
            modifier = Modifier

        )
    }
}

@Composable
fun FinalScreen(modifier: Modifier = Modifier) {

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppFinalTheme {
        App()
    }
}