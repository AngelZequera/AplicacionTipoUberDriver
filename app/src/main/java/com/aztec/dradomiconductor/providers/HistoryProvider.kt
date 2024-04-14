package com.aztec.dradomiconductor.providers

import android.util.Log
import com.aztec.dradomiconductor.models.Booking
import com.aztec.dradomiconductor.models.Client
import com.aztec.dradomiconductor.models.History
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class HistoryProvider {
    val db = Firebase.firestore.collection("Histories")
    val authProvider = AuthProvider()


    fun create(history: History): Task<DocumentReference> {
       return db.add(history).addOnFailureListener {
           Log.d("FIRESTORE","Error: ${it.message}")
       }
    }

    fun getHistoryById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }

    fun getLastHistory(): Query{
        //Consulta compuesta - indice
        return db.whereEqualTo("idDriver",authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
    }

    fun getHistories(): Query{
        //Consulta compuesta - indice
        return db.whereEqualTo("idDriver",authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING)
    }


    fun getBooking(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId())
    }

    fun updateCalificationToCliente (id: String, calification: Float): Task<Void> {
        return db.document(id).update("calificationToClient", calification).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR ${exception.message}")
        }
    }
}