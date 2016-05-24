package tran.quan.videostreamer.service.fcm.webservice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tran.quan.videostreamer.service.fcm.webservice.servicemodel.Camera;

public interface CameraService {
    @GET("api/camera/getcamera")
    Call<List<Camera>> getCamera();
}