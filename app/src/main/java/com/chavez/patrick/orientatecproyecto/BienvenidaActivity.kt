package com.chavez.patrick.orientatecproyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class BienvenidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permitir que el contenido se extienda hasta los bordes de la pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_bienvenida)

        // Aplicar márgenes para el sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el botón y establecer el click listener
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener { goToRegistroActivity(it) }
    }

    // Método para navegar a RegistroActivity
    fun goToRegistroActivity(view: View) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }
}
