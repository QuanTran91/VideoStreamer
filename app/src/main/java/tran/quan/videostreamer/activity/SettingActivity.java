package tran.quan.videostreamer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tran.quan.videostreamer.R;
import tran.quan.videostreamer.business.WebserviceHandler;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText webApiAddress;
    Button btnOk,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        webApiAddress = (EditText)findViewById(R.id.web_address);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnOk = (Button)findViewById(R.id.btn_ok);
        //Add button listener
        webApiAddress.setText(WebserviceHandler.INSTANCE.getWebserviceAddress());
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            finish();
            return;
        }

        if(v==btnOk){
            v.setEnabled(false);
            WebserviceHandler.INSTANCE.updateWebservice(webApiAddress.getText().toString());
            finish();
        }
        return;
    }
}
