package com.example.catchthekenny_kotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import com.example.catchthekenny_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.Runnable
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var score=0
    private var imageArray=ArrayList<ImageView>()
    private var runnable:Runnable=Runnable{}
    private var handler=Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)

        hideImage()

        object :CountDownTimer(15500,1000){

            override fun onTick(millisUntilFinished: Long) {
                binding.timeText.text="Time : ${millisUntilFinished/1000}"

            }

            override fun onFinish() {
                binding.timeText.text="Time Finished!"
                handler.removeCallbacks(runnable)

                //Oyun bittiğinde kennleri gizler
                for (image in imageArray){
                    image.visibility=View.INVISIBLE
                }

                // bir alert dialog versek oyun daha profesyonel olur
                var alert= AlertDialog.Builder(this@MainActivity)
                alert.setTitle("GAME OVER!")
                alert.setMessage("Restart The Game ?")

                alert.setPositiveButton("Yes",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        // Restart
                        var intentFromMain=intent // MainActivity tekrar başlattı
                        finish()  // yeni Activity başlamadan bu Activity kapattı ekran karmaşası oluşmasın OnDestroy çalışacak
                        println("finishten sonra da kalan komutlar çalıştı")
                        startActivity(intentFromMain)
                    }

                })

                alert.setNegativeButton("No",object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(this@MainActivity,"Game Over!",Toast.LENGTH_LONG).show()
                    }
                })
                alert.show()


            }

        }.start()

    }



    fun hideImage(){
        //binding.imageView1.visibility=View.INVISIBLE
        //View.GONE Fotoğrafı Layouttan komple siler yerine diğer görsel kayabilir
        // View.INVISIBLE , VIEW.VISIBLE


        runnable=object:Runnable{
            override fun run() {

                for (image in imageArray){
                    image.visibility=View.INVISIBLE
                }
                var random=Random()
                var randomIndex=random.nextInt(9)   // 9 dahil degil
                imageArray[randomIndex].visibility=View.VISIBLE

                handler.postDelayed(runnable,500)
            }

        }
        handler.post(runnable)
    }

    fun increaseScore(view: View){
        score+=1
        binding.scoreText.text="Score : ${score}"

    }
}