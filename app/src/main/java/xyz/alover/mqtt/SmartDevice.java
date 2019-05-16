package xyz.alover.mqtt;


/**
 * 智能设备的类型
 * LED、DHT11、ADC
 * 1、2、3
 *
 */

class SmartDeviceMqttInfo{
    private String topic;
    private String messageOn;
    private String messageOff;

    public SmartDeviceMqttInfo(String topic, String messageOn, String messageOff) {
        this.topic = topic;
        this.messageOn = messageOn;
        this.messageOff = messageOff;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageOn() {
        return messageOn;
    }

    public void setMessageOn(String messageOn) {
        this.messageOn = messageOn;
    }

    public String getMessageOff() {
        return messageOff;
    }

    public void setMessageOff(String messageOff) {
        this.messageOff = messageOff;
    }
}
public class SmartDevice {
    private String name;
    private String detail;
    private int imageId;
    private int type;
    private SmartDeviceMqttInfo smartDeviceMqttInfo;

    public SmartDevice(String name, String detail, int imageId, int type, SmartDeviceMqttInfo smartDeviceMqttInfo) {
        this.name = name;
        this.detail = detail;
        this.imageId = imageId;
        this.type = type;
        this.smartDeviceMqttInfo = smartDeviceMqttInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SmartDeviceMqttInfo getSmartDeviceMqttInfo() {
        return smartDeviceMqttInfo;
    }

    public void setSmartDeviceMqttInfo(SmartDeviceMqttInfo smartDeviceMqttInfo) {
        this.smartDeviceMqttInfo = smartDeviceMqttInfo;
    }
}
