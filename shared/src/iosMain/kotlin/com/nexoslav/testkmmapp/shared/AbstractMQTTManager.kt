package com.nexoslav.testkmmapp.shared

actual abstract class AbstractMQTTManager {
    actual fun createMQTT(broker: String, clientId: String) {
        //todo:
    }

    actual fun connect(username: String, password: String) {
        //todo:
    }

    actual fun disconnect() {
        //todo:
    }

    protected actual fun subscribe(topic: String, qos: Int) {
        //todo:
    }

    protected actual fun unsubscribe(topic: String) {
        //todo:
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