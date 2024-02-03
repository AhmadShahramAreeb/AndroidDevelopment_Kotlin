package com.ahmadshahram.countdowntimer_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.ahmadshahram.countdowntimer_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*CountDownTime bir abstract old. için nesnesini oluşturarak kullanamyız dolayısıyla anonim bir nesneyi object keyword ile oluşturarak abstract sınıfın içinden metodları implemente edecek bir ortam oluşturmuş olduk*/
                          /*toplam süre*/  /*azalan adım süre ms cinsinden*/
        object :CountDownTimer(20000 ,1000){
            override fun onTick(millisUntilFinished: Long) {
                // sayaç her geri sayım yaptığınd bu kod buluğu çalışır
                binding.textView.text="Time Left : ${millisUntilFinished/1000}"
            }

            override fun onFinish() {
                // geri sayım durduğunda bu kod buluğu çalışır
                binding.textView.text="Time Left : "
            }

        }.start()

    }

}