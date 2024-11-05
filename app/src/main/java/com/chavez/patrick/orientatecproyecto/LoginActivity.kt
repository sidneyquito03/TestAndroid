package com.chavez.patrick.orientatecproyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var registerText: TextView
    private lateinit var googleButton: LinearLayout
    private lateinit var loginButton: Button
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private var isPasswordVisible: Boolean = false
    private lateinit var etPasswordInput: EditText
    private lateinit var eyeIcon: ImageView
    private lateinit var etEmailInput: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmailInput = findViewById(R.id.emailInput)
        etPasswordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        eyeIcon = findViewById(R.id.eyeIcon)

        registerText = findViewById(R.id.registerText)
        registerText.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            authenticateUser()
        }

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleButton = findViewById(R.id.google)
        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        eyeIcon.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun authenticateUser() {
        if (!validateEmail() || !validatePassword()) {
            return
        }
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url = "http://192.168.18.35:9080/api/v1/user/login"
        val params = HashMap<String, String>().apply {
            put("email", etEmailInput.text.toString())
            put("password", etPasswordInput.text.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, JSONObject(params as Map<*, *>),
            Response.Listener { response ->
                try {
                    val nombreCompleto = response.getString("nombre_completo")
                    val email = response.getString("email")

                    val gotoPerfil = Intent(this, PerfilActivity::class.java)
                    gotoPerfil.putExtra("nombre_completo", nombreCompleto)
                    gotoPerfil.putExtra("email", email)
                    startActivity(gotoPerfil)
                    finish()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = if (error.networkResponse != null) {
                    val responseData = String(error.networkResponse.data)
                    Log.d("ErrorResponse", responseData)
                    responseData
                } else {
                    "Error desconocido"
                }
                Toast.makeText(this, "Login Failed: $errorMessage", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun validateEmail(): Boolean {
        val emailInput = etEmailInput.text.toString()
        if (emailInput.isEmpty()) {
            etEmailInput.error = "No agregó su email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmailInput.error = "Inserte un email válido"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val passwordInput = etPasswordInput.text.toString()
        return if (passwordInput.isEmpty()) {
            etPasswordInput.error = "No escribió su contraseña"
            false
        } else {
            etPasswordInput.error = null
            true
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            eyeIcon.setImageResource(R.drawable.cerrar_ojo)
        } else {
            etPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            eyeIcon.setImageResource(R.drawable.ojo)
        }
        etPasswordInput.setSelection(etPasswordInput.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.result
            if (account != null) {
                val email = account.email
                Log.d("GoogleSignIn", "Email: $email")
                // Redirige al usuario a la siguiente pantalla
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.w("GoogleSignIn", "Error en Google Sign-In", e)
        }
    }
}
