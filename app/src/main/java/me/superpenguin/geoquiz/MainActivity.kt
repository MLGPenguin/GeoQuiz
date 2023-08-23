package me.superpenguin.geoquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import me.superpenguin.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // Page 240

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Wouldn't this reset if you cheated twice but didn't open the second time?
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { handleAnswer(true) }
        binding.falseButton.setOnClickListener { handleAnswer(false) }

        updateQuestion(binding)

        binding.nextButton.setOnClickListener { updateQuestion(binding, 1) }
        binding.prevButton.setOnClickListener { updateQuestion(binding, -1) }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        Log.d(TAG, "onCreate")
    }

    private fun getToast(@StringRes resId: Int): Toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT)

    private fun handleAnswer(answer: Boolean) {
        val msg = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            answer == quizViewModel.currentQuestionAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        getToast(msg).show()
    }

    /** Updates the question on the screen.
     *
     * @param inc The amount to increment the current index by (Supports negatives)
     */
    private fun updateQuestion(binding: ActivityMainBinding, inc: Int = 0) {
        quizViewModel.incrementQuestion(inc)
        if (inc != 0) quizViewModel.isCheater = false // Reset cheat status on question change
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
    }

}