package com.mobiledev.mealbase

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity of the table
@Entity(tableName = "meal_table")
data  class Meal(

    // Columns on the table
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "meal_name") val mealName:String?,
    @ColumnInfo(name = "drink_alternate") val drinkAlternate:String?,
    @ColumnInfo(name = "category") val category:String?,
    @ColumnInfo(name = "area") val area:String?,
    @ColumnInfo(name = "instructions") val instructions:String?,
    @ColumnInfo(name = "meal_thumb") val mealThumb:String?,
    @ColumnInfo(name = "tags") val tags:String?,
    @ColumnInfo(name = "youtube") val youtube:String?,
    @ColumnInfo(name = "ingredient1") val ingredient1:String?,
    @ColumnInfo(name = "ingredient2") val ingredient2:String?,
    @ColumnInfo(name = "ingredient3") val ingredient3:String?,
    @ColumnInfo(name = "ingredient4") val ingredient4:String?,
    @ColumnInfo(name = "ingredient5") val ingredient5:String?,
    @ColumnInfo(name = "ingredient6") val ingredient6:String?,
    @ColumnInfo(name = "ingredient7") val ingredient7:String?,
    @ColumnInfo(name = "ingredient8") val ingredient8:String?,
    @ColumnInfo(name = "ingredient9") val ingredient9:String?,
    @ColumnInfo(name = "ingredient10") val ingredient10:String?,
    @ColumnInfo(name = "ingredient11") val ingredient11:String?,
    @ColumnInfo(name = "ingredient12") val ingredient12:String?,
    @ColumnInfo(name = "ingredient13") val ingredient13:String?,
    @ColumnInfo(name = "ingredient14") val ingredient14:String?,
    @ColumnInfo(name = "ingredient15") val ingredient15:String?,
    @ColumnInfo(name = "ingredient16") val ingredient16:String?,
    @ColumnInfo(name = "ingredient17") val ingredient17:String?,
    @ColumnInfo(name = "ingredient18") val ingredient18:String?,
    @ColumnInfo(name = "ingredient19") val ingredient19:String?,
    @ColumnInfo(name = "ingredient20") val ingredient20:String?,
    @ColumnInfo(name = "measure1") val measure1:String?,
    @ColumnInfo(name = "measure2") val measure2:String?,
    @ColumnInfo(name = "measure3") val measure3:String?,
    @ColumnInfo(name = "measure4") val measure4:String?,
    @ColumnInfo(name = "measure5") val measure5:String?,
    @ColumnInfo(name = "measure6") val measure6:String?,
    @ColumnInfo(name = "measure7") val measure7:String?,
    @ColumnInfo(name = "measure8") val measure8:String?,
    @ColumnInfo(name = "measure9") val measure9:String?,
    @ColumnInfo(name = "measure10") val measure10:String?,
    @ColumnInfo(name = "measure11") val measure11:String?,
    @ColumnInfo(name = "measure12") val measure12:String?,
    @ColumnInfo(name = "measure13") val measure13:String?,
    @ColumnInfo(name = "measure14") val measure14:String?,
    @ColumnInfo(name = "measure15") val measure15:String?,
    @ColumnInfo(name = "measure16") val measure16:String?,
    @ColumnInfo(name = "measure17") val measure17:String?,
    @ColumnInfo(name = "measure18") val measure18:String?,
    @ColumnInfo(name = "measure19") val measure19:String?,
    @ColumnInfo(name = "measure20") val measure20:String?,
    @ColumnInfo(name = "source") val source:String?,
    @ColumnInfo(name = "imageSource") val imageSource:String?,
    @ColumnInfo(name = "creativeCommonsConfirmed") val creativeCommons:String?,
    @ColumnInfo(name = "dateModified") val dateModified:String?
    )
