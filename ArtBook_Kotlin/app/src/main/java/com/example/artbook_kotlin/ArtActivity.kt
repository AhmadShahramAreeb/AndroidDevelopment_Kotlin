package com.example.artbook_kotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.artbook_kotlin.databinding.ActivityArtBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

/*  İzin istemek dışında da Manifest'te aynı izinler için kullanım uyarı verme ayarlarını da yapmamız gerekir   */
class ArtActivity : AppCompatActivity() {
    //Declare - ön tanımlama işlemelri
    private lateinit var binding:ActivityArtBinding
    private lateinit var activityResultLaunher:ActivityResultLauncher<Intent> /*ActivityResultLauncher gerçek bir Activity old. için Intent tipi ile tanımlanır*/
    private lateinit var permissionLauncher:ActivityResultLauncher<String> /* izinler String tipinde veriler olduğu için ön tanımlamaı String olarak yaptık*/
    private var selectedBitmap: Bitmap?=null
    private lateinit var database :SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

        database=this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)

        /*ActivityResultLauncher iki amaç için kullanılır 1. izin almak , 2. Gallery veya Hafıza ya gidip veri seçmek için oluşturulan Sayfalar için
        * her iki durum için de başta OnCreate Fonk. dan önce ActivityResultLauncher arayüzünü tanımlayacağız ardından bu işlemlerin tanımlamasını yapacağız.
        * ardından ilgili ön tanımlama yaptığımız işlemlerin kodunu OnCreate atlına yazacağız.
        * bu kod genel kullanım ve sık kullanıldığı için ayrı bir fonk. şeklinde yazılması daha iyidir */
        registerLauncher()

        val intent=intent
        val info = intent.getStringExtra("info")
        if (info.equals("new")){
            binding.artNameText.setText("")
            binding.artistNameText.setText("")
            binding.yearText.setText("")
            binding.saveButton.visibility=View.VISIBLE
            binding.selectArtImageView.setImageResource(R.drawable.select_image)
        } else {
            binding.saveButton.visibility=View.INVISIBLE
            val selectedId=intent.getIntExtra("id",0)

            val cursor = database.rawQuery("SELECT * FROM arts WHERE id = ? ",arrayOf(selectedId.toString()))
            /* selected arguman her zaman string dizi şeklinde veri istiyor geçmişte lazım olmadığı için null diyerek geçiyorduk */
            val artNameIx=cursor.getColumnIndex("artname")
            val artistNameIx=cursor.getColumnIndex("artistname")
            val yearIx=cursor.getColumnIndex("year")
            val imageIx=cursor.getColumnIndex("image")

            while (cursor.moveToNext()){
                binding.artNameText.setText(cursor.getString(artNameIx))
                binding.artistNameText.setText(cursor.getString(artNameIx))
                binding.yearText.setText(cursor.getString(yearIx))

                val byteArray=cursor.getBlob(imageIx)
                val bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                binding.selectArtImageView.setImageBitmap(bitmap)
            }
            cursor.close()
        }

    }

    fun saveButtonClicked(view:View){
        val artName=binding.artNameText.text.toString()
        val artistName=binding.artistNameText.text.toString()
        val year=binding.yearText.text.toString()


        if(selectedBitmap != null){
            val smallBitmap=makeSmallerBitmap(selectedBitmap!!,300 /*max 500 */)

            val outputStream=ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50 /* 1 ile 100 arasi*/,outputStream)
            val byteArray=outputStream.toByteArray()

            try {
                val database=this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS arts( id INTEGER PRIMARY KEY,  artname VARCHAR," +
                        " artistname VARCHAR ,year VARCHAR,image BLOB)")

                //çalışır ama mantıklı değil database.execSQL("INSERT INTO arts (artname , artistname, year, image) VALUES (${artName},${artistName},${year},${byteArray})")
                // Başka yöntem kullanalım
                val sqlString = "INSERT INTO arts (artname, artistname, year, image) VALUES (?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,artName)
                statement.bindString(2,artistName)
                statement.bindString(3,year)
                statement.bindBlob(4,byteArray)
                statement.execute()
            } catch (e:Exception){
                e.printStackTrace()
            }
            val intent = Intent(this@ArtActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // bundan önceki tüm sayfaları kapat
            startActivity(intent)
        }

    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize: Int) : Bitmap{
        var width=image.width
        var height=image.height

        val bitmapRatio:Double=width.toDouble()/height.toDouble()

        if (bitmapRatio>1){
            //landscape image
            width=maximumSize
            val scaledHeight=width/bitmapRatio
            height=scaledHeight.toInt()

        } else {
            // portrait or square image
            height=maximumSize
            val scaledWidth=bitmapRatio*height
            width=scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun selectImage(view:View){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Android 33+ -> READ_MEDIA_IMAGES
            //Gallery ye gitmek için izin almamız lazım , ilk daha önce izin verilmiş mi kontrol ediyoruz


                if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES)!=PackageManager.PERMISSION_GRANTED){
                    //checkSelfPermission şartı PackageManager sınfına ait değer döndürecek

                    //rational - izin vermeyince kullanıcı gerekli açıklamayı yapar ve hangi işlem için izin istemenin mantığını açıklıyor
                    /*android OS kendisi karar vererek isterse rational açıklama yaparak veya rational açıklama yapmadan tekrar izin ister ancak geliştirici iki durum için de
                     kodu yazmalı*/
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES /* Hangi izin için açıklama yapılsın */)){
                        // rational açıklama yap
                        Snackbar.make(view,"Permission for Uploading Images is Necessary",Snackbar.LENGTH_INDEFINITE).setAction("Allow Access ?",
                            View.OnClickListener { permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) }).show()

                    } else  { //Direct requet permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                    }

                } else {
                    val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI /*iç hafızadan ilgili fotoğrafı verisini URI adressini seçer*/)
                    activityResultLaunher.launch(intentToGallery)

                }
        } else{
            //Android 32- -> Read_EXTERNAL_STORAGE
            //Gallery ye gitmek için izin almamız lazım , ilk daha önce izin verilmiş mi kontrol ediyoruz


                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        //checkSelfPermission şartı PackageManager sınfına ait değer döndürecek

                        //rational - izin vermeyince kullanıcı gerekli açıklamayı yapar ve hangi işlem için izin istemenin mantığını açıklıyor
                        /*android OS kendisi karar vererek isterse rational açıklama yaparak veya rational açıklama yapmadan tekrar izin ister ancak geliştirici iki durum için de
                         kodu yazmalı*/
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE /* Hangi izin için açıklama yapılsın */)){
                            // rational açıklama yap
                            Snackbar.make(view,"Permission for Uploading Images is Necessary",Snackbar.LENGTH_INDEFINITE).setAction("Allow Access ?",
                                View.OnClickListener { permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }).show()

                        } else  { //Direct requet permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }

                    } else {
                        val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI /*iç hafızadan ilgili fotoğrafı verisini URI adressini seçer*/)
                        activityResultLaunher.launch(intentToGallery)

                    }
        }

    }


    // ResultLauncher initialization işlemine register deniliyor
    private fun registerLauncher(){
        activityResultLaunher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode== RESULT_OK){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    val imageData=intentFromResult.data
                    //binding.selectArtImageView.setImageURI(imageData)
                     if(imageData!=null){
                       try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                println("Buradayim")
                                val source = ImageDecoder.createSource(this@ArtActivity.contentResolver,imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.selectArtImageView.setImageBitmap(selectedBitmap)
                            }
                           else{
                               selectedBitmap=MediaStore.Images.Media.getBitmap(this@ArtActivity.contentResolver,imageData)
                                binding.selectArtImageView.setImageBitmap(selectedBitmap)
                            }

                       } catch (e: Exception) {
                           e.printStackTrace()
                       }


                     }

            }

        }

      }
        permissionLauncher =    registerForActivityResult(ActivityResultContracts.RequestPermission())  { result->
            if(result){
                //permission granted
                val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI /*iç hafızadan ilgili fotoğrafı verisini URI adressini seçer*/)
                activityResultLaunher.launch(intentToGallery)
            } else{
                //Permission denied
                Toast.makeText(this@ArtActivity,"Permission Required to Upload Pictures",Toast.LENGTH_LONG).show()
            }

        }



    }
}