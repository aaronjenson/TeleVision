package com.team3238.electro.television;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class CameraActivity extends AppCompatActivity
{
    private static final String TAG = "CameraActivity";

    VisionCameraGLSurfaceView visionView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
    {
        @Override public void onManagerConnected(int status)
        {
            switch(status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public CameraActivity()
    {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        visionView = (VisionCameraGLSurfaceView) findViewById(R.id.vision_view);
        visionView.setCameraTextureListener(visionView);
    }

    @Override public void onPause()
    {
        if (visionView != null) {
            visionView.onPause();
        }
        super.onPause();
    }

    @Override public void onResume()
    {
        super.onResume();
        if (visionView != null) {
            visionView.onResume();
        }
        if(!OpenCVLoader.initDebug())
        {
            Log.d(TAG,
                    "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this,
                    mLoaderCallback);
        } else
        {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_camera_settings)
        {
            openCameraSettings();
        }
        if(id == R.id.action_opencv_settings)
        {
            openCVSettings();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCameraSettings()
    {
        Log.i(TAG, "Calling openCameraSettings().");

        View view = getLayoutInflater()
                .inflate(R.layout.camera_settings, null);
//        LinearLayout container = (LinearLayout) view
//                .findViewById(R.id.camera_settings_fragment_container);

        final Dialog cameraSettingsDialog = new Dialog(CameraActivity.this,
                R.style.AppTheme_NoActionBar);
        cameraSettingsDialog.setContentView(view);
        cameraSettingsDialog.setCancelable(true);
        cameraSettingsDialog.getWindow()
                .setLayout(LinearLayout.LayoutParams.MATCH_PARENT, 680);
        cameraSettingsDialog.getWindow().setGravity(Gravity.BOTTOM);
        cameraSettingsDialog.show();

        RangeSeekBar<Integer> hBar = (RangeSeekBar<Integer>) view
                .findViewById(R.id.hValueBar);
        hBar.setSelectedMaxValue(254);
        hBar.setSelectedMinValue(118);
    }

    private void openCVSettings()
    {
        Log.i(TAG, "Calling openCVSettings().");
    }
}
