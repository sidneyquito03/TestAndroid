package com.chavez.patrick.orientatecproyecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chavez.patrick.orientatecproyecto.databinding.ActivityPreguntasFrecuentesBinding

class PreguntasFrecuentesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreguntasFrecuentesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreguntasFrecuentesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el botón de regreso
        binding.backArrow.setOnClickListener {
            finish() // Cierra esta actividad y regresa al menú anterior
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // Añade animación al volver
    }
}
