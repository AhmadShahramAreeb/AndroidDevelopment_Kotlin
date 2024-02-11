package com.example.landmarkguidebook_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.landmarkguidebook_kotlin.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intentFromMain=intent
        val receivedLanmark=intent.getSerializableExtra("landmark") as Landmark
        // as Landmark diyerek gelen veriyi Landmark sınıfına ait nesneye cast ettim,
        // çünkü serileşmiş nesnenin alanlarına ve metodlarına erişmek mümkün değil
        binding.countryNameText.setText(receivedLanmark.country)
        binding.landmarkNameText.setText(receivedLanmark.name)
        binding.landmarkImageView.setImageResource(receivedLanmark.image)
    }
}