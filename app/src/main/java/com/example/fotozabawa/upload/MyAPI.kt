package com.example.fotozabawa.upload

import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface MyAPI {
    @Multipart
    @POST("upload") //do zmiany "Api.php?apicall=upload"
    fun uploadImage(
        @Part("fromName") fromName: RequestBody,
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part,
        @Part image4: MultipartBody.Part,
        @Part image5: MultipartBody.Part,
        @Part image6: MultipartBody.Part,
        @Part("banner") banner: RequestBody
    ): retrofit2.Call<UploadResponse> //???

    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl("localhost:3000/") //do zmiany "http://10.10.10.118/ImageUploader/"
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }
}