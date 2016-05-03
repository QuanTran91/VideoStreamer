package tran.quan.videostreamer.model;


import com.orm.dsl.Table;

/**
 * Created by myrap_000 on 4/25/2016.
 */
@Table
public class CameraViewItem{
    private Long id;
    private String cameraName;
    private boolean isFavorite;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Long getId() {
        return id;
    }

    public CameraViewItem(){}
}
