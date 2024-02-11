package com.example.landmarkguidebook_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landmarkguidebook_kotlin.databinding.ActivityDetailsBinding
import com.example.landmarkguidebook_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var landmarkList:ArrayList<Landmark>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //initialize - bos parantez constructor ile bos olarak diziyi tanimladik
        landmarkList= ArrayList<Landmark>()

        //Data
        val pisa=Landmark("Pisa","Italy",R.drawable.pisa)
        val colosseum=Landmark("Colosseum","Italy",R.drawable.colosseum)
        val eiffelTower=Landmark("Eiffel Tower","France",R.drawable.eiffeltower)
        val londonBridge=Landmark("London Bridge","United Kingdom",R.drawable.londongbridge)

        landmarkList.add(pisa)
        landmarkList.add(colosseum)
        landmarkList.add(eiffelTower)
        landmarkList.add(londonBridge)

        binding.recylerView.layoutManager=LinearLayoutManager(this)  // liste elemanlarini alt alta siralari
        //Grid Layout kullanarak liste elemanlarini yan yana siralayabiliriz
        val landmarkAdapter=LandmarkAdapter(landmarkList)
        binding.recylerView.adapter=landmarkAdapter



        //ListView - Not Professional Way
        //Adapter - Layout ile Data connect

        /* ListView
        //Mapping
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,landmarkList.map { it->it.name })
                        //android haizr list gosterim layout'u
        binding.listView.adapter=adapter

        //position - listedeki kaçıncı yazıya tıklandığını veriyor
        binding.listView.onItemClickListener=AdapterView.OnItemClickListener { *//*input*//* parent, view, position, id ->*//*output*//*
        val intent = Intent(this@MainActivity,DetailsActivity::class.java)
            intent.putExtra("landmark",landmarkList.get(position))
            startActivity(intent)
        }*/

    }
}