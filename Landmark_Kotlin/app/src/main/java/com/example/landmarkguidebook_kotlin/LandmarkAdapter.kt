package com.example.landmarkguidebook_kotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landmarkguidebook_kotlin.databinding.RecylerRowBinding

class LandmarkAdapter(val landmarkList:ArrayList<Landmark>):RecyclerView.Adapter<LandmarkAdapter.LandmarkHolder>() {

    class LandmarkHolder(val binding: RecylerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkHolder {
        //LandmarkHolder olan ViewHolader ilk oluşturulduğunda yapılacak işlemleri burada tanımlıyoruz
        //recyler_row layout'unu burada bağalantı vererek bağlama yapacaz , View Binding kullanarak
        val binding = RecylerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LandmarkHolder(binding)
    }

    override fun onBindViewHolder(holder: LandmarkHolder, position: Int) {
        //bağlantı dan sonraki işlemler burada tanımlanıyor
        // mesela hangi textView'da hangi veri gösterilecek olan işler burada tanımlanır
        holder.binding.recyclerViewTextView.text = landmarkList.get(position).name

        holder.itemView.setOnClickListener(){
            val intent=Intent(holder.itemView.context,DetailsActivity::class.java)
            intent.putExtra("landmark",landmarkList.get(position))
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        // sınıfını belirlediğimiz veri kaç adet gelecek burada belirtmek gerek
        // profesyonel projelerde genellikle sayısı bilinez, veri geldiğinde sayısını bize veren değişkeni burada kulllanırız.
        return landmarkList.size
    }
}