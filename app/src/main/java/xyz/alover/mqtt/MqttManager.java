package xyz.alover.mqtt;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttManager {
    public static final String TAG=MqttManager.class.getSimpleName();

    private String clientId="device_control_manager";
    private int messageId=0;

    private MqttClient client;
    private static MqttConnectOptions connectOptions;

    public MqttManager(String clientId){
        this.clientId=clientId;
    }

    /**
     * 连接到服务器
     * @param host
     */
    public void connect(String host, String userName, final String password, final MqttStateListener listener){
        try{
            client=new MqttClient(host,clientId,new MemoryPersistence());
            connectOptions=new MqttConnectOptions();
            connectOptions.setUserName(userName);
            connectOptions.setPassword(password.toCharArray());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.i(TAG,"连接丢失");
                    listener.onConnectLost();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i(TAG,"收到订阅话题："+topic);
                    String payload=new String(message.getPayload());
                    Log.i(TAG,"收到订阅信息："+payload);
                    listener.onMessageArrived(topic,payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.i(TAG,"发布完毕");
                    listener.onDeliveryComplete();
                }
            });
            client.connect(connectOptions);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    /**
     * 订阅
     * @param topic
     * @param retain
     * @param qos
     */
    public void subscribe(String topic,boolean retain,int qos){
        if(client!=null){
            int[] Qos={qos};
            String[] Topic={topic};
            try{
                client.subscribe(Topic,Qos);
                Log.d(TAG,"订阅："+topic);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 发布
     * @param topic
     * @param message
     * @param retain
     * @param qos
     */
    public void publish(String topic,String message,boolean retain,int qos){
        if(client!=null){
            MqttMessage mqttMessage=new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setId(messageId++);
            mqttMessage.setRetained(retain);
            mqttMessage.setPayload(message.getBytes());
            try {
                client.publish(topic,mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消订阅
     * @param topic
     */
    public void unSubscribe(String topic){
        try {
            client.unsubscribe(topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



}
