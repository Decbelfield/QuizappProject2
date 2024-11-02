package com.example.quizappproject2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegistrationScreen(navController) }
        composable("rules") { RulesScreen(navController) }
        composable("quiz") { QuizScreen(navController) }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp()
        }
    }
}
fun saveUserScore(context: Context, score: Int) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("quizAppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("userScore", score)
        apply()
    }
}

fun getUserScore(context: Context): Int {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("quizAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("userScore", 0) // Default score is 0 if no value is found
}


@Composable
fun SplashScreen(navController: NavController) {
    Log.d("QuizApp", "SplashScreen: Launched")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.quizzes_anger),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quiz Game", style = MaterialTheme.typography.headlineMedium)
        }
    }

    LaunchedEffect(Unit) {
        try {
            delay(3000)  // Delay for splash screen
            Log.d("QuizApp", "Navigating to login")
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        } catch (e: Exception) {
            Log.e("QuizApp", "Error during splash screen navigation: ${e.message}")
        }
    }
}


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (validateLogin(email, password)) {
                navController.navigate("rules")
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Register")
        }
        val storedScore = getUserScore(context)
        Text("Last score: $storedScore", style = MaterialTheme.typography.bodyMedium)

    }
}

fun validateLogin(email: String, password: String): Boolean {
    return email.isNotEmpty() && password.isNotEmpty()
}

@Composable
fun RegistrationScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("Date of Birth") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (validateRegistration(firstName, lastName, email, password)) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            } else {
                Toast.makeText(context, "Invalid registration details", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }
    }
}

fun validateRegistration(firstName: String, familyName: String, email: String, password: String): Boolean {
    return firstName.length in 3..30 && familyName.isNotEmpty() && isEmailValid(email) && password.isNotEmpty()
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun RulesScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Rules", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("1. You will be asked a series of questions.")
        Text("2. Select the correct answer(s) and confirm.")
        Text("3. Your score will be calculated based on correct answers.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("quiz") }) {
            Text("Start Quiz")
        }
    }
}
@Composable
fun QuizScreen(navController: NavController) {
    val context = LocalContext.current
    val questions = remember {
        listOf(
            Question(
                "What is the capital of France?",
                listOf("Paris", "London", "Berlin", "Madrid"),
                correctAnswer = "Paris",
                type = QuestionType.MULTIPLE_CHOICE
            ),
            Question(
                "Which of the following are programming languages?",
                listOf("Python", "HTML", "Java", "CSS"),
                correctAnswers = listOf("Python", "Java"),
                type = QuestionType.MULTIPLE_ANSWERS
            ),
            Question(
                "What is 2 + 2?",
                listOf("3", "4", "5", "6"),
                correctAnswer = "4",
                type = QuestionType.MULTIPLE_CHOICE
            ),
            Question(
                "Which are fruits?",
                listOf("Apple", "Carrot", "Banana", "Potato"),
                correctAnswers = listOf("Apple", "Banana"),
                type = QuestionType.MULTIPLE_ANSWERS
            ),
            Question(
                "What is the largest ocean?",
                listOf("Atlantic", "Indian", "Arctic", "Pacific"),
                correctAnswer = "Pacific",
                type = QuestionType.MULTIPLE_CHOICE
            )
        )
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    val selectedAnswers = remember { mutableStateListOf<String>() }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    if (currentQuestionIndex < questions.size) {
        val question = questions[currentQuestionIndex]

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(question.text, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            if (question.type == QuestionType.MULTIPLE_CHOICE) {
                question.options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAnswers.size == 1 && selectedAnswers[0] == option,
                            onClick = {
                                selectedAnswers.clear()
                                selectedAnswers.add(option)
                            }
                        )
                        Text(option)
                    }
                }
            } else if (question.type == QuestionType.MULTIPLE_ANSWERS) {
                question.options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedAnswers.contains(option),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedAnswers.add(option)
                                } else {
                                    selectedAnswers.remove(option)
                                }
                            }
                        )
                        Text(option)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                showConfirmationDialog = true
            }) {
                Text("Confirm Answer")
            }
        }

        if (showConfirmationDialog) {
            ConfirmationDialog(
                onConfirm = {
                    if (question.type == QuestionType.MULTIPLE_CHOICE) {
                        if (selectedAnswers.firstOrNull() == question.correctAnswer) {
                            score++
                        }
                    } else {
                        if (selectedAnswers.containsAll(question.correctAnswers) && selectedAnswers.size == question.correctAnswers.size) {
                            score++
                        }
                    }
                    selectedAnswers.clear()
                    currentQuestionIndex++
                    showConfirmationDialog = false

                    if (currentQuestionIndex == questions.size)
                    {
                        saveUserScore(context, score)
                        Toast.makeText(context, "Quiz finished! Your score: $score/${questions.size}", Toast.LENGTH_SHORT).show()
                        navController.navigate("rules")
                    }
                },
                onDismiss = { showConfirmationDialog = false }
            )
        }
    }
}

@Composable
fun ConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Answer") },
        text = { Text("Are you sure about your answer?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}
