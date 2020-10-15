package com.nexoslav.testkmmapp.androidApp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nexoslav.testkmmapp.shared.Greeting
import com.nexoslav.testkmmapp.shared.MQTTManager

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    private lateinit var messageField: TextView
    private lateinit var connectionStatusText: TextView
    private lateinit var mqttManager: MQTTManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectionStatusText = findViewById(R.id.connectionStatusText)
        dealWithMqtt()

        val tv: TextView = findViewById(R.id.text_view)

        tv.text = greet()

        val subscribeBtn: Button = findViewById(R.id.topicSubscribeBtn)
        messageField = findViewById(R.id.messageField)
        val topicField: EditText = findViewById(R.id.topicEdit)

        subscribeBtn.setOnClickListener {
            val topic = topicField.text.toString()

        }
    }

    private fun dealWithMqtt() {
        mqttManager = MQTTManager()
        mqttManager.addConnectionStatusListener {
            runOnUiThread {
                connectionStatusText.text = it.toString()
            }
        }
        mqttManager.createMQTT("tcp://siot.net:1883", "text_client_id_android")
        mqttManager.connect("", "")
        mqttManager.subscribeToTopic("/test/topic") { topic, message ->
            runOnUiThread {
                messageField.text = "${messageField.text}\n topic: $topic, message: $message;"
            }
        }
    }
}
