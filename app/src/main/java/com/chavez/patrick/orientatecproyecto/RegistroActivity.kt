package com.chavez.patrick.orientatecproyecto
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegistroActivity : AppCompatActivity() {
    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var startButton: Button
    private lateinit var eyeIcon: ImageView
    private lateinit var loginText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        startButton = findViewById(R.id.startButton)
        eyeIcon = findViewById(R.id.eyeIcon)
        loginText = findViewById(R.id.loginText)

        // Agrega subrayado al TextView
        loginText.paintFlags = loginText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        startButton.setOnClickListener {
            registerUser()
        }

        // Configura el listener para el ícono de ojo
        eyeIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        // Configura el listener para el TextView de inicio de sesión
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val fullName = fullNameInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        // Validar que los campos no estén vacíos
        if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            // Validar formato de email
            if (!isValidEmail(email)) {
                Toast.makeText(this, "Email no válido.", Toast.LENGTH_SHORT).show()
                return
            }

            val url = "http://192.168.18.35:9080/api/v1/user/register"

            val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.optString("status")
                    if (status.equals("success", ignoreCase = true)) {
                        Toast.makeText(this, "Usuario : $fullName registrado correctamente", Toast.LENGTH_LONG).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            navigateToLoginActivity()
                        }, 2000)
                    } else {
                        Toast.makeText(this, "Error en el registro: $status", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this, "Error al registrar el usuario: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nombre_completo"] = fullName
                    params["email"] = email
                    params["contraseña"] = password
                    return params
                }
            }

            // Agregar la solicitud a la cola
            Volley.newRequestQueue(this).add(stringRequest)
        } else {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePasswordVisibility() {
        // Verifica el tipo de entrada actual del campo de contraseña
        if (passwordInput.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Oculta la contraseña
            passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            eyeIcon.setImageResource(R.drawable.cerrar_ojo) // Cambia al ícono de ojo cerrado
        } else {
            // Muestra la contraseña
            passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            eyeIcon.setImageResource(R.drawable.ojo) // Cambia al ícono de ojo abierto
        }
        passwordInput.setSelection(passwordInput.text.length) // Mantener el cursor al final
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java) // Crear el intent
        startActivity(intent) // Iniciar la nueva actividad
        finish() // Finaliza la actividad actual
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return emailPattern.matches(email)
    }
}
