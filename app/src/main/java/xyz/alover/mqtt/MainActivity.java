package xyz.alover.mqtt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage, tv_topic_message,tv_client_id;
    private EditText et_publish_topic, et_subscribe_topic, et_server_address, et_server_port,et_publish_topic_payload,et_username,et_password;
    private LinearLayout ll_setting,ll_main,ll_subscribe;
    private Button btn_publish_topic, btn_subscribe_topic, btn_connect_server,btn_clean_topic;
    private RecyclerView rv_device_list,rv_subscribe_list;
    private SeekBar sb_pwm;
    private List<SmartDevice> smartDeviceList=new ArrayList<>();
    private List<Subscribe> subscribeList=new ArrayList<>();
    private SubscribeAdapter subscribeAdapter;
    private SmartDeviceAdapter smartDeviceAdapter;

    private MqttManager mqttManager = new MqttManager(getIMEI());
    private MqttStateListener listener = new MqttStateListener() {
        @Override
        public void onConnectLost() {
            //btn_connect_server.setText("连接服务器");
        }

        @Override
        public void onMessageArrived(final String topic, final String payload) {
            StringBuilder sb=new StringBuilder();
            char payloadArray[]=payload.toCharArray();
            for(int i=0;i<payload.length();i++){
                if(payloadArray[i]>=0x20&&payloadArray[i]<=0x7E){
                    sb.append(payloadArray[i]);
                }
            }
            String cleanMessage=sb.toString();
            if(topic.contains(MqttParams.TOPIC_LED_STATUS)){
                if(cleanMessage.contains("on")){
                    subscribeList.get(0).setMessage("LED当前状态:开启");
                }else {
                    subscribeList.get(0).setMessage("LED当前状态:关闭");
                }

            }else if(topic.contains(MqttParams.TOPIC_DHT11_STATUS)){
                String temp=cleanMessage.substring(cleanMessage.indexOf("tmp")+4,cleanMessage.indexOf("hum")-1);
                String humi=cleanMessage.substring(cleanMessage.indexOf("hum")+4);
                subscribeList.get(1).setMessage("当前温度："+temp+"℃\t当前湿度："+humi+"%");
            }else if(topic.contains(MqttParams.TOPIC_PWM_STATUS)){
                String duty=cleanMessage.substring(cleanMessage.lastIndexOf(":")+1);
                subscribeList.get(2).setMessage("当前占空比:"+duty+"%");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv_topic_message.setText(topic + ":" + payload);
                    subscribeAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onDeliveryComplete() {
            //Toast.makeText(getApplicationContext(),"消息发布完成",Toast.LENGTH_SHORT);
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ll_setting.setVisibility(View.INVISIBLE);
                    ll_subscribe.setVisibility(View.INVISIBLE);
                    ll_main.setVisibility(View.VISIBLE);
//                    showAllDevice();
                    return true;
                case R.id.navigation_dashboard:
                    ll_main.setVisibility(View.INVISIBLE);
                    ll_setting.setVisibility(View.INVISIBLE);
                    ll_subscribe.setVisibility(View.VISIBLE);
//                    showAllSubscribe();
                    return true;
                case R.id.navigation_notifications:
                    ll_main.setVisibility(View.INVISIBLE);
                    ll_subscribe.setVisibility(View.INVISIBLE);
                    ll_setting.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initView();
        showAllDevice();
        showAllSubscribe();
    }

    /**
     * 初始化View
     */
    private void initView() {
        rv_device_list=(RecyclerView)findViewById(R.id.rv_device_list);
        rv_subscribe_list=(RecyclerView)findViewById(R.id.rv_subscribe_list);
        ll_setting=(LinearLayout)findViewById(R.id.ll_setting);
        ll_main=(LinearLayout)findViewById(R.id.ll_main);
        ll_subscribe=(LinearLayout)findViewById(R.id.ll_subscribe);
        tv_topic_message = (TextView) findViewById(R.id.tv_topic_message);
        sb_pwm=(SeekBar)findViewById(R.id.sb_pwm);
        et_publish_topic = (EditText) findViewById(R.id.et_publish_topic);
        et_publish_topic_payload = (EditText) findViewById(R.id.et_publish_topic_payload);
        et_subscribe_topic = (EditText) findViewById(R.id.et_subscribe_topic);
        et_server_address = (EditText) findViewById(R.id.et_server_address);
        et_server_port = (EditText) findViewById(R.id.et_server_port);
        tv_client_id = (TextView) findViewById(R.id.tv_client_id);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.et_password);
        btn_publish_topic = (Button) findViewById(R.id.btn_publish_topic);
        btn_subscribe_topic = (Button) findViewById(R.id.btn_subscribe_topic);
        btn_connect_server = (Button) findViewById(R.id.btn_connect_server);
        btn_clean_topic=(Button)findViewById(R.id.btn_clean_topic);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btn_publish_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "发布消息...", Toast.LENGTH_SHORT).show();
                mqttManager.publish(et_publish_topic.getText().toString(), et_publish_topic_payload.getText().toString(), false, 0);
                Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
            }
        });
        btn_subscribe_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "正在订阅消息...", Toast.LENGTH_SHORT).show();
                mqttManager.subscribe(et_subscribe_topic.getText().toString(), false, 0);
            }
        });

        btn_connect_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_connect_server.getText().toString().contains("已连接")) {
                    mqttManager.disconnect();
                    Toast.makeText(getApplicationContext(), "已经断开连接", Toast.LENGTH_SHORT).show();
                    btn_connect_server.setText("连接到服务器");
                    return;
                }
                btn_connect_server.setText("正在连接服务器...");
                String serverAddress = new String("tcp://" + et_server_address.getText().toString() + ":" + et_server_port.getText().toString());
                mqttManager.connect(serverAddress, et_username.getText().toString(), et_password.getText().toString(), listener);
                btn_connect_server.setText("已连接");
            }
        });

        btn_clean_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_topic_message.setText("");
            }
        });

        sb_pwm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mProgress=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mqttManager.publish(MqttParams.TOPIC_PWM,"pwm:"+(1000+mProgress),false,0);
            }
        });

    }

    private void showAllDevice(){
        smartDeviceInit();
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_device_list.setLayoutManager(layoutManager);
        smartDeviceAdapter=new SmartDeviceAdapter(smartDeviceList,mqttManager);
        rv_device_list.setAdapter(smartDeviceAdapter);
    }





    private void smartDeviceInit(){
        if(smartDeviceList.size()>0){
            smartDeviceList.clear();
        }
        smartDeviceList.add(new SmartDevice("室内灯光","主卧",R.mipmap.caramera,1,new SmartDeviceMqttInfo(MqttParams.TOPIC_LED,MqttParams.CMD_LED_ON,MqttParams.CMD_LED_OFF)));
        smartDeviceList.add(new SmartDevice("温湿度","客厅",R.mipmap.temp,1,new SmartDeviceMqttInfo(MqttParams.TOPIC_DHT11,MqttParams.CMD_DHT11_ON,MqttParams.CMD_DHT11_OFF)));
        //smartDeviceList.add(new SmartDevice("电压检测","客厅",R.mipmap.voltage,1,new SmartDeviceMqttInfo(MqttParams.TOPIC_VOLTAGE,MqttParams.CMD_VOLTAGE_ON,MqttParams.CMD_VOLTAGE_OFF)));
        smartDeviceList.add(new SmartDevice("PWM调光","主卧",R.mipmap.light,1,new SmartDeviceMqttInfo(MqttParams.TOPIC_PWM,MqttParams.CMD_PWM_DUTY,MqttParams.CMD_PWM_DUTY)));
    }

    private void showAllSubscribe(){
        subscribeInit();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rv_subscribe_list.setLayoutManager(layoutManager);
        subscribeAdapter=new SubscribeAdapter(subscribeList,mqttManager);
        rv_subscribe_list.setAdapter(subscribeAdapter);
    }


    private void subscribeInit(){
        if(subscribeList.size()>0){
            subscribeList.clear();
        }

        subscribeList.add(new Subscribe(MqttParams.TOPIC_LED_STATUS,"暂无",false));
        subscribeList.add(new Subscribe(MqttParams.TOPIC_DHT11_STATUS,"暂无",false));
        //subscribeList.add(new Subscribe(MqttParams.TOPIC_VOLTAGE_STATUS,"暂无",false));
        subscribeList.add(new Subscribe(MqttParams.TOPIC_PWM_STATUS,"暂无",false));


    }





    /**
     * 检查权限，网络读写等
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, 4);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 5);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 6);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 7);
        }

    }

    private String getIMEI() {
//        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 5);
//        }
//        String imei = "android_"+TelephonyMgr.getDeviceId();
        return "android_client_yize";
    }



}
