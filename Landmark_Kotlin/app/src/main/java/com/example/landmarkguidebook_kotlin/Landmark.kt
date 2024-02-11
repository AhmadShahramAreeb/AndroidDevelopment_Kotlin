package com.example.landmarkguidebook_kotlin

import java.io.Serializable

/**iki ekran arasında intent kullanarak putExtra metoduyla kendi oluşturduğum sınıf nesnelerini
   göndermek için sınıfım Serleşmiş olmalı, bunu Serializable arayüzünü import ederek yapabiliriz */
class Landmark(name:String,country:String,image:Int) : Serializable {
    /*  image degisken'in veri tipini Int old. nedeni ilgili her resmin id'si drawble
      klasorunde Int olarak verilir ve bu sayi ilgili resime denk geli*/

    //Encapsulation
    var name:String=name
        private set
        get

    var country:String=country
        private set
        get

    var image:Int=image
        private set
        get
}