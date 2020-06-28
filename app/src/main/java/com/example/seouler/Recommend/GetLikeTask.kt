package com.example.seouler.Recommend

import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.example.seouler.Like.map_like
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Thread.sleep

class GetLikeTask(val uid: String, val view: Recommend_MainActivity) : AsyncTask<Any?, Any?, Any?>() {

    var firestore = FirebaseFirestore.getInstance()
    var cUsersRef = firestore.collection("Users")
    lateinit var dUserLikeRef : DocumentReference

    override fun doInBackground(vararg params: Any?): Any? {

        var tmp = hashMapOf(
            "uid" to uid
        )
        dUserLikeRef = cUsersRef.document(uid)
        dUserLikeRef.update(tmp as Map<String, Any>)
        Log.d("태그", "2${dUserLikeRef.id}")


        Log.d("태그", "시작")
        dUserLikeRef.get().addOnSuccessListener() {
            if(it.data?.get("OK") as String? != "OK") {
                //Log.d("태그", "1${OK}")
                countForLike = 0
                dUserLikeRef.update(mapOf("OK" to "OK"))
                dUserLikeRef.collection("Like")
                    .document("Count").set(mapOf("count" to 0))
                    .addOnFailureListener { e -> Log.w("태그", "Oh no", e) }
                dUserLikeRef.collection("Like")
                    .document(countForLike.toString())
                    .collection(countForLike.toString())
                    .document(null.toString())
                    .set(ContentItem(null, null, null, null))
                    .addOnSuccessListener { Log.d("태그", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("태그", "Error writing document", e) }
                sleep(3000)
            }
        }

        Log.d("태그", "count: $countForLike")

        dUserLikeRef.collection("Like").document("Count").get()
            .addOnSuccessListener {
                countForLike = it.get("count").toString().toInt()
                Log.d("태그", "count3: $countForLike")

                val map_l = mutableMapOf<String, ContentItem>()
                Log.d("태그", "count4: $countForLike")
                sleep(3000)
                dUserLikeRef.collection("Like").document(countForLike.toString())
                    .collection(countForLike.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val cid = document.get("contentid").toString()
                            map_l.put(
                                cid, ContentItem(
                                    cid,
                                    document.get("firstimage").toString(),
                                    document.get("readcount") as Int?,
                                    document.get("title").toString()
                                )
                            )
                        }
                        map_like = map_l
                    }
            }
        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        if(isFirstOk) {
            // 추천 페이지 아이템 초기화
            GetContentTask(list_content_attraction, list_keyword_attraction,
                "76","A01", "A0101", null, view).execute()
            Log.d("태그", "실행")
            GetContentTask(list_content_cultural, list_keyword_cultural,
                "75","A02", "A0206", "A02060100", view).execute()
            GetContentTask(list_content_accommodation, list_keyword_accommodation,
                "80","B02", "B0201", "B02010100", view).execute()
            GetContentTask(list_content_shopping, list_keyword_shopping,
                "79","A04", "A0401", "A04010200", view).execute()
            GetContentTask(list_content_cuisine, list_keyword_cuisine,
                "82","A05", "A0502", "A05020100", view).execute()
            isFirstOk = false
        }
        else {
            DisplayTask("76", view).execute()
            DisplayTask("75", view).execute()
            DisplayTask("80", view).execute()
            DisplayTask("79", view).execute()
            DisplayTask("82", view).execute()
        }
    }

}