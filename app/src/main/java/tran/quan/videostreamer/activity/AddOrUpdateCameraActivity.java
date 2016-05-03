package tran.quan.videostreamer.activity;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import tran.quan.videostreamer.R;
import tran.quan.videostreamer.business.DatabaseHandler;
import tran.quan.videostreamer.model.CameraViewItem;

public class AddOrUpdateCameraActivity extends AppCompatActivity implements View.OnClickListener {

    CameraViewItem cameraViewItem;
    EditText cameraName,ipAddress;
    Button btnOk,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);
        cameraName = (EditText)findViewById(R.id.camera_name);
        ipAddress = (EditText)findViewById(R.id.ip_address);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnOk = (Button)findViewById(R.id.btn_ok);
        //Add button listener
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Long cameraId = (Long) bundle.get("cameraId");
            cameraViewItem = DatabaseHandler.INSTANCE.getCameraById(cameraId);
            cameraName.setText(cameraViewItem.getCameraName());
            ipAddress.setText(cameraViewItem.getUrl());
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            closeActivity();
            return;
        }

        if(v==btnOk){
            v.setEnabled(false);
            addCamera();
        }
    }

    private void addCamera() {
        CameraViewItem item = cameraViewItem==null? new CameraViewItem():cameraViewItem;
        item.setCameraName(cameraName.getText().toString());
        item.setUrl(ipAddress.getText().toString());
        DatabaseHandler.INSTANCE.addCamera(item);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}
