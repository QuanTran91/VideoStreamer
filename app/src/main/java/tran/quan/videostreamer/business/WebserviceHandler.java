package tran.quan.videostreamer.business;

/**
 * Created by myrap_000 on 5/29/2016.
 */
public class WebserviceHandler {
    public final static WebserviceHandler INSTANCE = new WebserviceHandler();
    String mWebserviceAddress = "http://192.168.1.4:64015/";
    private WebserviceHandler(){

    };

    public String getWebserviceAddress(){
        return mWebserviceAddress;
    }

    public void updateWebservice(String webservice){
        mWebserviceAddress = webservice;
    }
}
