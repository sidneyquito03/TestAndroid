package com.chavez.patrick.orientatecproyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class InicioActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menuButton: ImageButton
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Configurar DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_header) // Asegúrate de que este ID sea correcto

        // Configurar el botón del menú
        menuButton = findViewById(R.id.menu)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configurar el toggle de la barra de acción con el DrawerLayout
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar navegación para el menú lateral (drawer)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.preguntas -> {
                    val intent = Intent(this, PreguntasFrecuentesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Añade animación
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.ajustes -> {
                    startActivity(Intent(this, AjustesActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
            }
        }

        // Configurar el BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Any = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.ubicacion -> UbicacionFragment()
                R.id.inicio -> InicioFragment()
                R.id.test -> TestFragment()
                R.id.chatbox -> ChatboxFragment()
                else -> HomeFragment()
            }
            // Reemplaza el contenido de fragment_home con el fragmento seleccionado
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_home, selectedFragment as Fragment)
                .commit()
            true
        }

        // Mostrar el fragmento inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_home, HomeFragment())
                .commit()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
