package com.mobiledev.mealbase

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.mobiledev.mealbase.HomeActivity.Companion.db
import com.mobiledev.mealbase.HomeActivity.Companion.mealDao
import com.mobiledev.mealbase.databinding.ActivitySearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class SearchActivity : AppCompatActivity() {


    // all the variables to hold retrieved information
    private lateinit var meal: JSONObject
    private lateinit var mealid: String
    private lateinit var mealname: String
    private lateinit var drink: String
    private lateinit var category: String
    private lateinit var area: String
    private lateinit var inst: String
    private lateinit var mealThumb: String
    private lateinit var tags: String
    private lateinit var youtube: String
    private lateinit var ingredient1: String
    private lateinit var ingredient2: String
    private lateinit var ingredient3: String
    private lateinit var ingredient4: String
    private lateinit var ingredient5: String
    private lateinit var ingredient6: String
    private lateinit var ingredient7: String
    private lateinit var ingredient8: String
    private lateinit var ingredient9: String
    private lateinit var ingredient10: String
    private lateinit var ingredient11: String
    private lateinit var ingredient12: String
    private lateinit var ingredient13: String
    private lateinit var ingredient14: String
    private lateinit var ingredient15: String
    private lateinit var ingredient16: String
    private lateinit var ingredient17: String
    private lateinit var ingredient18: String
    private lateinit var ingredient19: String
    private lateinit var ingredient20: String
    private lateinit var measure1: String
    private lateinit var measure2: String
    private lateinit var measure3: String
    private lateinit var measure4: String
    private lateinit var measure5: String
    private lateinit var measure6: String
    private lateinit var measure7: String
    private lateinit var measure8: String
    private lateinit var measure9: String
    private lateinit var measure10: String
    private lateinit var measure11: String
    private lateinit var measure12: String
    private lateinit var measure13: String
    private lateinit var measure14: String
    private lateinit var measure15: String
    private lateinit var measure16: String
    private lateinit var measure17: String
    private lateinit var measure18: String
    private lateinit var measure19: String
    private lateinit var measure20: String
    private lateinit var source: String
    private lateinit var imgSource: String
    private lateinit var creativeCommon: String
    private lateinit var dom: String

    private lateinit var mealDetails: java.lang.StringBuilder
    private lateinit var mealDetailsList: List<Meal>
    private lateinit var urlStr: String

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // cast to empty list to avoid null errors
        mealDetailsList = emptyList()

        // saved instance when application rotates
        if (savedInstanceState != null) {
            restoreInstanceState()
        }

        // initial button states
        binding.retrievemealbt.isEnabled = false
        binding.cardView2.isVisible = false
        binding.textView6.isVisible = false
        binding.savemealbt.isEnabled = false

        // initialising meal dao
        db = MealDataBase.getInstance(applicationContext)
        mealDao = db.MealDao()

        // ingredient string
        var ingredient: String = " "

        // button to save meal
        binding.savemealbt.setOnClickListener {
            runBlocking {
                launch {
                    saveMealDetailsToDatabase()
                    binding.savemealbt.isEnabled = false
                }
            }
        }

        /** reference link - https://stackoverflow.com/questions/6270484/how-to-remove-all-listeners-added-with-addtextchangedlistener
         * an example from above site assisted when add textchange lisener
         */

        // text change listener to track the changes in the edit text
        binding.searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // check if the text is null or empty
                if (s.isNullOrEmpty()) {

                    binding.savemealbt.isEnabled = false
                    binding.retrievemealbt.isEnabled = false

                } else {

                    // button to retrieve meals from api
                    binding.retrievemealbt.setOnClickListener {

                        CoroutineScope(Dispatchers.IO).launch {
                            var stringBuild = StringBuilder()
                            establishConnection(ingredient, stringBuild)
                            getJSON(stringBuild)
                            launch(Dispatchers.Main) {
                                binding.cardView2.isVisible = true
                                binding.textView6.isVisible = true
                                binding.savemealbt.isEnabled = true
                                binding.displayview.setText(mealDetails).toString()
                                binding.retrievemealbt.isEnabled = false
                            }
                        }
                    }
                    ingredient = binding.searchbar.text.toString()
                    binding.retrievemealbt.isEnabled = true
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /** LINK - https://www.javatpoint.com/kotlin-android-json-parsing-using-url
     * this site provided me an example on how to establish the connection with json
     */

    // suspend function to establish connection with api
    private suspend fun establishConnection(
        ingredient: String,
        stringBuild: java.lang.StringBuilder
    ) {
        try {
            urlStr = "https://www.themealdb.com/api/json/v1/1/search.php?s=$ingredient"
            val url = URL(urlStr)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            val buffer = BufferedReader(InputStreamReader(con.inputStream))
            var lines: String? = buffer.readLine()
            while (lines != null) {
                stringBuild.append(lines + "\n")
                lines = buffer.readLine()
            }
        } catch (e: org.json.JSONException) {
            Toast.makeText(this, "Please enter a valid ingredient", Toast.LENGTH_SHORT).show()
        }
    }

    // private suspend function to extract from JSON format and insert into Meal data class
    private suspend fun getJSON(stringBuild: java.lang.StringBuilder) {
        try {

            val getJson = JSONObject(stringBuild.toString())
            mealDetails = java.lang.StringBuilder()
            val jsonArray = getJson.getJSONArray("meals")
            mealDetailsList = mutableListOf<Meal>()

            // for loop to interate over jsonArray
            for (i in 0 until jsonArray.length()) {

                meal = jsonArray[i] as JSONObject
                mealid = meal.optString("idMeal")
                mealname = meal.optString("strMeal")
                drink = meal.optString("strDrinkAlternate")
                category = meal.optString("strCategory")
                area = meal.getString("strArea")
                inst = meal.optString("strInstructions")
                mealThumb = meal.optString("strMealThumb")
                tags = meal.optString("strTags")
                youtube = meal.optString("strYoutube")
                ingredient1 = meal.optString("strIngredient1")
                ingredient2 = meal.optString("strIngredient2")
                ingredient3 = meal.optString("strIngredient3")
                ingredient4 = meal.optString("strIngredient4")
                ingredient5 = meal.optString("strIngredient5")
                ingredient6 = meal.optString("strIngredient6")
                ingredient7 = meal.optString("strIngredient7")
                ingredient8 = meal.optString("strIngredient8")
                ingredient9 = meal.optString("strIngredient9")
                ingredient10 = meal.optString("strIngredient10")
                ingredient11 = meal.optString("strIngredient11")
                ingredient12 = meal.optString("strIngredient12")
                ingredient13 = meal.optString("strIngredient13")
                ingredient14 = meal.optString("strIngredient14")
                ingredient15 = meal.optString("strIngredient15")
                ingredient16 = meal.optString("strIngredient16")
                ingredient17 = meal.optString("strIngredient17")
                ingredient18 = meal.optString("strIngredient18")
                ingredient19 = meal.optString("strIngredient19")
                ingredient20 = meal.optString("strIngredient20")
                measure1 = meal.optString("strMeasure1")
                measure2 = meal.optString("strMeasure2")
                measure3 = meal.optString("strMeasure3")
                measure4 = meal.optString("strMeasure4")
                measure5 = meal.optString("strMeasure5")
                measure6 = meal.optString("strMeasure6")
                measure7 = meal.optString("strMeasure7")
                measure8 = meal.optString("strMeasure8")
                measure9 = meal.optString("strMeasure9")
                measure10 = meal.optString("strMeasure10")
                measure11 = meal.optString("strMeasure11")
                measure12 = meal.optString("strMeasure12")
                measure13 = meal.optString("strMeasure13")
                measure14 = meal.optString("strMeasure14")
                measure15 = meal.optString("strMeasure15")
                measure16 = meal.optString("strMeasure16")
                measure17 = meal.optString("strMeasure17")
                measure18 = meal.optString("strMeasure18")
                measure19 = meal.optString("strMeasure19")
                measure20 = meal.optString("strMeasure20")
                source = meal.optString("strSource")
                imgSource = meal.optString("strImageSource")
                creativeCommon = meal.optString("strCreativeCommonsConfirmed")
                dom = meal.optString("dateModified")

                val mealFromApi = Meal(
                    Integer.parseInt(mealid),
                    mealname,
                    drink,
                    category,
                    area,
                    inst,
                    mealThumb,
                    tags,
                    youtube,
                    ingredient1,
                    ingredient2,
                    ingredient3,
                    ingredient4,
                    ingredient5,
                    ingredient6,
                    ingredient7,
                    ingredient8,
                    ingredient9,
                    ingredient10,
                    ingredient11,
                    ingredient12,
                    ingredient13,
                    ingredient14,
                    ingredient15,
                    ingredient16,
                    ingredient17,
                    ingredient18,
                    ingredient19,
                    ingredient20,
                    measure1,
                    measure2,
                    measure3,
                    measure4,
                    measure5,
                    measure6,
                    measure7,
                    measure8,
                    measure9,
                    measure10,
                    measure11,
                    measure12,
                    measure13,
                    measure14,
                    measure15,
                    measure16,
                    measure17,
                    measure18,
                    measure19,
                    measure20,
                    source,
                    imgSource,
                    creativeCommon,
                    dom
                )

                // adding the extracted meal to mutable list
                (mealDetailsList as MutableList<Meal>).add(mealFromApi)

                // appending to the string builder
                mealDetails.append(
                    "${i + 1})\"Meal ID: $mealid\n Meal: $mealname\n Drink Alternate: $drink\n Category: $category\n Area: $area\n Instructions: $inst\n  Meal Thumb: $mealThumb\n Tags: $tags\n Youtube: $youtube\n" +
                            " Ingredient1: $ingredient1\n Ingredient2: $ingredient2\n Ingredient3: $ingredient3\n Ingredient4: $ingredient4\n Ingredient5: $ingredient5\n Ingredient6: $ingredient6\n Ingredient7: $ingredient7\n Ingredient8: $ingredient8\n " +
                            "Ingredient9: $ingredient9\n Ingredient10: $ingredient10\n Ingredient11: $ingredient11\n Ingredient12: $ingredient12\n Ingredient13: $ingredient13\n Ingredient14: $ingredient14\n Ingredient15: $ingredient15\n Ingredient16: $ingredient16\n Ingredient17: $ingredient17\n" +
                            " Ingredient18: $ingredient18\n Ingredient19: $ingredient19\n Ingredient20: $ingredient20\n Measure1: $measure1 \n Measure2: $measure2\n Measure3: $measure3\n Measure4: $measure4\n Measure5: $measure5\n Measure6: $measure6\n Measure7: $measure7\n Measure8: $measure8\n Measure9: $measure9\n Measure10: $measure10\n Measure11: $measure11\n " +
                            "Measure12: $measure12\n Measure13: $measure13\n Measure14: $measure14\n Measure15: $measure15\n Measure16: $measure16\n Measure17: $measure17\n Measure18: $measure18\n Measure19: $measure19\n Measure20: $measure20\n Source: $source\n ImageSource: $imgSource\n CreativeCommonsConfirmed : $creativeCommon\n DateOfModified: $dom \""
                )
                mealDetails.append("\n\n")
            }
        } catch (e: org.json.JSONException) {
            runOnUiThread {
                Toast.makeText(this, "Please enter a valid ingredient !!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // function to save meal to database
    private suspend fun saveMealDetailsToDatabase() {
        // Use a loop to insert all meal details to the database
        for (mealDetails in mealDetailsList) {
            db.MealDao().insertMeal(mealDetails)
        }
    }

    // function to save state before screen orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("text", binding.displayview.text)
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("retrieveBt", binding.retrievemealbt.isEnabled)
        editor.putBoolean("saveBt", binding.savemealbt.isEnabled)
        editor.putBoolean("cardView", binding.cardView2.isVisible)
        editor.putBoolean("detailstext", binding.textView6.isVisible)
        editor.apply()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreInstanceState()
        binding.displayview.text = savedInstanceState.getCharSequence("text")
    }

    // onresume function of shared preferance
    override fun onResume() {
        super.onResume()
        restoreInstanceState()
    }

    // function to restore instanceState
    private fun restoreInstanceState() {
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.retrievemealbt.isEnabled = sharedPrefs.getBoolean("retrieveBt", false)
        binding.savemealbt.isEnabled = sharedPrefs.getBoolean("saveBt", false)
        binding.cardView2.isVisible = sharedPrefs.getBoolean("cardView", false)
        binding.textView6.isVisible = sharedPrefs.getBoolean("detailstext", false)
    }
}

