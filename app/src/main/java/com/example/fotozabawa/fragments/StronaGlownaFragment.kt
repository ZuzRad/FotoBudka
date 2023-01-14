package com.example.fotozabawa.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.fotozabawa.Constants
import com.example.fotozabawa.R
import com.example.fotozabawa.database.AppDatabase
import com.example.fotozabawa.databinding.FragmentStronaGlownaBinding
import com.example.fotozabawa.model.Ustawienia
import com.example.fotozabawa.upload.MyAPI
import com.example.fotozabawa.upload.UploadRequestBody
import com.example.fotozabawa.upload.UploadResponse
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

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
            runBlocking(Dispatchers.IO) {
                launch {
                    if (appDatabase.ustawieniaDao().exists()==false) {
                        runBlocking(Dispatchers.IO) { //narazie do testowania połączenia z serwerm, potem zmienić żeby po starcie aplikacji były to wartości startowe
                            appDatabase.ustawieniaDao().deleteAll()
                            var ustawienie = Ustawienia(1, 0, 1, 0)
                            appDatabase.ustawieniaDao().insert(ustawienie)
                        }
                    }
                }
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

            Handler().postDelayed( {
                Toast.makeText(requireContext(), list_paths.toString(), Toast.LENGTH_SHORT).show()
                uploadImages()
            }, 3000)


        }

        val myButton = view.findViewById<Button>(R.id.button_menu)
        myButton.setOnClickListener {
            Toast.makeText(requireContext(), list_paths.toString(), Toast.LENGTH_SHORT).show()

            val fragment: Fragment = MenuFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            requireActivity().title = "Menu"
        }


    }


    override fun onProgressUpdate(percentage: Int) {
        TODO("Not yet implemented")
    }


    private fun uploadImages(){
        var image1 :File = File("")
        var image2 :File = File("")
        var image3 :File = File("")
        var image4 :File = File("")
        var image5 :File = File("")
        var image6 :File = File("")
        var size = list_paths.size

        if(size==1){
            val name = list_paths[0].subSequence(69, list_paths[0].length)
            val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(), "r", null) ?: return

            image1= File(requireContext().cacheDir, name.toString())
            val inputStream1 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream1 = FileOutputStream(image1)
            inputStream1.copyTo(outputStream1)

            image2= File(requireContext().cacheDir, name.toString())
            val inputStream2 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream2 = FileOutputStream(image2)
            inputStream2.copyTo(outputStream2)

            image3= File(requireContext().cacheDir, name.toString())
            val inputStream3 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream3 = FileOutputStream(image3)
            inputStream3.copyTo(outputStream3)

            image4= File(requireContext().cacheDir, name.toString())
            val inputStream4 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream4 = FileOutputStream(image4)
            inputStream4.copyTo(outputStream4)

            image5= File(requireContext().cacheDir, name.toString())
            val inputStream5 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream5 = FileOutputStream(image5)
            inputStream5.copyTo(outputStream5)

            image6= File(requireContext().cacheDir, name.toString())
            val inputStream6 = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream6 = FileOutputStream(image6)
            inputStream6.copyTo(outputStream6)
        }
        else if(size == 2){
            val name1 = list_paths[0].subSequence(69, list_paths[0].length)
            val parcelFileDescriptor1 = requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(), "r", null) ?: return

            val name2 = list_paths[1].subSequence(69, list_paths[1].length)
            val parcelFileDescriptor2 = requireContext().contentResolver.openFileDescriptor(list_paths[1].toUri(), "r", null) ?: return

            image1= File(requireContext().cacheDir, name1.toString())
            val inputStream1 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream1 = FileOutputStream(image1)
            inputStream1.copyTo(outputStream1)

            image2= File(requireContext().cacheDir, name1.toString())
            val inputStream2 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream2 = FileOutputStream(image2)
            inputStream2.copyTo(outputStream2)

            image3= File(requireContext().cacheDir, name1.toString())
            val inputStream3 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream3 = FileOutputStream(image3)
            inputStream3.copyTo(outputStream3)

            image4= File(requireContext().cacheDir, name2.toString())
            val inputStream4 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream4 = FileOutputStream(image4)
            inputStream4.copyTo(outputStream4)

            image5= File(requireContext().cacheDir, name2.toString())
            val inputStream5 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream5 = FileOutputStream(image5)
            inputStream5.copyTo(outputStream5)

            image6= File(requireContext().cacheDir, name2.toString())
            val inputStream6 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream6 = FileOutputStream(image6)
            inputStream6.copyTo(outputStream6)
        }
        else if(size==3){
            val name1 = list_paths[0].subSequence(69, list_paths[0].length)
            val parcelFileDescriptor1 = requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(), "r", null) ?: return

            val name2 = list_paths[1].subSequence(69, list_paths[1].length)
            val parcelFileDescriptor2 = requireContext().contentResolver.openFileDescriptor(list_paths[1].toUri(), "r", null) ?: return

            val name3 = list_paths[2].subSequence(69, list_paths[2].length)
            val parcelFileDescriptor3 = requireContext().contentResolver.openFileDescriptor(list_paths[2].toUri(), "r", null) ?: return

            image1= File(requireContext().cacheDir, name1.toString())
            val inputStream1 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream1 = FileOutputStream(image1)
            inputStream1.copyTo(outputStream1)

            image2= File(requireContext().cacheDir, name1.toString())
            val inputStream2 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream2 = FileOutputStream(image2)
            inputStream2.copyTo(outputStream2)

            image3= File(requireContext().cacheDir, name2.toString())
            val inputStream3 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream3 = FileOutputStream(image3)
            inputStream3.copyTo(outputStream3)

            image4= File(requireContext().cacheDir, name2.toString())
            val inputStream4 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream4 = FileOutputStream(image4)
            inputStream4.copyTo(outputStream4)

            image5= File(requireContext().cacheDir, name3.toString())
            val inputStream5 = FileInputStream(parcelFileDescriptor3.fileDescriptor)
            val outputStream5 = FileOutputStream(image5)
            inputStream5.copyTo(outputStream5)

            image6= File(requireContext().cacheDir, name3.toString())
            val inputStream6 = FileInputStream(parcelFileDescriptor3.fileDescriptor)
            val outputStream6 = FileOutputStream(image6)
            inputStream6.copyTo(outputStream6)
        }
        else if(size==6){
            val name1 = list_paths[0].subSequence(69, list_paths[0].length)
            val parcelFileDescriptor1 = requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(), "r", null) ?: return

            val name2 = list_paths[1].subSequence(69, list_paths[1].length)
            val parcelFileDescriptor2 = requireContext().contentResolver.openFileDescriptor(list_paths[1].toUri(), "r", null) ?: return

            val name3 = list_paths[2].subSequence(69, list_paths[2].length)
            val parcelFileDescriptor3 = requireContext().contentResolver.openFileDescriptor(list_paths[2].toUri(), "r", null) ?: return

            val name4 = list_paths[3].subSequence(69, list_paths[3].length)
            val parcelFileDescriptor4 = requireContext().contentResolver.openFileDescriptor(list_paths[3].toUri(), "r", null) ?: return

            val name5 = list_paths[4].subSequence(69, list_paths[4].length)
            val parcelFileDescriptor5 = requireContext().contentResolver.openFileDescriptor(list_paths[4].toUri(), "r", null) ?: return

            val name6 = list_paths[5].subSequence(69, list_paths[5].length)
            val parcelFileDescriptor6 = requireContext().contentResolver.openFileDescriptor(list_paths[5].toUri(), "r", null) ?: return

            image1= File(requireContext().cacheDir, name1.toString())
            val inputStream1 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
            val outputStream1 = FileOutputStream(image1)
            inputStream1.copyTo(outputStream1)

            image2= File(requireContext().cacheDir, name2.toString())
            val inputStream2 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            val outputStream2 = FileOutputStream(image2)
            inputStream2.copyTo(outputStream2)

            image3= File(requireContext().cacheDir, name3.toString())
            val inputStream3 = FileInputStream(parcelFileDescriptor3.fileDescriptor)
            val outputStream3 = FileOutputStream(image3)
            inputStream3.copyTo(outputStream3)

            image4= File(requireContext().cacheDir, name4.toString())
            val inputStream4 = FileInputStream(parcelFileDescriptor4.fileDescriptor)
            val outputStream4 = FileOutputStream(image4)
            inputStream4.copyTo(outputStream4)

            image5= File(requireContext().cacheDir, name5.toString())
            val inputStream5 = FileInputStream(parcelFileDescriptor5.fileDescriptor)
            val outputStream5 = FileOutputStream(image5)
            inputStream5.copyTo(outputStream5)

            image6= File(requireContext().cacheDir, name6.toString())
            val inputStream6 = FileInputStream(parcelFileDescriptor6.fileDescriptor)
            val outputStream6 = FileOutputStream(image6)
            inputStream6.copyTo(outputStream6)
        }


