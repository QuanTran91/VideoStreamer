package tran.quan.videostreamer.service.fcm.webservice.servicemodel;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by myrap_000 on 5/23/2016.
 */

@Parcel
public class Camera
{
    @SerializedName("value")
    private Integer someValue;

    @SerializedName("message")
    private String strMessage;

    //Getters and setters
}