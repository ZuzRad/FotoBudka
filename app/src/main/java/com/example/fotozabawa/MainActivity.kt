package com.example.fotozabawa

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.MenuItem
import android.view.Surface
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.IOException
import java.util.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.fotozabawa.R.layout.fragment_strona_glowna)

    }
}
//    //----------------------------wersja 1 klikając start pojawi się kamera w tle (jeszcze nie robi zdjęć)--------------------------------
//    private var myCameraCaptureSession: CameraCaptureSession? = null
//    private var myCameraID: String? = null
//    private var myCameraManager: CameraManager? = null
//    private var myCameraDevice: CameraDevice? = null
//    private var myTextrureView: TextureView? = null
//    private var myCaptureRequestBuilder: CaptureRequest.Builder? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(com.example.fotozabawa.R.layout.activity_main)
//        myTextrureView = findViewById(com.example.fotozabawa.R.id.textureView)
//        myCameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
//        openCamera()
//        //cameraPreview()
//    }
//
//    private val myStateCallBack: CameraDevice.StateCallback =
//        object : CameraDevice.StateCallback() {
//            override fun onOpened(camera: CameraDevice) {
//                myCameraDevice = camera
//            }
//
//            override fun onDisconnected(camera: CameraDevice) {
//                myCameraDevice!!.close()
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//                myCameraDevice!!.close()
//                myCameraDevice = null
//            }
//        }
//
//    private fun openCamera() {
//        try {
//            myCameraID = myCameraManager!!.cameraIdList[0]
//            val CameraID = myCameraID
//            ActivityCompat.requestPermissions(
//                this@MainActivity,
//                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                PackageManager.PERMISSION_GRANTED
//            )
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CAMERA
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }
//            if (CameraID != null) {
//                myCameraManager!!.openCamera(CameraID, myStateCallBack, null)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun cameraPreview(view: View?) {
//        val mySurfaceTexture = myTextrureView!!.surfaceTexture
//        val mySurface = Surface(mySurfaceTexture)
//        try {
//            myCaptureRequestBuilder =
//                myCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//            myCaptureRequestBuilder!!.addTarget(mySurface)
//            myCameraDevice!!.createCaptureSession(
//                Arrays.asList(mySurface), object : CameraCaptureSession.StateCallback() {
//                    override fun onConfigured(session: CameraCaptureSession) {
//                        myCameraCaptureSession = session
//                        myCaptureRequestBuilder!!.set(
//                            CaptureRequest.CONTROL_MODE,
//                            CameraMetadata.CONTROL_MODE_AUTO
//                        )
//                        try {
//                            myCameraCaptureSession!!.setRepeatingRequest(
//                                myCaptureRequestBuilder!!.build(),
//                                null,
//                                null
//                            )
//                        } catch (e: CameraAccessException) {
//                            e.printStackTrace()
//                        }
//                    }
//
//                    override fun onConfigureFailed(session: CameraCaptureSession) {}
//                }, null
//            )
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }

//    //-----------------------------------inna wersja robienia zdjęć(należy dodać w acivity_main funkcje CameraButton na onclicku przycisku)----------------------------------
//    var index = 0
//    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        .toString() + "/myCamera/"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(com.example.fotozabawa.R.layout.activity_main)
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
//            PackageManager.PERMISSION_GRANTED
//        )
//        val builder = VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
//    }
//
//    fun CameraButton(view: View?) {
//        index++
//        val file = "$directory$index.jpg"
//        val newFile = File(file)
//        try {
//            newFile.createNewFile()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val outputFileUri: Uri = Uri.fromFile(newFile)
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
//        startActivity(cameraIntent)
//    }
//}