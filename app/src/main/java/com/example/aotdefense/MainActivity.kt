package com.example.aotdefense

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var progressCharacter = 0
    private var progressTitan = 0
    private var currentCharacter = "艾連"

    private lateinit var btnStart: Button
    private lateinit var btnChangeCharacter: Button
    private lateinit var sbCharacter: SeekBar
    private lateinit var sbTitan: SeekBar
    private lateinit var tvSelectedCharacter: TextView
    private lateinit var tvCharacter: TextView  // ✅ 加這一行

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> {
                sbCharacter.progress = progressCharacter
                if (progressCharacter >= 100 && progressTitan < 100) {
                    showToast("$currentCharacter 勝利！")
                    btnStart.isEnabled = true
                }
            }
            2 -> {
                sbTitan.progress = progressTitan
                if (progressTitan >= 100 && progressCharacter < 100) {
                    showToast("巨人勝利！")
                    btnStart.isEnabled = true
                }
            }
        }
        true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btn_start)
        btnChangeCharacter = findViewById(R.id.btn_change_character)
        sbCharacter = findViewById(R.id.sbCharacter)
        sbTitan = findViewById(R.id.sbTitan)
        tvSelectedCharacter = findViewById(R.id.tv_selected_character)
        tvCharacter = findViewById(R.id.tvCharacter)  // ✅ 綁定 ID

        tvSelectedCharacter.text = "目前角色：$currentCharacter"

        btnChangeCharacter.setOnClickListener {
            currentCharacter = when (currentCharacter) {
                "艾連" -> "米卡莎"
                "米卡莎" -> "兵長"
                else -> "艾連"
            }
            tvSelectedCharacter.text = "目前角色：$currentCharacter"
            tvCharacter.text = currentCharacter  // ✅ 同步角色名
        }

        btnStart.setOnClickListener {
            startBattle()
        }
    }

    private fun startBattle() {
        progressCharacter = 0
        progressTitan = 0
        sbCharacter.progress = 0
        sbTitan.progress = 0
        btnStart.isEnabled = false

        // 玩家角色 Thread
        Thread {
            while (progressCharacter < 100 && progressTitan < 100) {
                val delay = when (currentCharacter) {
                    "艾連" -> (100..200L).random()
                    "米卡莎" -> (80..160L).random()
                    "兵長" -> (60..120L).random()
                    else -> 150L
                }
                Thread.sleep(delay)
                progressCharacter += (1..5).random()
                handler.sendEmptyMessage(1)
            }
        }.start()

        // 巨人 Thread
        Thread {
            while (progressTitan < 100 && progressCharacter < 100) {
                Thread.sleep((150..250L).random())
                progressTitan += (1..3).random()
                handler.sendEmptyMessage(2)
            }
        }.start()
    }
}
