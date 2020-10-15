package com.nexoslav.testkmmapp.shared

import kotlin.collections.set


class MQTTManager : AbstractMQTTManager() {
    private val connectionStatusListeners = mutableListOf<IConnectionStatusListener>()
    private val subscribedGuids: HashMap<String, MutableList<IMqttMessageListener>> = HashMap()

    fun addConnectionStatusListener(listener: IConnectionStatusListener) {
        if (!connectionStatusListeners.contains(listener)) {
            connectionStatusListeners.add(listener)
        }
    }

    fun removeConnectionStatusListener(listener: IConnectionStatusListener) {
        if (connectionStatusListeners.contains(listener)) {
            connectionStatusListeners.remove(listener)
        }
    }

    override fun subscribeToEverything() {
        if (subscribedGuids.isNotEmpty()) {
            for (topic in subscribedGuids.keys) {
                subscribe(topic, 2)
            }
        }
    }

    override fun onConnectionFailed(throwable: Throwable) {
        connectionStatusListeners.forEach { listener ->
            listener.onConnectionStatusChanged(MqttConnectionStatus.Fail(throwable))
        }
    }

    override fun onConnectionLost(throwable: Throwable) {
        onConnectionFailed(throwable)
    }

    override fun onConnected() {
        connectionStatusListeners.forEach { listener ->
            listener.onConnectionStatusChanged(MqttConnectionStatus.Success)
        }
    }

    fun sendMessageOnTopic(topic: String, message: String, qos: Int) {
        sendMessage(topic, message, qos, false)
    }


    override fun onNewMessage(topic: String, message: String) {
        //status must be true if message arrived
        val listeners = subscribedGuids[topic]
        if (listeners != null && listeners.isNotEmpty()) {
            for (listener in listeners) {
                listener.messageArrived(topic, message)
            }
        }
    }

    fun unsubscribeFromTopic(topic: String, mqttMessageListener: IMqttMessageListener) {
        val listeners = subscribedGuids[topic]
        if (listeners == null || listeners.isEmpty()) {
            unsubscribe(topic)
        } else {
            listeners.remove(mqttMessageListener)
            if (listeners.isEmpty()) {
                unsubscribe(topic)
                subscribedGuids.remove(topic)
            }
        }
    }

    fun subscribeToTopic(topic: String, messageListener: IMqttMessageListener) {
        subscribe(topic, 2)
        var listeners: MutableList<IMqttMessageListener>? = subscribedGuids[topic]
        if (listeners == null) {
            listeners = mutableListOf()
            listeners.add(messageListener)
            subscribedGuids[topic] = listeners
        } else {
            listeners.add(messageListener)
        }
    }
}

sealed class MqttConnectionStatus {
    object Success : MqttConnectionStatus()

    data class Fail(val throwable: Throwable?) : MqttConnectionStatus()
}

fun interface IMqttMessageListener {
    fun messageArrived(topic: String, message: String)
}

fun interface IConnectionStatusListener {
    fun onConnectionStatusChanged(connectionStatus: MqttConnectionStatus)
}
