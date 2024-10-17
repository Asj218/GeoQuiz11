package com.example.geoquiz1

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    //private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView


    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0

    private var isAnswerChecked = false // Флаг, указывающий на то, был ли дан ответ на текущий вопрос
    private var correctAnswers = 0 // Счетчик правильных ответов


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        //prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener { View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            if (isAnswerChecked) { // Проверяем, был ли дан ответ на текущий вопрос
                goToNextQuestion()
            } else {
                Toast.makeText(this, R.string.choose_answer, Toast.LENGTH_SHORT).show()
            }
        }
/*
        prevButton.setOnClickListener{
            goToPreviousQuestion()
        }
*/
        questionTextView.setOnClickListener {
            if (isAnswerChecked) { // Проверяем, был ли дан ответ на текущий вопрос
                goToNextQuestion()
            } else {
                Toast.makeText(this, R.string.choose_answer, Toast.LENGTH_SHORT).show()
            }
        }

        updateQuestion()

    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

//сохранить состояние текущего вопроса и правильных ответов
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentIndex", currentIndex)
        outState.putInt("correctAnswers", correctAnswers)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt("currentIndex")
        correctAnswers = savedInstanceState.getInt("correctAnswers")
        updateQuestion()
    }




    private fun updateQuestion(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        // Сбрасываем флаг, когда переходим к новому вопросу
        isAnswerChecked = false

        // Разблокируем кнопки, если они были заблокированы
        trueButton.isEnabled = true
        falseButton.isEnabled = true

        if (currentIndex == questionBank.size - 1) {
            nextButton.visibility = View.GONE // Скрываем кнопку "Next"
            showResult() // Показываем результат
        } else {
            nextButton.visibility = View.VISIBLE // Показываем кнопку "Next"
        }

    }
    //указывает, какую кнопку нажал пользователь
    private fun checkAnswer(userAnswer: Boolean){
        if (!isAnswerChecked) { // Проверяем, был ли дан ответ на этот вопрос
            val correctAnswer = questionBank[currentIndex].answer
            val messageResId = if (userAnswer == correctAnswer){
                correctAnswers++ // Увеличиваем счетчик правильных ответов
                R.string.correct_toast
                //correctAnswers++ // Увеличиваем счетчик правильных ответов
            } else {
                R.string.incorrect_toast
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()



            // Блокируем кнопки после ответа
            trueButton.isEnabled = false
            falseButton.isEnabled = false

            isAnswerChecked = true // Устанавливаем флаг, чтобы показать, что ответ был дан

        }
    }
    // Выделенная функция для перехода к следующему вопросу
    private fun goToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }
/*    private fun goToPreviousQuestion() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size // Переход к предыдущему вопросу
        updateQuestion()
    } */


    private fun showResult() {
        // Здесь можно создать диалог или новый экран для отображения результата
        Toast.makeText(this, "Правильные ответы: $correctAnswers из ${questionBank.size}", Toast.LENGTH_LONG).show()
    }
}












