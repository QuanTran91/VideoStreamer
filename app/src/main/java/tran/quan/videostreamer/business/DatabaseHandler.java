package tran.quan.videostreamer.business;

import com.orm.SugarRecord;

import java.util.List;

import tran.quan.videostreamer.model.CameraViewItem;

/**
 * Created by myrap_000 on 4/26/2016.
 */
public class DatabaseHandler {
    public final static DatabaseHandler INSTANCE = new DatabaseHandler();

    private DatabaseHandler(){}

    public List<CameraViewItem> getCameraList(){
        return SugarRecord.listAll(CameraViewItem.class);
    }

    public void addCamera(CameraViewItem item){
        SugarRecord.save(item);
    }
}
