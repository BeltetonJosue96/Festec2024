package com.sistema.festec2024

import android.content.Intent
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
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegistrarUsuario : AppCompatActivity() {
    private lateinit var usuario: EditText
    private lateinit var correo: EditText
    private lateinit var contra: EditText
    private lateinit var crear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        usuario = findViewById(R.id.reg_usuario)
        correo = findViewById(R.id.correo_usuario)
        contra = findViewById(R.id.contra)
        crear = findViewById(R.id.btn_registrar_nuevo_usuario)
        crear.setOnClickListener {
            if (validarCampos()) {
                enviarDatos()
            }
        }
    }
    private fun validarCampos(): Boolean {
        val user = usuario.text.toString()
        val email = correo.text.toString()
        val pass = contra.text.toString()

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
        json.put("username", usuario.text.toString())
        json.put("email", correo.text.toString())
        json.put("password", contra.text.toString())
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/users/create")
            .post(requestBody)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@RegistrarUsuario, "Lo sentimos existen fallos de comunicación", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val respuesta = JSONObject(response.body?.string())
                    val mensaje = respuesta.getString("message")
                    runOnUiThread {
                        Toast.makeText(this@RegistrarUsuario, mensaje, Toast.LENGTH_SHORT).show()
                    }
                    runOnUiThread {
                        Toast.makeText(this@RegistrarUsuario, "Usuario agregado con éxito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegistrarUsuario, Login::class.java)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@RegistrarUsuario, "Lo sentimos, no se pudo completar la petición", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}