package com.sistema.festec2024

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var user: EditText
    private lateinit var password: EditText
    private lateinit var acceder: Button
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val BotonRegistrarUsuario = findViewById<Button>(R.id.btn_registrar_usuario)
        BotonRegistrarUsuario.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegistrarUsuario::class.java)
            startActivity(intent)
        })

        user = findViewById(R.id.usuario)
        password = findViewById(R.id.correo_usuario)
        acceder = findViewById(R.id.btn_login)

        acceder.setOnClickListener {
            if (validarCampos()) {
                enviarDatos()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val username = user.text.toString()
        val contra = password.text.toString()

        if (username.isEmpty() || contra.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun enviarDatos() {
        val json = JSONObject()
        json.put("username", user.text.toString())
        json.put("pass", password.text.toString())
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/users/login")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Login, "Lo sentimos existen fallos de comunicación", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val respuesta = JSONObject(response.body?.string())
                    val token = respuesta.getJSONObject("data").getString("token")

                    // Almacenar el token en las Preferencias Compartidas
                    val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("token", token)
                        apply()
                    }

                    runOnUiThread {
                        Toast.makeText(this@Login, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, Escritorio::class.java)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Login, "Lo sentimos, no se pudo iniciar sesión, verifique el usuario y contraseña", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
