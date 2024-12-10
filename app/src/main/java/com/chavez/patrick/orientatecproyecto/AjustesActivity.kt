package com.chavez.patrick.orientatecproyecto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chavez.patrick.orientatecproyecto.databinding.ActivityAjustesBinding

//AGREGAR LÓGICA EN ELIMINAR USUARIO

class AjustesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjustesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjustesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acción para el botón de retroceso
        binding.backArrow.setOnClickListener {
            onBackPressed() // Llama a onBackPressed para que se aplique la animación
        }

        // Acción para abrir configuraciones de notificaciones
        binding.notificacionesLayout.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
            startActivity(intent)
        }

        // Acción para abrir configuraciones de permisos
        binding.permisosLayout.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }

        // Acción para abrir configuraciones de almacenamiento para borrar caché
        binding.cacheLayout.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }

        // Acción para eliminar cuenta
        binding.eliminarCuentaLayout.setOnClickListener {
            mostrarDialogoEliminarCuenta()
        }
    }

    private fun mostrarDialogoEliminarCuenta() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Eliminar cuenta")
            .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> eliminarCuenta() }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun eliminarCuenta() {
        // Aquí va la lógica para eliminar la cuenta del usuario
        Toast.makeText(this, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // Añade animación al volver
    }
}
