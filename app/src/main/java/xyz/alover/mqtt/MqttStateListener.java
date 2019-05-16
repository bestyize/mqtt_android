package xyz.alover.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttStateListener {
    void onConnectLost();
    void onMessageArrived(String topic, String payload);
    void onDeliveryComplete();
}
