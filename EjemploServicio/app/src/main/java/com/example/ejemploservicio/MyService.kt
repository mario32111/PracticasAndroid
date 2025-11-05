package com.example.ejemploservicio

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import java.util.Timer
import java.util.TimerTask

class MyService : Service() {

    //manipula el proceso en segundo plano
    var handler1: Handler?= null
    var tiempo1: Timer = Timer()

    //cada cuando se va a actualizar
    var INTERVALO_ACTUALIZACION: Long = 100

    //contador para cambiar e numerito
    var cont1: Int = 0


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        iniciaConteo()

        handler1 = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message){
                UPDATE_LISTENER?.actualizarContador(cont1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        detenerConteo()
    }

    private fun iniciaConteo(){
        tiempo1.schedule(object : TimerTask(){
            //es el proceso que se mantiene en segundo plano
            override fun run() {
                cont1++
                handler1?.sendEmptyMessage(0)
            }

        }, 0, INTERVALO_ACTUALIZACION)
    }

    private fun detenerConteo(){
        if (tiempo1 != null){
            tiempo1.cancel()
        }
    }

    //esto es lo equivalente al statuc
    //en este caso solo esta escuchando cambios y actualiza el listener
    companion object {
        var UPDATE_LISTENER: MainActivity?= null

        fun setUpdateListener( pServicio: MainActivity){
            UPDATE_LISTENER = pServicio
        }
    }
}