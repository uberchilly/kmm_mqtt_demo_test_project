package com.nexoslav.testkmmapp.shared

expect abstract class AbstractMQTTManager {
    fun createMQTT(broker: String, clientId: String)
    fun connect(username: String, password: String)
    fun disconnect()
    protected fun subscribe(topic: String, qos: Int = 2)
    protected fun unsubscribe(topic: String)
    protected fun sendMessage(topic: String, message: String, qos: Int, retained: Boolean = false)
    fun destroyMQTT()

    protected abstract fun subscribeToEverything()

    protected abstract fun onConnectionFailed(throwable: Throwable)

    protected abstract fun onConnectionLost(throwable: Throwable)

    protected abstract fun onConnected()

    protected abstract fun onNewMessage(topic: String, message: String)
}