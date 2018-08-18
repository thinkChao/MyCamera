package camera.cn.com.mycamera;


import android.graphics.Point;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MyCamera";
    private Camera mCamera = null;
    private Camera.Parameters mParameters;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private int mBackCameraId;
    private int mFrontCameraId;
    private int mBackOrientation;
    private int mFrontOrientation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(mSufaceHolderCallck);
        detectCamera();
        openCamera();
        mCamera.setPreviewCallback(mPreviewCallback);
        setPreviewParams();
        //startPreview();
    }

    SurfaceHolder.Callback mSufaceHolderCallck = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e(TAG,"surfaceCreated()=======>>");
//            detectCamera();
//            openCamera();
//            setPreviewParams();
           startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e(TAG,"surfaceChanged()=======>>");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG,"surfaceDestroyed()=======>>");

        }
    };

    //第一步：检测相机资源
    private void detectCamera(){
        Log.e(TAG,"detectCamera()=======>>");

        Display display = getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);//不能省略,必须有
        int screenWidth = outSize.x;//得到屏幕的宽度
        int screenHeight = outSize.y;//得到屏幕的高度
        Log.e(TAG,"detectCamera()=======>>" + "screenWidth = " + screenWidth + "screenHeight = " +screenHeight );

        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i = 0;i<numberOfCameras;i++){
            final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i,cameraInfo);
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                mBackCameraId = i;
                mBackOrientation = cameraInfo.orientation;
            }else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                mFrontCameraId = i;
                mFrontOrientation = cameraInfo.orientation;
            }
        }
    }
    //第二步：打开相机
    private void openCamera(){
        Log.e(TAG,"openCamera()=======>>");
        mCamera = Camera.open(mBackCameraId);//打开后置相机
    }

    //第三步：设置预览参数
    private void setPreviewParams(){
        Log.e(TAG,"setPreviewParams()=======>>");
        mParameters = mCamera.getParameters();
        mParameters.setPreviewSize(1920,1080);//这里要设置成设备支持的尺寸，否则setParameters会报错
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(mParameters);
        mCamera.setDisplayOrientation(90);//这个不属于参数
    }

    //第四步：开启预览
    private void startPreview(){
        Log.e(TAG,"startPreview()=======>>");
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            Log.e(TAG,"SurfaceView Size:" + mSurfaceView.getWidth() + " * " + mSurfaceView.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback(){

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
           // Log.e(TAG,"onPreviewFrame()=======>>");
        }
    };

    //第五步：拍照
    private void takePicture(){
        mCamera.takePicture(null,null,mPictureCallback);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //保存照片
        }
    };

}
