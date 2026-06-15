package com.intek.rokidhermes

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity created")
        setContentView(buildContentView())
    }

    private fun buildContentView(): LinearLayout {
        val padding = 32
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(padding, padding, padding, padding)
            setBackgroundColor(Color.BLACK)

            addView(TextView(context).apply {
                text = "Rokid Hermes"
                setTextColor(Color.WHITE)
                textSize = 30f
                gravity = Gravity.CENTER
            })

            statusText = TextView(context).apply {
                text = "Listo para probar Hermes"
                setTextColor(Color.LTGRAY)
                textSize = 18f
                gravity = Gravity.CENTER
                setPadding(0, 28, 0, 28)
            }
            addView(statusText)

            addView(Button(context).apply {
                text = "Probar Hermes"
                textSize = 18f
                setOnClickListener { testHermesHealth() }
            })
        }
    }

    private fun testHermesHealth() {
        statusText.text = "Conectando con Hermes..."
        Log.d(TAG, "Checking Hermes health at $HEALTH_URL")

        Thread {
            val result = runCatching {
                val connection = (URL(HEALTH_URL).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5_000
                    readTimeout = 5_000
                }

                connection.use { conn ->
                    val code = conn.responseCode
                    Log.d(TAG, "Hermes health response code: $code")
                    if (code == HttpURLConnection.HTTP_OK) {
                        "Hermes conectado"
                    } else {
                        "Error Hermes: HTTP $code"
                    }
                }
            }.getOrElse { error ->
                Log.e(TAG, "Hermes health check failed", error)
                "Error Hermes: ${error.localizedMessage ?: error.javaClass.simpleName}"
            }

            runOnUiThread { statusText.text = result }
        }.start()
    }

    private inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T {
        return try {
            block(this)
        } finally {
            disconnect()
        }
    }

    companion object {
        private const val TAG = "RokidHermes"
        private const val HEALTH_URL = "http://192.168.0.234:8080/health"
    }
}
