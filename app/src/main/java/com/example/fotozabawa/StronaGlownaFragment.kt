package com.example.fotozabawa

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fotozabawa.databinding.FragmentStronaGlownaBinding
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


class StronaGlownaFragment : Fragment() {
    private lateinit var appDatabase: AppDatabase
    private var _binding: FragmentStronaGlownaBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStronaGlownaBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        outputDirectory = getOutputDirectory()

        if(allPermissionGranted()){
            startCamera()
        }else{
            Toast.makeText(activity?.applicationContext,"Permissions requested", Toast.LENGTH_SHORT).show()
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,arrayOf(Manifest.permission.CAMERA),123)
            }
        }



        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 1000)

        val buttonStart = view.findViewById<Button>(R.id.button_start)
        buttonStart.setOnClickListener {

            runBlocking(Dispatchers.IO) {
                launch {
                    var czas_number = async { appDatabase.ustawieniaDao().getCzas() }
                    var tryb_number = async { appDatabase.ustawieniaDao().getTryb() }

//<----------Tutaj wydaje się dźwięk odliczenia do zaczęcia serii zdjęć---------->//

                    runBlocking {
                        for (x in 2..czas_number.await()) {
                            delay(1000)
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_INTERCEPT, 150)
                        }
                        delay(1000)
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_INTERCEPT, 300)
                        //<----------A tu się kończy---------->//

                    }
                    launch {
                        delay(1000)
       //<----------Tutaj co 3 sekundy wydajemy dźwięk i cykamy zdjęcie---------->//

                        for (x in 1..tryb_number.await()) {
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                            takePhoto()
                            delay(3000)
                        }
                    }

                }

            }
        }

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

    private fun getOutputDirectory(): File{
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let{ mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply{
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }

    private fun takePhoto(){
        val imageCapture = imageCapture?:return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(requireContext()),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "photo saved"
                    //Toast.makeText(requireActivity(), "$msg $savedUri", Toast.LENGTH_LONG).show()
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.d(Constants.TAG, "onError: ${exception.message}",exception)
                }
            }
        )
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { mPreview->
                mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            }catch (e:Exception){
                Log.d(Constants.TAG, "startCamera Fail:", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                startCamera()
            }else{
                Toast.makeText(requireContext(),"permissions not granted by the user", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun allPermissionGranted()= Constants.REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(requireActivity().baseContext,it)== PackageManager.PERMISSION_GRANTED
    }

}




