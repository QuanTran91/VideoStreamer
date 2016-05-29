package tran.quan.videostreamer.service.fcm.webservice.servicemodel;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by myrap_000 on 5/29/2016.
 */
public class SensorEvent {
    @SerializedName("camera_id")
    private UUID cameraid;

    @SerializedName("event_type")
    private int eventType;

    @SerializedName("value")
    private double value;

    public UUID getCameraid() {
        return cameraid;
    }

    public void setCameraid(UUID cameraid) {
        this.cameraid = cameraid;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
