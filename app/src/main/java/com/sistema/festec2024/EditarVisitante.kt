package com.sistema.festec2024

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class EditarVisitante : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button

    private var visitanteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_visitante)

        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellidos)
        etCorreo = findViewById(R.id.et_correo)
        btnGuardar = findViewById(R.id.btn_mod_visitante)
        btnEliminar = findViewById(R.id.btn_eliminar_visitante)

        visitanteId = intent.getIntExtra("visitor_id", 0)
        val name = intent.getStringExtra("name")
        val lastname = intent.getStringExtra("lastname")
        val email = intent.getStringExtra("email")

        etNombre.setText(name)
        etApellido.setText(lastname)
        etCorreo.setText(email)

        btnGuardar.setOnClickListener {
            actualizarVisitante()
        }

        btnEliminar.setOnClickListener {
            eliminarVisitante()
        }
    }

    private fun actualizarVisitante() {
        val client = OkHttpClient()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token == null) {
            Toast.makeText(this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonObject = JSONObject()
        jsonObject.put("id", visitanteId)
        jsonObject.put("name", etNombre.text.toString())
        jsonObject.put("lastname", etApellido.text.toString())
        jsonObject.put("email", etCorreo.text.toString())

        val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/visitors/update")
            .addHeader("Authorization", "Bearer $token")
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@EditarVisitante, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarVisitante, "Visitante actualizado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditarVisitante, ListarVisitantes::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@EditarVisitante, "Error: ${response.body?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun eliminarVisitante() {
        val client = OkHttpClient()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token == null) {
            Toast.makeText(this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonObject = JSONObject()
        jsonObject.put("visitor_id", visitanteId)

        val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/visitors/delete")
            .addHeader("Authorization", "Bearer $token")
            .delete(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@EditarVisitante, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarVisitante, "Visitante eliminado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditarVisitante, ListarVisitantes::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@EditarVisitante, "Error: ${response.body?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}