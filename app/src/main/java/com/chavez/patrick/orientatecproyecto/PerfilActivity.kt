package com.chavez.patrick.orientatecproyecto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
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
    private lateinit var nombreEditButton: ImageButton
    private lateinit var emailEditButton: ImageButton
    private lateinit var contraseñaEditButton: ImageButton


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
        nombreEditButton = findViewById(R.id.editUserIconButton)
        emailEditButton = findViewById(R.id.editEmailIconButton1)
        contraseñaEditButton = findViewById(R.id.passwordEditText1)

        val nombreCompleto = intent.getStringExtra("nombre_completo")
        val email = intent.getStringExtra("email")
        val contraseña = intent.getStringExtra("contraseña")

        nombreCompletoEditText.text = (nombreCompleto ?: "Name") as Editable?
        emailEditText.text = (email ?: "example@gmail.com") as Editable?
        passwordEditText.text = (contraseña ?: "example123") as Editable?

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        loadProfileData()

        // Inicialmente, los EditTexts están desactivados para edición
        nombreCompletoEditText.isEnabled = false
        emailEditText.isEnabled = false
        passwordEditText.isEnabled = false

        nombreEditButton.setOnClickListener {
            nombreCompletoEditText.isEnabled = true
            nombreCompletoEditText.requestFocus()  // Coloca el cursor en el campo para edición
        }

// Habilita el campo de email cuando se presione el botón de editar
        emailEditButton.setOnClickListener {
            emailEditText.isEnabled = true
            emailEditText.requestFocus()
        }

// Habilita el campo de contraseña cuando se presione el botón de editar
        contraseñaEditButton.setOnClickListener {
            passwordEditText.isEnabled = true
            passwordEditText.requestFocus()
        }

        // Abrir galería para cambiar foto de perfil
        editImageButton.setOnClickListener {
            openGalleryForImage()
        }

        // Configurar botones de guardar, cancelar y cerrar sesión
        saveButton.setOnClickListener {
            val nombreNuevo = nombreCompletoEditText.text.toString()
            val emailNuevo = emailEditText.text.toString()
            val contraseñaNueva = passwordEditText.text.toString()

            //ESTO DEBO DE VERLO EN LA API actualizarPerfil(nombreNuevo, emailNuevo, contraseñaNueva)

            nombreCompletoEditText.isEnabled = false
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
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
            nombreCompletoEditText.setText(sharedPreferences.getString("nombre_completo", "Name"))
            emailEditText.setText(sharedPreferences.getString("email", "example@gmail.com"))
            passwordEditText.setText(sharedPreferences.getString("contraseña", "example123"))
        }

    private fun saveProfileData() {
        val editor = sharedPreferences.edit()
        editor.putString("nombre_completo", nombreCompletoEditText.text.toString())
        editor.putString("email", emailEditText.text.toString())
        editor.putString("password", passwordEditText.text.toString())
        editor.apply()
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val goToHome = Intent(this, LoginActivity::class.java)
        startActivity(goToHome)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
