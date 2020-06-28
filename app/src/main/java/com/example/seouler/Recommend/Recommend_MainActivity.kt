package com.example.seouler.Recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.example.seouler.Like.map_like
import com.example.seouler.R
import com.example.seouler.dataClass.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chattingmessage.*
import kotlinx.android.synthetic.main.activity_recommend__main.*

var buffer = "" // api 호출 결과를 담아온다.

class Recommend_MainActivity : AppCompatActivity() {
    lateinit var uid: String
    var sis: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend__main)

        sis = savedInstanceState

        uid = intent.getLongExtra("userId", 0).toString()
        var fireRef = FirebaseDatabase.getInstance().getReference("Like")
        var first = true

        var readUID = object  :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    if(uid == data.key as String) {
                        // 저장된 데이터가 있음
                        first = false
                        val map_l = mutableMapOf<String, ContentItem>()
                        for(cid in data.children) {
                            Log.d("태그", "cid: ${cid.key as String}")
                            Log.d("태그", "cid: ${cid.child("contentid").value as String}")
                            Log.d("태그", "cid: ${cid.child("firstimage").value as String}")
                            //Log.d("태그", "cid: ${cid.child("readcount").value as Int}")
                            Log.d("태그", "cid: ${cid.child("title").value as String}")
                            map_l[cid.key as String] = ContentItem(cid.child("contentid").value as String,
                                cid.child("firstimage").value as String, 0,
                                cid.child("title").value as String)
                        }
                        Log.d("태그", "읽어오기 완료")
                        map_like = map_l
                    }
                }
                // 저장된 데이터가 없을 경우
                if(first) {

                }


            }
        }
        fireRef.addListenerForSingleValueEvent((readUID))

        if(isFirstOk) {
            // 추천 페이지 아이템 초기화
            GetContentTask(list_content_attraction, list_keyword_attraction,
                "76","A01", "A0101", null, this).execute()
            Log.d("태그", "실행")
            GetContentTask(list_content_cultural, list_keyword_cultural,
                "75","A02", "A0206", "A02060100", this).execute()
            GetContentTask(list_content_accommodation, list_keyword_accommodation,
                "80","B02", "B0201", "B02010100", this).execute()
            GetContentTask(list_content_shopping, list_keyword_shopping,
                "79","A04", "A0401", "A04010200", this).execute()
            GetContentTask(list_content_cuisine, list_keyword_cuisine,
                "82","A05", "A0502", "A05020100", this).execute()
            isFirstOk = false
        }
        else {
            DisplayTask("76", this).execute()
            DisplayTask("75", this).execute()
            DisplayTask("80", this).execute()
            DisplayTask("79", this).execute()
            DisplayTask("82", this).execute()
        }

        btn_plus_attraction.setOnClickListener {
            showPlusDialog("Attraction")
        }

        btn_plus_cultural.setOnClickListener {
            showPlusDialog("Cultural")
        }

        btn_plus_accommodation.setOnClickListener {
            showPlusDialog("Accommodation")
        }

        btn_plus_shopping.setOnClickListener {
            showPlusDialog("Shopping")
        }

        btn_plus_cuisine.setOnClickListener {
            showPlusDialog("Cuisine")
        }
    }

    fun showPlusDialog(contentType: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        builder.setTitle("Choose Keywords!")

        var check: BooleanArray
        when(contentType) {
            "Attraction" -> {
                check = checkItem_attraction.clone()
                // item을 선택할 경우 check상태를 true로 저장한다.
                builder.setMultiChoiceItems(category_attraction, check) { dialog, which, isChecked -> // categoryArray
                    check[which] = isChecked
                }
                builder.setPositiveButton("Done") { dialog, which ->
                    // item의 check상태를 반영
                    checkItem_attraction = check
                    val list_k = ArrayList<KeywordItem>()
                    val list_c = ArrayList<ContentItem>()
                    for(i in category_attraction.indices) {
                        if (checkItem_attraction[i]) {
                            list_k.add(KeywordItem(category_attraction[i]))
                            map_attractionCat.get(category_attraction[i])?.cat1?.let { it1 ->
                                GetContentTask(
                                    list_c, list_k, "76", it1,
                                    map_attractionCat.get(category_attraction[i])!!.cat2, null, this
                                ).execute()
                            }
                        }
                    }
                }
            }
            "Cultural" -> {
                check = checkItem_cultural.clone()
                builder.setMultiChoiceItems(category_cultural, check) { dialog, which, isChecked -> // categoryArray
                    check[which] = isChecked
                }
                builder.setPositiveButton("Done") { dialog, which ->
                    checkItem_cultural = check
                    val list_k = ArrayList<KeywordItem>()
                    val list_c = ArrayList<ContentItem>()
                    for(i in category_cultural.indices) {
                        if (checkItem_cultural[i]) {
                            list_k.add(KeywordItem(category_cultural[i]))
                            map_culturalCat.get(category_cultural[i])?.cat1?.let { it1 ->
                                GetContentTask(
                                    list_c, list_k, "75", it1, map_culturalCat.get(category_cultural[i])!!.cat2,
                                    map_culturalCat.get(category_cultural[i])!!.cat3, this).execute()
                            }
                        }
                    }
                }
            }
            "Accommodation" -> {
                check = checkItem_accommodation.clone()
                builder.setMultiChoiceItems(category_accommodation, check) { dialog, which, isChecked -> // categoryArray
                    check[which] = isChecked
                }
                builder.setPositiveButton("Done") { dialog, which ->
                    checkItem_accommodation = check
                    val list_k = ArrayList<KeywordItem>()
                    val list_c = ArrayList<ContentItem>()
                    for(i in category_accommodation.indices) {
                        if (checkItem_accommodation[i]) {
                            list_k.add(KeywordItem(category_accommodation[i]))
                            map_accommodationCat.get(category_accommodation[i])?.cat1?.let { it1 ->
                                GetContentTask(
                                    list_c, list_k, "80", it1, map_accommodationCat.get(category_accommodation[i])!!.cat2,
                                    map_accommodationCat.get(category_accommodation[i])!!.cat3, this).execute()
                            }
                        }
                    }
                }
            }
            "Shopping" -> {
                check = checkItem_shopping.clone()
                builder.setMultiChoiceItems(category_shopping, check) { dialog, which, isChecked -> // categoryArray
                    check[which] = isChecked
                }
                builder.setPositiveButton("Done") { dialog, which ->
                    checkItem_shopping = check
                    val list_k = ArrayList<KeywordItem>()
                    val list_c = ArrayList<ContentItem>()
                    for(i in category_shopping.indices) {
                        if (checkItem_shopping[i]) {
                            list_k.add(KeywordItem(category_shopping[i]))
                            map_shoppingCat.get(category_shopping[i])?.cat1?.let { it1 ->
                                GetContentTask(
                                    list_c, list_k, "79", it1, map_shoppingCat.get(category_shopping[i])!!.cat2,
                                    map_shoppingCat.get(category_shopping[i])!!.cat3, this).execute()
                            }
                        }
                    }
                }
            }
            "Cuisine" -> {
                check = checkItem_cuisine.clone()
                builder.setMultiChoiceItems(category_cuisine, check) { dialog, which, isChecked -> // categoryArray
                    check[which] = isChecked
                }
                builder.setPositiveButton("Done") { dialog, which ->
                    checkItem_cuisine = check
                    val list_k = ArrayList<KeywordItem>()
                    val list_c = ArrayList<ContentItem>()
                    for(i in category_cuisine.indices) {
                        if (checkItem_cuisine[i]) {
                            list_k.add(KeywordItem(category_cuisine[i]))
                            map_cuisineCat.get(category_cuisine[i])?.cat1?.let { it1 ->
                                GetContentTask(
                                    list_c, list_k, "82", it1, map_cuisineCat.get(category_cuisine[i])!!.cat2,
                                    map_cuisineCat.get(category_cuisine[i])!!.cat3, this).execute()
                            }
                        }
                    }
                }
            }
            //else ->
        }

        builder.setNegativeButton("Cancel", null)

        val dialog : AlertDialog = builder.create()

        dialog.show()
    }

    override fun onRestart() {
        super.onRestart()
        setContentView(R.layout.activity_recommend__main)


        this.findViewById<ImageButton>(R.id.btn_plus_attraction).setOnClickListener {
            showPlusDialog("Attraction")
            Log.d("태그", "버튼 굿")
        }

        this.findViewById<ImageButton>(R.id.btn_plus_cultural).setOnClickListener {
            showPlusDialog("Cultural")
        }

        this.findViewById<ImageButton>(R.id.btn_plus_accommodation).setOnClickListener {
            showPlusDialog("Accommodation")
        }

        this.findViewById<ImageButton>(R.id.btn_plus_shopping).setOnClickListener {
            showPlusDialog("Shopping")
        }

        this.findViewById<ImageButton>(R.id.btn_plus_cuisine).setOnClickListener {
            showPlusDialog("Cuisine")
        }

        DisplayTask("76", this).execute()
        DisplayTask("75", this).execute()
        DisplayTask("80", this).execute()
        DisplayTask("79", this).execute()
        DisplayTask("82", this).execute()

    }


    override fun onBackPressed() {
        super.onBackPressed()

        var fireRef2 = FirebaseDatabase.getInstance().getReference("Like")
        fireRef2.child(uid).removeValue()
        Log.d("태그", "remove uid $uid")

        val mapToArray = map_like.values.toTypedArray()
        for(i in mapToArray.indices) {
            val m = mapToArray[i]
            Log.d("태그", "set uid $uid")
            fireRef2.child(uid).child(m.contentid as String).setValue(ContentItem(m.contentid, m.firstimage, 0, m.title))
        }
    }
}

