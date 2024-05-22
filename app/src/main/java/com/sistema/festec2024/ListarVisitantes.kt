package com.sistema.festec2024

import android.content.Context
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.io.Serializable


class ListarVisitantes : AppCompatActivity() {
    data class Visitante(
        val visitor_id: Int,
        val name: String,
        val lastname: String,
        val email: String,
        val createdAt: String,
        val updatedAt: String
    ) : Serializable
    private lateinit var lista: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_visitantes)
        lista = findViewById(R.id.lista_visitantes)
        val client = OkHttpClient()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token == null) {
            runOnUiThread {
                Toast.makeText(this@ListarVisitantes, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val request = Request.Builder()
            .url("https://us-central1-sublime-triode-420903.cloudfunctions.net/api/visitors/getAll")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un mensaje de error
                runOnUiThread {
                    Toast.makeText(this@ListarVisitantes, "Lo sentimos existen fallos de comunicación, verifique su conexión a Internet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val gson = Gson()
                    val type = object : TypeToken<ApiResponse<List<Visitante>>>() {}.type
                    val apiResponse: ApiResponse<List<Visitante>> = gson.fromJson(response.body?.string(), type)

                    if (apiResponse.message == "ok") {
                        runOnUiThread {
                            val adapter = VisitasAdapter(this@ListarVisitantes, apiResponse.data)
                            lista.adapter = adapter
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ListarVisitantes, "Error: ${apiResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        })
    }
    data class ApiResponse<T>(
        val message: String,
        val data: T
    )
}