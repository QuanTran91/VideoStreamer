package tran.quan.videostreamer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.orm.SugarContext;
import com.orm.SugarRecord;

import tran.quan.videostreamer.R;
import tran.quan.videostreamer.business.DatabaseHandler;
import tran.quan.videostreamer.model.CameraViewItem;

public class AddCameraActivity extends AppCompatActivity implements View.OnClickListener {

    EditText cameraName,ipAddress;
    AppCompatSpinner protocolSpinner;
    Button btnOk,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);
        cameraName = (EditText)findViewById(R.id.camera_name);
        ipAddress = (EditText)findViewById(R.id.ip_address);
        protocolSpinner = (AppCompatSpinner)findViewById(R.id.protocols);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnOk = (Button)findViewById(R.id.btn_ok);

        //Add button listener
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.video_protocol,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        protocolSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            closeActivity();
            return;
        }

        if(v==btnOk){
            addCamera();
        }
    }

    private void addCamera() {
        CameraViewItem item = new CameraViewItem();
        item.setCameraName(cameraName.getText().toString());
        item.setIpAddress(ipAddress.getText().toString());
        item.setProtocol(protocolSpinner.getSelectedItem().toString());
        DatabaseHandler.INSTANCE.addCamera(item);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}
