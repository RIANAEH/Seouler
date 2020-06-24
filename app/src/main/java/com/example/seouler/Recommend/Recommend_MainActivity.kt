package com.example.seouler.Recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.seouler.R
import kotlinx.android.synthetic.main.activity_recommend__main.*

var buffer = "" // api 호출 결과를 담아온다.

class Recommend_MainActivity : AppCompatActivity() {

    fun selector(p: ContentItem): Int? = p.readcount // 조회수로 정렬하기 위함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend__main)

        // 분류코드를 이용해 해당하는 content를 가져온다.
        // 처음에는 임시로 분류코드를 정해 출력한다.
        val list_a = ArrayList<ContentItem>()
        list_keyword_attraction.add(KeywordItem("Natural Sites"))
        GetContentTask(list_a, "76","A01", "A0101", null).execute().get() // .get()을 붙여서 실행해야 이 실행이 끝날 때까지 기다려준다.
        list_content_attraction = list_a
        checkItem_attraction[0] = true

        recyclerView_content_attraction.adapter = ContentAdapter(list_content_attraction)
        recyclerView_keyword_attraction.adapter = KeywordAdapter(list_keyword_attraction)

        val list_l = ArrayList<ContentItem>()
        list_keyword_cultural.add(KeywordItem("Museums"))
        GetContentTask(list_l, "78","A02", "A0206", "A02060100").execute().get()
        list_content_cultural = list_l
        checkItem_cultural[0] = true

        recyclerView_content_cultural.adapter = ContentAdapter(list_content_cultural)
        recyclerView_keyword_cultural.adapter = KeywordAdapter(list_keyword_cultural)

        val list_ac = ArrayList<ContentItem>()
        list_keyword_accommodation.add(KeywordItem("Hotels(Modern)"))
        GetContentTask(list_ac, "80","B02", "B0201", "B02010100").execute().get()
        list_content_accommodation = list_ac
        checkItem_accommodation[0] = true

        recyclerView_content_accommodation.adapter = ContentAdapter(list_content_accommodation)
        recyclerView_keyword_accommodation.adapter = KeywordAdapter(list_keyword_accommodation)

        val list_s = ArrayList<ContentItem>()
        list_keyword_shopping.add(KeywordItem("Traditional Markets"))
        GetContentTask(list_s, "79","A04", "A0401", "A04010200").execute().get()
        list_content_shopping = list_s
        checkItem_shopping[0] = true

        recyclerView_content_shopping.adapter = ContentAdapter(list_content_shopping)
        recyclerView_keyword_shopping.adapter = KeywordAdapter(list_keyword_shopping)

        val list_c = ArrayList<ContentItem>()
        list_keyword_cuisine.add(KeywordItem("Korean Restaurants"))
        GetContentTask(list_c, "82","A05", "A0502", "A05020100").execute().get()
        list_content_cuisine = list_c
        checkItem_cuisine[0] = true

        recyclerView_content_cuisine.adapter = ContentAdapter(list_content_cuisine)
        recyclerView_keyword_cuisine.adapter = KeywordAdapter(list_keyword_cuisine)

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
                                    list_c, "76", it1, map_attractionCat.get(category_attraction[i])!!.cat2, null
                                ).execute().get()
                            }
                        }
                    }
                    list_keyword_attraction = list_k
                    list_content_attraction = list_c

                    // list_content를 조회수가 큰 순서로 정렬한다.
                    list_content_attraction.sortByDescending({selector(it)})

                    //새롭게 디스플레이한다.
                    recyclerView_content_attraction.adapter = ContentAdapter(list_content_attraction)
                    recyclerView_keyword_attraction.adapter = KeywordAdapter(list_keyword_attraction)

                    Toast.makeText(this, "Content Num: ${list_content_attraction.size}", Toast.LENGTH_LONG).show()
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
                                    list_c, "75", it1, map_culturalCat.get(category_cultural[i])!!.cat2,
                                    map_culturalCat.get(category_cultural[i])!!.cat3).execute().get()
                            }
                        }
                    }
                    list_keyword_cultural = list_k
                    list_content_cultural = list_c

                    // list_content를 조회수가 큰 순서로 정렬한다.
                    list_content_cultural.sortByDescending({selector(it)})

                    //새롭게 디스플레이한다.
                    recyclerView_content_cultural.adapter = ContentAdapter(list_content_cultural)
                    recyclerView_keyword_cultural.adapter = KeywordAdapter(list_keyword_cultural)

                    Toast.makeText(this, "Content Num: ${list_content_cultural.size}", Toast.LENGTH_LONG).show()
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
                                    list_c, "80", it1, map_accommodationCat.get(category_accommodation[i])!!.cat2,
                                    map_accommodationCat.get(category_accommodation[i])!!.cat3).execute().get()
                            }
                        }
                    }
                    list_keyword_accommodation = list_k
                    list_content_accommodation = list_c

                    list_content_accommodation.sortByDescending({selector(it)})

                    recyclerView_content_accommodation.adapter = ContentAdapter(list_content_accommodation)
                    recyclerView_keyword_accommodation.adapter = KeywordAdapter(list_keyword_accommodation)

                    Toast.makeText(this, "Content Num: ${list_content_accommodation.size}", Toast.LENGTH_LONG).show()
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
                                    list_c, "79", it1, map_shoppingCat.get(category_shopping[i])!!.cat2,
                                    map_shoppingCat.get(category_shopping[i])!!.cat3).execute().get()
                            }
                        }
                    }
                    list_keyword_shopping = list_k
                    list_content_shopping = list_c

                    list_content_shopping.sortByDescending({selector(it)})

                    recyclerView_content_shopping.adapter = ContentAdapter(list_content_shopping)
                    recyclerView_keyword_shopping.adapter = KeywordAdapter(list_keyword_shopping)

                    Toast.makeText(this, "Content Num: ${list_content_shopping.size}", Toast.LENGTH_LONG).show()
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
                                    list_c, "82", it1, map_cuisineCat.get(category_cuisine[i])!!.cat2,
                                    map_cuisineCat.get(category_cuisine[i])!!.cat3).execute().get()
                            }
                        }
                    }
                    list_keyword_cuisine = list_k
                    list_content_cuisine = list_c

                    list_content_cuisine.sortByDescending({selector(it)})

                    recyclerView_content_cuisine.adapter = ContentAdapter(list_content_cuisine)
                    recyclerView_keyword_cuisine.adapter = KeywordAdapter(list_keyword_cuisine)

                    Toast.makeText(this, "Content Num: ${list_content_cuisine.size}", Toast.LENGTH_LONG).show()
                }
            }
            //else ->
        }

        builder.setNegativeButton("Cancel", null)

        val dialog : AlertDialog = builder.create()

        dialog.show()
    }
}
