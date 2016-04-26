package tran.quan.videostreamer.model;


import com.orm.dsl.Table;

/**
 * Created by myrap_000 on 4/25/2016.
 */
@Table
public class CameraViewItem{

    private Long Id;
    private String cameraName;
    private boolean isFavorite;
    private String ipAddress;
    private String protocol;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getId() {
        return Id;
    }

    public CameraViewItem(){}
}
