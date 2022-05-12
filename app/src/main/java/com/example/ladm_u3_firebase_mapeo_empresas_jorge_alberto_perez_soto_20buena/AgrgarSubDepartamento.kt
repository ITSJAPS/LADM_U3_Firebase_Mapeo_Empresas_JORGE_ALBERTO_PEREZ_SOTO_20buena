package com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.databinding.ActivityAgrgarSubDepartamentoBinding
import com.google.firebase.firestore.FirebaseFirestore

class AgrgarSubDepartamento : AppCompatActivity() {
    lateinit var binding:ActivityAgrgarSubDepartamentoBinding

    var baseRemota =   FirebaseFirestore.getInstance()
    val este = this


    val dataLista=ArrayList<String>()
    var listaID =ArrayList<String>()

    var idArea2=""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityAgrgarSubDepartamentoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idArea2 = this.intent.extras!!.getString("idArea")!!



        mostrarEnLista()


        binding.insertar.isEnabled = true


        binding.insertar.setOnClickListener() {

            val datos = hashMapOf(
                "idEdificio" to binding.edificio.text.toString(),
                "piso" to binding.textopiso.text.toString()
            )

            baseRemota.collection("area").document(idArea2).collection("subDepartamento")
                .add(datos)
                .addOnSuccessListener {
                    //si se pudo

                    Toast.makeText(this, "Exito en la insercion", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    //no se pudo
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
            binding.edificio.setText("")
            binding.textopiso.setText("")



        }

        binding.regresar.setOnClickListener {
            finish()

        }

    }



    private fun mostrarEnLista() {
        FirebaseFirestore.getInstance().collection("area").document(idArea2).collection("subDepartamento")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //si hubo error!
                    AlertDialog.Builder(this)
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                dataLista.clear()
                for (documento in query!!) {
                    var cadena = "ID del Edificio ${documento.getString("idEdificio")}\n" +
                            "No. de Piso: ${documento.getString("piso")}"



                    dataLista.add(cadena)

                    listaID.add(documento.id.toString())
                }
                binding.lista.adapter=ArrayAdapter<String>(this,
                    R.layout.simple_list_item_1,dataLista)

                binding.lista.setOnItemClickListener { adapter, view, posicion, l ->

                    dialogoEliminarAcualizar(posicion)

                }

            }
    }
    private fun dialogoEliminarAcualizar(posicion: Int) {
        var idElegido = listaID.get(posicion)

        AlertDialog.Builder(este).setTitle("CUENTAME")
            .setMessage("¿Que deceas hacer con \n${dataLista.get(posicion)}?")

            .setPositiveButton("Eliminar"){d,i->
                eliminar(idElegido)
            }
            .setNeutralButton("Actualizar"){d,i->
                actualizar(idElegido)
            }
            .setNegativeButton("Cancelar"){d,i->

            }
            .show()
    }


    private fun eliminar(idElegido: String) {
        baseRemota.collection("area").document(idArea2).collection("subDepartamento").document(idElegido)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(este,"Se Elimino con exito!", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener{
                AlertDialog.Builder(este)
                    .setMessage(it.message)
                    .show()
            }

    }

    private fun actualizar(idElegido: String) {
        baseRemota.collection("area").document(idArea2).collection("subDepartamento").document(idElegido)
            .get()// Obtiene un documento
            .addOnSuccessListener {
                //si se pudo

                binding.edificio.setText(it.getString("idEdificio"))
                binding.textopiso.setText(it.getString("piso"))


                Toast.makeText(este,"Se Cargaron los datos.", Toast.LENGTH_LONG).show()

                binding.insertar.isEnabled=false
                binding.actualizar.isEnabled=true

                binding.regresar.setText("Cancelar")

            }
            .addOnFailureListener{
                //no se pudo
                AlertDialog.Builder(este)
                    .setMessage(it.message)
                    .show()
            }

        binding.actualizar.setOnClickListener {
            baseRemota.collection("area").document(idArea2).collection("subDepartamento").document(idElegido)

                .update("idEdificio" , binding.edificio.text.toString(),
                    "piso", binding.textopiso.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(este,"Se Actualizaron correctamente!", Toast.LENGTH_LONG).show()

                    binding.edificio.text.clear()
                    binding.textopiso.text.clear()



                    binding.actualizar.isEnabled=false
                    binding.regresar.setText("Regresar")




                }
                .addOnFailureListener{
                    AlertDialog.Builder(este)
                        .setMessage(it.message)
                        .show()
                }


        }


    }


}