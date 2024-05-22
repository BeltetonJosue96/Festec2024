package com.sistema.festec2024

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sistema.festec2024.ListarVisitantes.Visitante

class VisitasAdapter(context: Context, private val visitantes: List<Visitante>) : ArrayAdapter<Visitante>(context, R.layout.list_item_visitante, visitantes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_visitante, parent, false)

        val visitante = visitantes[position]
        val id = view.findViewById<TextView>(R.id.id_visitante)
        val nombre = view.findViewById<TextView>(R.id.nombre)
        val apellido = view.findViewById<TextView>(R.id.apellido)
        val correo = view.findViewById<TextView>(R.id.correo_visitante)

        id.text = "ID: " + visitante.visitor_id.toString()
        nombre.text = "Nombre: " + visitante.name
        apellido.text = "Apellido: " + visitante.lastname
        correo.text = "Email: " + visitante.email

        view.setOnClickListener {
            val intent = Intent(context, EditarVisitante::class.java)
            intent.putExtra("visitor_id", visitante.visitor_id)
            intent.putExtra("name", visitante.name)
            intent.putExtra("lastname", visitante.lastname)
            intent.putExtra("email", visitante.email)
            context.startActivity(intent)
        }

        return view
    }
}