//        var name = list_paths[0].subSequence(69, list_paths[0].length)
//        val parcelFileDescriptor =
//            requireContext().contentResolver.openFileDescriptor(list_paths[0].toUri(), "r", null) ?: return
//        val file = File(requireContext().cacheDir, name.toString()) //w tym miejscu się wywala... nie ma takiego pliku
//        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
        //val body = UploadRequestBody(file, "image", this)

            val body1 = UploadRequestBody(image1, "image", this)
            val body2 = UploadRequestBody(image2, "image", this)
            val body3 = UploadRequestBody(image3, "image", this)
            val body4 = UploadRequestBody(image4, "image", this)
            val body5 = UploadRequestBody(image5, "image", this)
            val body6 = UploadRequestBody(image6, "image", this)

        Log.d("Zdjęcie1---------------",image1.toString())
        Log.d("Zdjęcie2---------------",image2.toString())
        Log.d("Zdjęcie3---------------",image3.toString())
        Log.d("Zdjęcie4---------------",image4.toString())
        Log.d("Zdjęcie5---------------",image5.toString())
        Log.d("Zdjęcie6---------------",image6.toString())

            MyAPI().uploadImage(
                RequestBody.create(MediaType.parse("multipart/form-data"), "folder1"),
                MultipartBody.Part.createFormData("image1", image1.name, body1),
                MultipartBody.Part.createFormData("image2", image2.name, body2),
                MultipartBody.Part.createFormData("image3", image3.name, body3),
                MultipartBody.Part.createFormData("image4", image4.name, body4),
                MultipartBody.Part.createFormData("image5", image5.name, body5),
                MultipartBody.Part.createFormData("image6", image6.name, body6),
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




