package com.example.fotozabawa

import android.Manifest
import android.content.ContentValues
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.fotozabawa.databinding.FragmentStronaGlownaBinding
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*


class StronaGlownaFragment : Fragment() {
    private var _binding: FragmentStronaGlownaBinding? = null
    private val binding get() = _binding!!
//    private var myCameraCaptureSession: CameraCaptureSession? = null
//    private var myCameraID: String? = null
//    private var myCameraManager: CameraManager? = null
//    private var myCameraDevice: CameraDevice? = null
//    private var myTextrureView: TextureView? = null
//    private var myCaptureRequestBuilder: CaptureRequest.Builder? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStronaGlownaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val texture = view.findViewById<TextureView>(R.id.textureView)
//        myTextrureView = view.findViewById(R.id.textureView)
//        myCameraManager = requireActivity().getSystemService(CAMERA_SERVICE) as CameraManager
//        openCamera()
//
//        Handler().postDelayed({ cameraPreview() }, 100)
        val myButton = view.findViewById<Button>(R.id.button_menu)
        myButton.setOnClickListener {
            val fragment: Fragment = MenuFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            requireActivity().title = "Menu"
        }
    }
}
//        val buttonStart = view.findViewById<Button>(R.id.button_start)
//        buttonStart.setOnClickListener {
//            val bitmap = texture.getBitmap()
//            val directory = Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DCIM + "/Camera"
//            Toast.makeText(activity?.applicationContext, directory, Toast.LENGTH_SHORT).show()
//
//            val imageReader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 2)
//            imageReader.setOnImageAvailableListener({ reader ->
//                val image = reader.acquireLatestImage()
//                val planes = image.planes
//                val buffer = planes[0].buffer
//                val bytes = ByteBuffer.allocate(buffer.capacity()).put(buffer).array()
//                val byteArray = bytes
//
//                // Save the byte[] to a file and insert it into the media store...
//                val myoutput = FileOutputStream(directory)
//                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, myoutput)
//                myoutput.close()
//
//            val values = ContentValues().apply {
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
//                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
//            }
//            val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//            val outputStream = requireContext().contentResolver.openOutputStream(uri!!)
//            outputStream!!.write(byteArray)
//            outputStream.close()
//            }, null)
//
//        }

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
//            myCameraID = myCameraManager!!.cameraIdList[1]
//            val CameraID = myCameraID
//            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED) }
//            if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) { //???
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
//    fun cameraPreview() {
//        val mySurfaceTexture = myTextrureView!!.surfaceTexture
//        val mySurface = Surface(mySurfaceTexture)
//        try {
//            myCaptureRequestBuilder = myCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//            myCaptureRequestBuilder!!.addTarget(mySurface)
//            myCameraDevice!!.createCaptureSession(Arrays.asList(mySurface), object : CameraCaptureSession.StateCallback() {
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



