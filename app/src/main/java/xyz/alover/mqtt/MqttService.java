package xyz.alover.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttService extends Service {
    private String host="tcp://106.14.216.18:1883";
    private int port=1883;
    private String userName="yize";
    private String password="zr2014520";
    private String publishTopic="/mqtt/smart_home/light0";
    private String subscribeTopic="/mqtt/smart_home/light0";
    private String client_id="device_control_manager";
    private MqttClient client;
    private static MqttConnectOptions connectOptions;

    public MqttService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
