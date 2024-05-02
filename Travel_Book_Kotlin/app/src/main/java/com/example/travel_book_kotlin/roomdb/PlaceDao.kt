package com.example.travel_book_kotlin.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.travel_book_kotlin.model.Place
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao  //Data Access Objec - Veriye Erisim Objesi , Room Dao Parcasi
interface PlaceDao {

   /* @Query("Select * from place where id = :id") fonk. alinan parametre syntaxi
    fun  getAll(id : String):List<Place>
    */

    @Query("SELECT * FROM Place")
    fun getAll() : Flowable<List<Place>>

    @Insert
    fun insert(place:Place) : Completable

    @Delete
    fun delete(place: Place) : Completable

}