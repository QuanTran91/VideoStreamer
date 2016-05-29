package tran.quan.videostreamer.service.fcm.webservice.servicemodel;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.UUID;

/**
 * Created by myrap_000 on 5/23/2016.
 */

@Parcel
public class Camera
{
    @SerializedName("camera_id")
    private UUID id;

    @SerializedName("name")
    private String name;

    @SerializedName("video_url")
    private String videoUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}