package com.example.seouler.dataClass

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class FirestoreRefs (uid : String) : Serializable{
    var firebase : List<FirebaseFirestore> = listOf(FirebaseFirestore.getInstance())
    var cUsersRef = listOf<CollectionReference>(firebase.get(0).collection("Users"))
    var dUserRef = listOf<DocumentReference>(cUsersRef.get(0).document(uid))
    //var firebase = FirebaseFirestore.getInstance()
    //var cUsersRef = firebase.collection("Users")
   // var dUserRef = cUsersRef.document(uid)

}