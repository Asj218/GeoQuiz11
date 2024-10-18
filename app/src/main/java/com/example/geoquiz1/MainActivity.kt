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
import android.content.Intent
import androidx.lifecycle.ViewModelProvider


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_IS_ANSWER_CHECKED = "is_answer_checked"
private const val KEY_CORRECT_ANSWER_COUNT = "correct_count"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    //private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    //private lateinit var quizViewModel: QuizViewModel

    private val quizViewModel: QuizViewModel by
    lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0
    private var isAnswerChecked = false // Флаг, указывающий на то, был ли дан ответ на текущий вопрос
    private var correctAnswerCount = 0 // Счетчик правильных ответов


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        //prevButton = findViewById(R.id.prev_button)
        //quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        questionTextView = findViewById(R.id.question_text_view)
        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        nextButton.setOnClickListener {
            if (isAnswerChecked) { // Проверяем, был ли дан ответ на текущий вопрос
                goToNextQuestion()
            } else {
                showToast(R.string.choose_answer)
            }
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity,answerIsTrue = true)
            // Переход на новую активити
            //val intent = Intent(this, CheatActivity::class.java)

            // Передача информации о правильном ответе, если нужно
/*            intent.putExtra("ANSWER_IS_TRUE", questionBank[currentIndex].answer)
            */

            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        updateQuestion()


        /*
                prevButton.setOnClickListener{
                    goToPreviousQuestion()
                }
        */
        questionTextView.setOnClickListener {
            if (isAnswerChecked) { // Проверяем, был ли дан ответ на текущий вопрос
                goToNextQuestion()
            } else {
                showToast(R.string.choose_answer)
            }
        }
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            isAnswerChecked = savedInstanceState.getBoolean(KEY_IS_ANSWER_CHECKED, false)
            correctAnswerCount = savedInstanceState.getInt(KEY_CORRECT_ANSWER_COUNT, 0)
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
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putBoolean(KEY_IS_ANSWER_CHECKED, isAnswerChecked)
        outState.putInt(KEY_CORRECT_ANSWER_COUNT, correctAnswerCount)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt("currentIndex")
        correctAnswerCount = savedInstanceState.getInt("correctAnswers")
        updateQuestion()
    }



    private fun updateQuestion(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        isAnswerChecked.takeIf { it }?.let {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            if (currentIndex == questionBank.lastIndex) {
                nextButton.visibility = View.GONE // Скрываем кнопку "Next"
                showResult() // Показываем результат
            }
        } ?: run {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            nextButton.visibility = View.VISIBLE // Показываем кнопку "Next" в остальных случаях
        }

    }
    //указывает, какую кнопку нажал пользователь
    private fun checkAnswer(userAnswer: Boolean){
        if (!isAnswerChecked) { // Проверяем, был ли дан ответ на этот вопрос
            val correctAnswer = questionBank[currentIndex].answer
            if (userAnswer == correctAnswer) {
                showToast(R.string.correct_toast)
                correctAnswerCount++
            } else {
                showToast(R.string.incorrect_toast)
            }
            isAnswerChecked = true // Устанавливаем флаг, чтобы показать, что ответ был дан
            updateQuestion()
        }
    }
    // Выделенная функция для перехода к следующему вопросу
    private fun goToNextQuestion() {
        if (currentIndex < questionBank.lastIndex) {
            currentIndex++
            isAnswerChecked = false
            updateQuestion()
        }
    }

    private fun showResult() {
        // Здесь можно создать диалог или новый экран для отображения результата
        Toast.makeText(this, "Правильные ответы: $correctAnswerCount из ${questionBank.size}", Toast.LENGTH_LONG).show()
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}












