package com.mbieniek.facebookimagepickersampleapp

import android.content.Context
import okhttp3.ResponseBody
import okio.Okio
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import io.reactivex.Observable

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object DataManager {
    val dataService: DataService
    val retrofit: Retrofit
    lateinit var path : String

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        dataService = retrofit.create(DataService::class.java)
    }


    fun downloadFileByUrlToFileDir(url: String): Observable<File> {
        return dataService.downloadFileByUrlRx(url)
                .flatMap({ response -> saveToDiskRx(response) })
    }

    private fun saveToDiskRx(response: Response<ResponseBody>): Observable<File> {

        return Observable.fromCallable<File> {
            try {
                val getFile = fun() : File {

                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
                    val now = Date()
                    val fileName = formatter.format(now) + ".jpg"
                    val pathName = path + "/" + fileName
                    val destinationFile = File(pathName)

                    val bufferedSink = Okio.buffer(Okio.sink(destinationFile))
                    bufferedSink.writeAll(response.body()!!.source())
                    bufferedSink.close()

                    return destinationFile
                }
                getFile()
            } catch (e: IOException) {
                 null
            }
        }
    }
}