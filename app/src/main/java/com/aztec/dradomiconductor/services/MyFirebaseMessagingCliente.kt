package com.aztec.dradomiconductor.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aztec.dradomiconductor.R
import com.aztec.dradomiconductor.channel.NotificationHelper
import com.aztec.dradomiconductor.receivers.AcceptReceiver
import com.aztec.dradomiconductor.receivers.CancelReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingCliente: FirebaseMessagingService() {

    private val NOTIFICATION_CODE = 100


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //Recibir la información
        val data = message.data
        val title = data["title"]
        val body = data["body"]
        val idBooking = data["idBooking"]
        Log.d("NOTIFICACION", "ID BOOKING ${idBooking}")

        if (!title.isNullOrBlank() && !body.isNullOrBlank()){
            if (idBooking != null){
                showNotificationActions(title, body, idBooking)
            }else{
                showNotifcation(title,body)
            }
        }
        //Enviar información
    }

    private fun showNotifcation(title: String, body: String){
        val helper = NotificationHelper(baseContext)
        val builder = helper.getNotification(title, body)
        //Mostrar la notificación
        helper.getManager().notify(1, builder.build())

    }

    private fun showNotificationActions(title: String, body: String, idBooking: String) {
        val helper = NotificationHelper(baseContext)

        // ACEPTAR VIAJE
        val acceptIntent = Intent(this, AcceptReceiver::class.java)
        acceptIntent.putExtra("idBooking", idBooking)
        var acceptPendingIntent: PendingIntent? = null

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            acceptPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, acceptIntent, PendingIntent.FLAG_MUTABLE)
        }
        else {
            acceptPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val actionAccept = NotificationCompat.Action.Builder(
            R.mipmap.ic_launcher,
            "Aceptar",
            acceptPendingIntent
        ).build()

        // CANCELAR VIAJE
        val cancelIntent = Intent(this, CancelReceiver::class.java)
        cancelIntent.putExtra("idBooking", idBooking)
        var cancelPendingIntent: PendingIntent? = null

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            cancelPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, cancelIntent, PendingIntent.FLAG_MUTABLE)
        }
        else {
            cancelPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val actionCancel = NotificationCompat.Action.Builder(
            R.mipmap.ic_launcher,
            "Cancelar",
            cancelPendingIntent
        ).build()

        val builder = helper.getNotificationActions(title, body, actionAccept, actionCancel)
        helper.getManager().notify(2, builder.build())
    }

}