package tran.quan.videostreamer.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tran.quan.videostreamer.R;
import tran.quan.videostreamer.adapter.CameraItemAdapter;
import tran.quan.videostreamer.business.DatabaseHandler;
import tran.quan.videostreamer.model.CameraViewItem;

public class VideoListFragment extends Fragment {

    RecyclerView cameraItemsList;
    CameraItemAdapter adapter;
    private OnFragmentInteractionListener mListener;
    public VideoListFragment() {
        // Required empty public constructor
    }
    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        cameraItemsList = (RecyclerView)view.findViewById(R.id.camera_items);
        List<CameraViewItem> items = DatabaseHandler.INSTANCE.getCameraList();
        adapter = new CameraItemAdapter(items);
        cameraItemsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        cameraItemsList.setItemAnimator(new DefaultItemAnimator());
        cameraItemsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        UpdateCameraList();
        super.onResume();
    }

    private void UpdateCameraList(){
        adapter.UpdateCameraList(DatabaseHandler.INSTANCE.getCameraList());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
