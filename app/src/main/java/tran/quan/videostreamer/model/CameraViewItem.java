package tran.quan.videostreamer.model;


import com.orm.dsl.Table;

import java.util.UUID;

/**
 * Created by myrap_000 on 4/25/2016.
 */
@Table
public class CameraViewItem{
    private Long id;
    private UUID cameraId;
    private String cameraName;
    private String url;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Long getId() {
        return id;
    }

    public UUID getCameraId() {
        return cameraId;
    }

    public void setCameraId(UUID cameraId) {
        this.cameraId = cameraId;
    }

    public CameraViewItem(){}
}
