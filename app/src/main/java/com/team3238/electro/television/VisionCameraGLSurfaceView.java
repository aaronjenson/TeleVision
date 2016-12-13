package com.team3238.electro.television;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CaptureRequest;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.team3238.electro.opencv.Camera2Renderer;
import com.team3238.electro.opencv.CameraGLSurfaceView;
import com.team3238.electro.opencv.CameraGLSurfaceView;

import java.util.HashMap;

/**
 * Created by aaron on 12/12/2016.
 */

public class VisionCameraGLSurfaceView extends CameraGLSurfaceView
        implements CameraGLSurfaceView.CameraTextureListener
{
    final static String TAG = "VisionCameraGLSurfaceView";

    static final int height = 480;
    static final int width = 640;
    private int frameCounter;
    private long lastNanoTime;

    static Camera2Renderer.Settings getCameraSettings() {
        Camera2Renderer.Settings settings = new Camera2Renderer.Settings();
        settings.height = height;
        settings.width = width;
        settings.camera_settings = new HashMap<>();
        settings.camera_settings.put(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_OFF);
        settings.camera_settings.put(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_OFF);
        settings.camera_settings.put(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_OFF);
        settings.camera_settings.put(CaptureRequest.SENSOR_EXPOSURE_TIME, 1000000L);
        settings.camera_settings.put(CaptureRequest.LENS_FOCUS_DISTANCE, .2f);
        return settings;
    }

    public VisionCameraGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs, getCameraSettings());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override public void onCameraViewStarted(int width, int height)
    {
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                Toast.makeText(getContext(), "onCameraViewStarted", Toast.LENGTH_SHORT).show();
            }
        });

        frameCounter = 0;
        lastNanoTime = System.nanoTime();
    }

    @Override public void onCameraViewStopped()
    {
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                Toast.makeText(getContext(), "onCameraViewStopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public boolean onCameraTexture(int texIn, int texOut, int width,
            int height, long system_time_millis)
    {
        return false;
    }
}
