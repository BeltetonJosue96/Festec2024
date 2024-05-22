package com.sistema.festec2024

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FormularioVisitas : AppCompatActivity() {
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var correo: EditText
    private lateinit var agregar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_visitas)
        nombre = findViewById(R.id.nombre)
        apellido = findViewById(R.id.apellidos)
        correo = findViewById(R.id.correo)
        agregar = findViewById(R.id.btn_agregar_visitante)

        agregar.setOnClickListener {
            if (validarCampos()) {
                enviarDatos()
            }
        }
    }
    private fun validarCampos(): Boolean {
        val nombre = nombre.text.toString()
        val apellidos = apellido.text.toString()
        val correo = correo.text.toString()

        if (nombre.isEmpty() || apellidos.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(
                this,
                "Por favor, introduzca un correo electrónico válido.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    private fun enviarDatos() {
        val json = JSONObject()
        json.put("name", nombre.text.toString())
        json.put("lastname", apellido.text.toString())
        json.put("email", correo.text.toString())

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        // Comprobar si el token es nulo
        if (token == null) {
            runOnUiThread {
                Toast.makeText(this@FormularioVisitas, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/visitors/create")
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@FormularioVisitas, "Lo sentimos existen fallos de comunicación, verifique su conexión a Internet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@FormularioVisitas, "Visita agregada satisfactoriamente", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@FormularioVisitas, "Lo sentimos, no se pudo completar la petición", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    private fun limpiarCampos() {
        nombre.text.clear()
        apellido.text.clear()
        correo.text.clear()
    }
}