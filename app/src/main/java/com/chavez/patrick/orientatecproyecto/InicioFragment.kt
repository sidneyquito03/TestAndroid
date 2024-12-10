package com.chavez.patrick.orientatecproyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val images = listOf(
            R.drawable.beca18,
            R.drawable.becarepsol,
            R.drawable.becasbcp,
            R.drawable.becaslatam,
            R.drawable.becasferre
        )

        val recyclerViewCarousel: RecyclerView = view.findViewById(R.id.recycler_view_carousel)
        recyclerViewCarousel.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCarousel.adapter = CarouselAdapter(images)

        // Set up the text with colored "BECAS"
        val textBecas: TextView = view.findViewById(R.id.text_becas)
        val text = "Postula a sus diversas BECAS"
        val spannableString = SpannableString(text)
        val startIndex = text.indexOf("BECAS")
        val endIndex = startIndex + "BECAS".length

        if (startIndex >= 0 && endIndex <= text.length) {
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#2BB9D9")),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            Log.e("InicioFragment", "Index out of bounds for BECAS span")
        }

        textBecas.text = spannableString

        // URLs de YouTube
        val youtubeUrl1 = "https://youtu.be/0hTa8IHuHvM"
        val youtubeUrl2 = "https://youtu.be/cI9bq5557AE"
        val youtubeUrl3 = "https://youtu.be/5WrU1prLzCI"


        val imageTarjeta1 = view.findViewById<ImageView>(R.id.image_tarjeta_1)
        val imageTarjeta2 = view.findViewById<ImageView>(R.id.image_tarjeta_2)
        val imageTarjeta3 = view.findViewById<ImageView>(R.id.image_tarjeta_3)


        imageTarjeta1.setOnClickListener {
            openYouTube(youtubeUrl1)
        }

        imageTarjeta2.setOnClickListener {
            openYouTube(youtubeUrl2)
        }

        imageTarjeta3.setOnClickListener {
            openYouTube(youtubeUrl3)
        }
    }


    private fun openYouTube(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
