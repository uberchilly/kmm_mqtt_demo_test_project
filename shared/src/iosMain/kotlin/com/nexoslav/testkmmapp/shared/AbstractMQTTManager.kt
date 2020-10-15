package com.nexoslav.testkmmapp.shared

import SwiftMQTT

actual abstract class AbstractMQTTManager {
    var mqttSession: MQTTSession?

    actual fun createMQTT(broker: String, clientId: String) {
        mqttSession = MQTTSession(
            host = broker,
            port = port,
            clientID = clientId,
            cleanSession = true,
            keepAlive = 15,
            useSSL = true
        )
        mqttSession.delegate = object : MQTTSessionDelegate() {
            override fun mqttDidReceive(message: MQTTMessage, session: MQTTSession) {
                onNewMessage(message.topic, message.nestoCeVidimo)
            }

            override fun mqttDidDisconnect(session: MQTTSession, error: MQTTSessionError) {
                if (error == MQTTSessionError.none) {
                    print("Successfully disconnected from MQTT broker")
                } else {
                    print(error.description)
                }
            }
        }
    }

    actual fun connect(username: String, password: String) {
        //todo:
        if (username.isNotEmpty() && password.isNotEmpty()) {
            mqttSession?.username = username
            mqttSession?.password = password
        }
        mqttSession?.connect { error ->
            if (error == MQTTSessionError.none) {
                onConnected()
                subscribeToEverything()
            } else {
                onConnectionFailed(error)
            }
        }
    }

    actual fun disconnect() {
        mqttSession?.disconnect()
    }

    protected actual fun subscribe(topic: String, qos: Int) {
        mqttSession?.subscribe(to = topic, delivering = qos, null)
    }

    protected actual fun unsubscribe(topic: String) {
        mqttSession?.unSubscribe(from = topic, null)
    }

    actual fun destroyMQTT() {
        mqttSession = null
    }

    protected actual fun sendMessage(topic: String, message: String, qos: Int, retained: Boolean) {
        mqttSession.publish(message, topic = topic, delivering = qos, retain= retained, null)
    }

    protected actual abstract fun subscribeToEverything()

    protected actual abstract fun onConnectionFailed(throwable: Throwable)

    protected actual abstract fun onConnectionLost(throwable: Throwable)

    protected actual abstract fun onConnected()

    protected actual abstract fun onNewMessage(topic: String, message: String)

}