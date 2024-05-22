package com.sistema.festec2024

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class Escritorio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escritorio)
        showGIF()
        val BotonRegistrarVisita=findViewById<Button>(R.id.btn_agregar_visita)
        BotonRegistrarVisita.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,FormularioVisitas::class.java)
            startActivity(intent)
        })
        val BotonListarVisitas=findViewById<Button>(R.id.btn_listar_visitantes)
        BotonListarVisitas.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,ListarVisitantes::class.java)
            startActivity(intent)
        })
    }
    fun showGIF(){
        val imageView: ImageView = findViewById(R.id.imageSaludo2)
        Glide.with(this).load(R.drawable.contacto).into(imageView)
    }
}