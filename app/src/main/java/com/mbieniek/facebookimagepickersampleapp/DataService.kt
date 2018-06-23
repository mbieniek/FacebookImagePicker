package com.mbieniek.facebookimagepickersampleapp

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DataService {

    @Streaming
    @GET
    abstract fun downloadFileByUrlRx(@Url fileUrl: String): Observable<Response<ResponseBody>>


}