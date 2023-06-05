package com.mobiledev.mealbase

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.mobiledev.mealbase.HomeActivity.Companion.db
import com.mobiledev.mealbase.HomeActivity.Companion.mealDao
import com.mobiledev.mealbase.databinding.ActivityAdvancedSearchBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class AdvancedSearchActivity : AppCompatActivity() {

    // late initializing the variables
    private lateinit var binding: ActivityAdvancedSearchBinding
    private lateinit var txt: String
    private lateinit var searchResult: List<Meal>
    private lateinit var thumbNailImage: ImageView
    private lateinit var imageContainer: LinearLayout
    private lateinit var mealNameDisplay: TextView
    private lateinit var mealThumbNails: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {

        }

        // setting defaults to false
        binding.cardView3.isVisible = false
        binding.charsearchbtdb.isEnabled = false
        binding.charsearchbtapi.isEnabled = false
        binding.meadetails2.isVisible = false

        // initialising the meal dao
        db = MealDataBase.getInstance(applicationContext)
        mealDao = db.MealDao()

        // textChange listener to track text changes in edit text
        binding.charsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {

                    binding.charsearchbtdb.isEnabled = false
                    binding.charsearchbtapi.isEnabled = false
                    binding.meadetails2.isVisible = false
                    imageContainer.removeAllViews()

                } else {

                    // search button onclick function
                    binding.charsearchbtdb.setOnClickListener {
                        imageContainer = binding.imagecontainerlayout
                        binding.cardView3.isVisible = true
                        binding.meadetails2.isVisible = true
                        imageContainer.removeAllViews()

                        // IO tread
                        CoroutineScope(Dispatchers.IO).launch {

                            // string to hold the editText
                            txt = binding.charsearch.text.toString()
                            txt.lowercase()

                            // passing the string to the mealDao
                            searchResult = mealDao.searchMeals(txt)

                            // for loop to iterate through table results
                            for (meal in searchResult) {

                                val mealNames = meal.mealName
                                val thumbNail = meal.mealThumb

                                // main thread
                                launch(Dispatchers.Main) {

                                    mealThumbNails = ArrayList<String>()
                                    mealThumbNails.add(thumbNail.toString())

                                    // creating text views for each meal name
                                    mealNameDisplay = TextView(this@AdvancedSearchActivity)
                                    mealNameDisplay.text = mealNames
                                    mealNameDisplay.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    mealNameDisplay.gravity = Gravity.CENTER_HORIZONTAL
                                    mealNameDisplay.setPadding(5, 10, 5, 10)

                                    // creating imageViews for each thumbnail image
                                    thumbNailImage = ImageView(this@AdvancedSearchActivity)
                                    thumbNailImage.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    Picasso.get().load(thumbNail).into(thumbNailImage)
                                    thumbNailImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                                    thumbNailImage.setPadding(0, 20, 0, 20)

                                    // updating the linear layout
                                    imageContainer.orientation = LinearLayout.VERTICAL
                                    imageContainer.addView(mealNameDisplay)
                                    imageContainer.addView(thumbNailImage)

                                }
                            }
                        }
                    }
                    // button to search from api
                    binding.charsearchbtapi.setOnClickListener {

                        imageContainer.removeAllViews()
                        binding.cardView3.isVisible = true
                        binding.meadetails2.isVisible = true
                        txt = binding.charsearch.text.toString()
                        txt.lowercase()
                        CoroutineScope(Dispatchers.IO).launch {
                            searchMealsByName(txt)
                        }
                    }
                    binding.charsearchbtdb.isEnabled = true
                    binding.charsearchbtapi.isEnabled = true

                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // function to search meals by name from api
    suspend fun searchMealsByName(searchString: String): List<String> {
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$searchString"
        val con = withContext(Dispatchers.IO) {
            URL(url).openConnection()
        } as HttpURLConnection
        withContext(Dispatchers.IO) {
            con.connect()
        }
        val response = con.inputStream.bufferedReader().readText()
        val json = JSONObject(response)
        val meals = json.getJSONArray("meals")
        val mealNames = mutableListOf<String>()
        for (i in 0 until meals.length()) {
            val meal = meals.getJSONObject(i)
            mealNames.add(meal.getString("strMeal"))

        }

        // running main tread to display the views
        runOnUiThread {
            for (meal in mealNames) {
                mealNameDisplay = TextView(this@AdvancedSearchActivity)
                mealNameDisplay.text = meal
                mealNameDisplay.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                imageContainer = binding.imagecontainerlayout

                mealNameDisplay.gravity = Gravity.START
                mealNameDisplay.setPadding(20, 20, 20, 20)
                imageContainer.addView(mealNameDisplay)
            }
        }
        return mealNames
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreInstanceState()

    }

    // onresume function of shared preferance
    override fun onResume() {
        super.onResume()
        restoreInstanceState()
    }

    // function to restore instanceState
    private fun restoreInstanceState() {
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.charsearchbtdb.isEnabled = sharedPrefs.getBoolean("charsearchbtdb", false)
        binding.charsearchbtapi.isEnabled = sharedPrefs.getBoolean("chatsearchbtapi", false)
        binding.cardView3.isVisible = sharedPrefs.getBoolean("cardView3", false)
        binding.meadetails2.isVisible = sharedPrefs.getBoolean("mealdetails2", false)
    }


}