package com.example.seouler.Recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R
import kotlinx.android.synthetic.main.activity_recommend__main.*

var buffer = "" // api 호출 결과를 담아온다.

class Recommend_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend__main)

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
}
