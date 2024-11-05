package com.chavez.patrick.orientatecproyecto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var editImageButton: ImageButton
    private lateinit var nombreCompletoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var logoutButton: Button
    private lateinit var backArrow: ImageView

    private lateinit var sharedPreferences: SharedPreferences
    private var profileImageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializa los elementos
        profileImageView = findViewById(R.id.profileImage)
        editImageButton = findViewById(R.id.changeProfileImageButton)
        nombreCompletoEditText = findViewById(R.id.nombre_completo)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.passwordEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        logoutButton = findViewById(R.id.logoutButton)
        backArrow = findViewById(R.id.backArrow)

        // Obtener valores del Intent
        val nombreCompleto = intent.getStringExtra("nombre_completo")
        val email = intent.getStringExtra("email")

        // Establecer los valores en los EditTexts
        nombreCompletoEditText.setText(nombreCompleto)
        emailEditText.setText(email)

        // Configura SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // Cargar datos guardados
        loadProfileData()

        // Abrir galería para cambiar foto de perfil
        editImageButton.setOnClickListener {
            openGalleryForImage()
        }

        // Configurar botones de guardar, cancelar y cerrar sesión
        saveButton.setOnClickListener {
            saveProfileData()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        // Configurar la acción de la flecha de retroceso
        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            profileImageUri = data?.data
            profileImageView.setImageURI(profileImageUri)
        }
    }

    private fun loadProfileData() {
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        nombreCompletoEditText.setText(username)
        emailEditText.setText(email)
        passwordEditText.setText(password)
    }

    private fun saveProfileData() {
        val editor = sharedPreferences.edit()
        editor.putString("username", nombreCompletoEditText.text.toString())
        editor.putString("email", emailEditText.text.toString())
        editor.putString("password", passwordEditText.text.toString())
        editor.apply()
    }

    private fun logout() {
        nombreCompletoEditText.setText(null)
        emailEditText.setText(null)
        passwordEditText.setText(null)

        // Regresar al usuario a la pantalla de inicio
        val goToPerfil = Intent(this, PerfilActivity::class.java)
        startActivity(goToPerfil)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // Añade animación al volver
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
