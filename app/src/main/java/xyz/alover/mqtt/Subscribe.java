package xyz.alover.mqtt;

public class Subscribe {
    private String topic;
    private String message;
    private boolean subscribeStatus;

    public Subscribe(String topic, String message, boolean subscribeStatus) {
        this.topic = topic;
        this.message = message;
        this.subscribeStatus = subscribeStatus;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(boolean subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
}
