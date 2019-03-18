package com.example.thousandcounter

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Thread.sleep


class CounterActivity : AppCompatActivity() {

    lateinit var textNumber: TextView
    lateinit var button: Button

    private var countTask: CountingTask? = null

    var currNumber: Int = 0
    var state: State = State.Finished

    enum class State {
        InProgress, Finished, Stopped
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter)

        button = findViewById(R.id.button)
        textNumber = findViewById(R.id.number)

        button.setOnClickListener { buttonClickListener() }

        savedInstanceState?.run {
            currNumber = getString("CURRENT_NUMBER")?.toInt() ?: 0
            state = getSerializable("CURR_STATE") as State
        }
    }

    override fun onStart() {
        super.onStart()
        if (currNumber != 0) textNumber.text = currNumber.toString()
        if (state == State.InProgress) startTask(currNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("CURRENT_NUMBER", currNumber.toString())
        outState.putSerializable("CURR_STATE", state)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        cancelTask()
        super.onPause()
    }

    override fun onStop() {
        cancelTask()
        super.onStop()
    }

    private fun buttonClickListener() {
        when (state) {
            State.InProgress -> stopTask()
            State.Finished -> startTask()
            State.Stopped -> startTask(currNumber)
        }
    }

    private fun startTask(init: Int = 0) {
        state = State.InProgress
        countTask = CountingTask(init)
        countTask?.execute()
        button.setText(R.string.stop_button_text)
    }

    private fun stopTask() {
        state = State.Stopped
        this.countTask?.cancel(false)
        button.setText(R.string.start_button_text)
    }

    private fun cancelTask() {
        this.countTask?.cancel(false)

    }

    inner class CountingTask(init: Int = 0) : AsyncTask<Void, Int, Void>() {
        private var counter: Int = init

        override fun doInBackground(vararg params: Void?): Void? {
            while (counter < 1000) {
                if (isCancelled) {
                    return null
                }
                publishProgress(++counter)
                sleep(1000)
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            textNumber.text = numToString(values[0]?:0)
            currNumber = values[0] ?: 0
        }

        override fun onPostExecute(result: Void?) {
            state = State.Finished
            button.setText(R.string.start_button_text)
            super.onPostExecute(result)
        }
    }

    private fun numToString(num: Int): String {
        if ((num < 1) || (num > 1000)) throw IllegalArgumentException("Number be from 1 to 1000\n")
        if (num == 1000)
            return "тысяча"

        val builder = StringBuilder()

        var x: Int = num / 100
        if (x > 0) builder.append(
            when (x) {
                1 -> "сто "
                2 -> "двести "
                3 -> "триста "
                4 -> "четыреста "
                5 -> "пятьсот "
                6 -> "шестьсот "
                7 -> "семьсот "
                8 -> "восемьсот "
                9 -> "девятьсот "
                else -> ""
            }
        )
        x = num % 100
        if (x < 20) {
            builder.append(
                when (x) {
                    1 -> "один"
                    2 -> "два"
                    3 -> "три"
                    4 -> "четыре"
                    5 -> "пять"
                    6 -> "шесть"
                    7 -> "семь"
                    8 -> "восемь"
                    9 -> "девять"
                    10 -> "десять"
                    11 -> "одиннадцать"
                    12 -> "двенадцать"
                    13 -> "тринадцать"
                    14 -> "четырнадцать"
                    15 -> "пятнадцать"
                    16 -> "шестнадцать"
                    17 -> "семнадцать"
                    18 -> "восемнадцать"
                    19 -> "девятнадцать"
                    else -> ""
                }
            )
            return builder.toString()
        }
        builder.append(
            when (x / 10) {
                2 -> "двадцать "
                3 -> "тридцать "
                4 -> "сорок "
                5 -> "пятьдесят "
                6 -> "шестьдесят "
                7 -> "семьдесят "
                8 -> "восемьдесят "
                9 -> "девяносто "
                else -> ""
            }
        )
        builder.append(
            when(x % 10) {
                1 -> "один"
                2 -> "два"
                3 -> "три"
                4 -> "четыре"
                5 -> "пять"
                6 -> "шесть"
                7 -> "семь"
                8 -> "восемь"
                9 -> "девять"
                else -> ""
            }
        )

        return builder.toString()
    }
}