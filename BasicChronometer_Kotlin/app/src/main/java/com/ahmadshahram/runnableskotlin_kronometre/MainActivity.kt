package com.ahmadshahram.runnableskotlin_kronometre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.ahmadshahram.runnableskotlin_kronometre.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var number=0
    var runnable:Runnable=Runnable{}  /*runnable isminda bir nesnesyi Runnable sinifi ait olusturduk , ancak =Runnable{} yazdığımız için nesne
    boş ve anonim olarak önden tanımlandı    */
    var handler:Handler=Handler(Looper.getMainLooper()) //Hnadler burada Runnable çalıştıracak bir yardımcı basit yapı
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


    fun start(view: View){
        binding.stopButton.isEnabled=true
        number=0
        runnable=   object:Runnable{
            override fun run() {
                number+=1
                binding.textView.text="Time : ${number}"
                handler.postDelayed(runnable,1000)
            }

        }
        handler.post(runnable)

        binding.startButton.isEnabled=false  /*START butunu basıldığında tekrar basmamak için deactive ediyor ,
         çünkü ilk STOP butununa basmak gerek işlemi durdurmak için */
    }

    fun stop(view: View){
        binding.startButton.isEnabled=true  // deactive ettğimi START butunun Active ettik
        number=0
        binding.textView.text="Time : 0"
        handler.removeCallbacks(runnable)  // runnable isimli Runnable sınıfına ait Thread'i durdurur ve siliyor
        binding.stopButton.isEnabled=false


    }
}