package com.example.travel_book_kotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//Room Entity parcasi , database icin hemde place nesnesini icin lazim olunca kullanacaz

@Entity //(tableName = )
//data sinif yapmak zorunlu degil
class Place (
            @ColumnInfo(name = "name")
            var name:String,

            @ColumnInfo(name = "latitude")
            var latitude:Double,

            @ColumnInfo(name = "longitude")
            var longitude:Double
    ) : Serializable {
            @PrimaryKey(autoGenerate = true)
            var id = 0 //
}