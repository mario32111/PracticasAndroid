package com.example.ejemplonotificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var edt1: EditText
    lateinit var btn1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edt1 = findViewById(R.id.edt1)
        btn1 = findViewById(R.id.btn1)

        crearNotificacion()
        btn1.setOnClickListener {
            generarNotificacion()
        }
    }

    private fun crearNotificacion(){
        val name = "Mi primera notificacion"
        val chanelId= "bassic channel"
        val descriptionText = "esta es mi primera notificacion"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val chanel = NotificationChannel(chanelId, name, importance).apply {
            description = descriptionText
        }

        val nManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.createNotificationChannel(chanel)

    }

    private fun generarNotificacion(){
        val chanelId = "bassic channel"
        val notifiIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
        val notificacion = NotificationCompat.Builder(this@MainActivity, chanelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(notifiIcon)
            .setContentTitle("Mensaje importante")
            .setContentText("informatica.com.mx")
            .setSubText("Sitio Web")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(this@MainActivity)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notificationId++
            notify(notificationId, notificacion)
        }
    }

    companion object {
        var notificationId = 0
    }
}