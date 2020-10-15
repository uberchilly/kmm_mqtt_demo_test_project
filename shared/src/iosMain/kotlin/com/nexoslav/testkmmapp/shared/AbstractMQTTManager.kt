package com.nexoslav.testkmmapp.shared

//import SwiftMQTT

actual abstract class AbstractMQTTManager {
//    var mqttSession: MQTTSession?

    actual fun createMQTT(broker: String, clientId: String) {
//        mqttSession = MQTTSession(
//            host = broker,
//            port = port,
//            clientID = clientId,
//            cleanSession = true,
//            keepAlive = 15,
//            useSSL = true
//        )
//        mqttSession.delegate = this
    }

    actual fun connect(username: String, password: String) {
        //todo:
//        if (username.isNotEmpty() && password.isNotEmpty()) {
//            mqttSession?.username = username
//            mqttSession?.password = password
//        }
//        mqttSession?.connect { error ->
//            if (error == .none) {
//                onConnected()
//                subscribeToEverything()
//            } else {
//                onConnectionFailed(error)
//            }
//        }
    }

    actual fun disconnect() {
//        mqttSession?.disconnect()
    }

    protected actual fun subscribe(topic: String, qos: Int) {
        //todo:
//        mqttSession?.subscribe(to = topic, delivering = .atLeastOnce) { error ->
//        }
    }

    protected actual fun unsubscribe(topic: String) {
//        mqttSession?.unSubscribe(from = topic, { error ->
//        })
    }

    actual fun destroyMQTT() {
        //todo:
    }

    protected actual fun sendMessage(topic: String, message: String, qos: Int, retained: Boolean) {
        //todo:
    }

    protected actual abstract fun subscribeToEverything()

    protected actual abstract fun onConnectionFailed(throwable: Throwable)

    protected actual abstract fun onConnectionLost(throwable: Throwable)

    protected actual abstract fun onConnected()

    protected actual abstract fun onNewMessage(topic: String, message: String)

}