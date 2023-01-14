package com.example.fotozabawa.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.net.toUri
import com.example.fotozabawa.database.AppDatabase
import com.example.fotozabawa.Constants
import com.example.fotozabawa.R
import com.example.fotozabawa.model.Ustawienia
import com.example.fotozabawa.upload.MyAPI
import com.example.fotozabawa.upload.UploadRequestBody
import com.example.fotozabawa.upload.UploadResponse
import com.example.fotozabawa.upload.getFileName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class StronaGlownaFragment : Fragment(), UploadRequestBody.UploadCallback {
    private lateinit var appDatabase: AppDatabase
    private var _binding: FragmentStronaGlownaBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var list_paths = arrayListOf<String>()




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

            //if(appDatabase.ustawieniaDao().getCzas()==null)
            runBlocking(Dispatchers.IO) { //narazie do testowania połączenia z serwerm, potem zmienić żeby po starcie aplikacji były to wartości startowe
                appDatabase.ustawieniaDao().deleteAll()
                var ustawienie = Ustawienia(1,0,1,0)
                appDatabase.ustawieniaDao().insert(ustawienie)
            }
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


                    launch {
       //<----------Iteracje, zdjęcia, beepy---------->//

                        for (x in 1..tryb_number.await()) {

                            for (y in 1..czas_number.await()){
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                                delay(1000)
                            }
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 300)
                            delay(1000)
                            takePhoto()
                        }
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_LOW_SSL, 500)
                    }

                }

            }

            Toast.makeText(requireContext(), list_paths.toString(), Toast.LENGTH_SHORT).show() // w tym miejscu nie ma jeszcze danych w liście
            //uploadImages()
        }

        val myButton = view.findViewById<Button>(R.id.button_menu)
        myButton.setOnClickListener {
            Toast.makeText(requireContext(), list_paths.toString(), Toast.LENGTH_SHORT).show()
           uploadImages() //do testu zakomentować przejście do menu i odpalis wysyłanie zdjęcia
//            val fragment: Fragment = MenuFragment()
//            val fragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.frameLayout, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//            requireActivity().title = "Menu"
        }


    }


    override fun onProgressUpdate(percentage: Int) {
        TODO("Not yet implemented")
    }

    private fun uploadImages(){
       // for(item in list_paths){
            if(list_paths[0] == null){
                Toast.makeText(requireContext(), "plik jest nullem", Toast.LENGTH_SHORT).show()
                return
            }

            var name = list_paths[0].subSequence(69,list_paths[0].length)
            val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(),"r",null) ?:return
            val file = File(requireContext().cacheDir, name.toString()) //w tym miejscu się wywala... nie ma takiego pliku
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            Log.d("<-------URI--------> ",list_paths[0])
            Log.d("<-------FILE-------->",file.toString()+" uri")
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            val body = UploadRequestBody(file, "image", this)

            MyAPI().uploadImage(
                RequestBody.create(MediaType.parse("multipart/form-data"), "folder1"),
                MultipartBody.Part.createFormData("image1", file.name, body),
                MultipartBody.Part.createFormData("image2", file.name, body),
                MultipartBody.Part.createFormData("image3", file.name, body),
                MultipartBody.Part.createFormData("image4", file.name, body),
                MultipartBody.Part.createFormData("image5", file.name, body),
                MultipartBody.Part.createFormData("image6", file.name, body),
                RequestBody.create(MediaType.parse("multipart/form-data"), "space")
            ).enqueue(object: Callback<UploadResponse>{
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    Log.d("BŁĄD---------      ","UDAŁO SIĘ!!!!!!!!!!!!!!!!!!!!!")
                    Toast.makeText(requireContext(), "wysłano pliki", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                  Log.d("BŁĄD---------      ",t.message!!)
                }

            })

       // }
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
                    list_paths.add(savedUri.toString())

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




