package xyz.alover.mqtt;

public class MqttParams {

    /**
     * 定义了所有话题
     */
    public static final String TOPIC_LED="/mqtt/led/control";
    public static final String TOPIC_LED_STATUS="/mqtt/led/status";
    public static final String TOPIC_DHT11="/mqtt/dht11/control";
    public static final String TOPIC_DHT11_STATUS="/mqtt/dht11/status";
    public static final String TOPIC_VOLTAGE="/mqtt/voltage/control";
    public static final String TOPIC_VOLTAGE_STATUS="/mqtt/voltage/status";
    public static final String TOPIC_PWM="/mqtt/pwm/control";
    public static final String TOPIC_PWM_STATUS="/mqtt/pwm/status";

    public static final String CMD_LED_ON="led:1001";
    public static final String CMD_LED_OFF="led:1002";
    public static final String CMD_DHT11_ON="dht:1003";
    public static final String CMD_DHT11_OFF="dht:1004";
    public static final String CMD_VOLTAGE_ON="vol:1005";
    public static final String CMD_VOLTAGE_OFF="vol:1006";
    public static String CMD_PWM_DUTY="pwm:1050";



}
