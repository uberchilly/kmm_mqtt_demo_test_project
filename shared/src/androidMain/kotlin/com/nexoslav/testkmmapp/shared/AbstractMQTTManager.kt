package com.nexoslav.testkmmapp.shared

import android.text.TextUtils
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

actual abstract class AbstractMQTTManager {
    private var publisher: MqttAsyncClient? = null
    actual fun createMQTT(broker: String, clientId: String) {
        if (publisher == null) {
            try {
                publisher = MqttAsyncClient(broker, clientId, MemoryPersistence())
                    .apply {
                        setCallback(object : MqttCallbackExtended {
                            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                                onConnected()
                                subscribeToEverything()
                            }

                            override fun connectionLost(cause: Throwable) {
                                onConnectionLost(cause)
                            }

                            override fun messageArrived(topic: String, message: MqttMessage) {
                                onNewMessage(topic, message.toString())
                            }

                            override fun deliveryComplete(token: IMqttDeliveryToken) {
                            }
                        })
                    }
            } catch (e: Exception) {
                onConnectionLost(e)
            }
        }
    }

    actual fun connect(username: String, password: String) {
        try {
            val options = MqttConnectOptions()
            options.isAutomaticReconnect = false
            options.isCleanSession = true
            options.isAutomaticReconnect = true
            options.connectionTimeout = 10
            if (username.isNotEmpty() && password.isNotEmpty()) {
                options.userName = username
                options.password = password.toCharArray()
            }
            publisher?.connect(options, Any(), object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    onConnectionFailed(exception)
                }
            })
        } catch (e: Exception) {
            onConnectionFailed(e)
        }
    }

    actual fun disconnect() {
        try {
            publisher?.disconnect()
        } catch (e: Exception) {
        }
    }

    protected actual fun subscribe(topic: String, qos: Int) {
        if (publisher?.isConnected != true) {
            return
        }

        try {
            publisher?.subscribe(topic, qos)
        } catch (e: MqttException) {
        }
    }

    protected actual fun unsubscribe(topic: String) {
        if (publisher?.isConnected != true) {
            return
        }

        try {
            publisher?.unsubscribe(topic)
        } catch (e: MqttException) {
        }
    }

    protected actual fun sendMessage(topic: String, message: String, qos: Int, retained: Boolean) {
        if (publisher?.isConnected != true) {
            return
        }
        if (TextUtils.isEmpty(topic))
            return

        if (TextUtils.isEmpty(message))
            return


        val msg = MqttMessage(message.toByteArray())
        msg.qos = qos
        msg.isRetained = retained

        try {
            publisher?.publish(topic, msg)
        } catch (e: Exception) {
        }

    }

    actual fun destroyMQTT() {
        try {
            publisher?.close(true)
        } catch (e: MqttException) {
        }

        publisher = null
    }

    protected actual abstract fun subscribeToEverything()

    protected actual abstract fun onConnectionFailed(throwable: Throwable)

    protected actual abstract fun onConnectionLost(throwable: Throwable)

    protected actual abstract fun onConnected()

    protected actual abstract fun onNewMessage(topic: String, message: String)
}