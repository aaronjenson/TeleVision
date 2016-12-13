package com.team3238.electro.television;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
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
        implements PopupMenu.OnMenuItemClickListener
{
    private static final String TAG = "CameraActivity";

    VisionCameraGLSurfaceView visionView;
    Dialog activeSettings;

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

    public void createOptionsMenu(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_camera);
        popup.show();
    }

    private void openCVCameraSettings()
    {
        Log.i(TAG, "Calling openCVCameraSettings().");

        if(activeSettings != null)
        {
            activeSettings.hide();
        }

        View view = getLayoutInflater().inflate(R.layout.opencv_settings, null);

        Dialog cameraSettingsDialog = new Dialog(CameraActivity.this,
                R.style.AppTheme_NoActionBar);
        cameraSettingsDialog.setContentView(view);
        cameraSettingsDialog.getWindow()
                .setLayout(LinearLayout.LayoutParams.MATCH_PARENT, 1000);
        cameraSettingsDialog.getWindow().setGravity(Gravity.BOTTOM);
        cameraSettingsDialog.show();
        activeSettings = cameraSettingsDialog;

        RangeSeekBar<Integer> hBar = (RangeSeekBar<Integer>) view
                .findViewById(R.id.hValueBar);
        hBar.setSelectedMaxValue(254);
        hBar.setSelectedMinValue(118);

        RangeSeekBar<Integer> sBar = (RangeSeekBar<Integer>) view
                .findViewById(R.id.sValueBar);
        sBar.setSelectedMaxValue(230);
        sBar.setSelectedMinValue(148);

        RangeSeekBar<Integer> vBar = (RangeSeekBar<Integer>) view
                .findViewById(R.id.vValueBar);
        vBar.setSelectedMaxValue(118);
        vBar.setSelectedMinValue(16);
    }

    private void openCameraSettings()
    {
        Log.i(TAG, "Calling openCameraSettings().");

        if(activeSettings != null)
        {
            activeSettings.hide();
        }
    }

    @Override public void onBackPressed()
    {
        if(activeSettings != null)
        {
            activeSettings.hide();
        } else
        {
            super.onBackPressed();
        }
    }

    @Override public boolean onMenuItemClick(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_camera_settings:
                openCameraSettings();
                return true;
            case R.id.action_opencv_settings:
                openCVCameraSettings();
                return true;
            default:
                Log.i(TAG, "Default onMenuItemClick() item case running");
                return false;
        }
    }
}
