package com.sistema.festec2024

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val BotonIngresar=findViewById<Button>(R.id.btn_ingresar)
        BotonIngresar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        })
    }
}
