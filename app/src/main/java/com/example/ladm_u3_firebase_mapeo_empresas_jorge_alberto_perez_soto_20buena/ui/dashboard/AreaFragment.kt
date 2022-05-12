package com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.ui.dashboard


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.AgrgarSubDepartamento
import com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.databinding.FragmentAreaBinding
import com.google.firebase.firestore.FirebaseFirestore


class AreaFragment : Fragment() {



    val dataLista=ArrayList<String>()
    var listaID =ArrayList<String>()



    private var _binding: FragmentAreaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentAreaBinding.inflate(inflater, container, false)
        val root: View = binding.root


       mostrarEnLista()

        return root
    }

    private fun insertarArea() {
        val datos = hashMapOf(
            "descripcion" to binding.descripcion.text.toString(),
            "division" to binding.division.text.toString(),
            "canEmpleados" to binding.canEmpleados.text.toString().toInt()
        )
        FirebaseFirestore.getInstance().collection("area")
            .add(datos)
            .addOnSuccessListener {
                //si se pudo
                Toast.makeText(this.requireContext(), "Exito en la insercion", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                //no se pudo
                AlertDialog.Builder(this.requireContext())
                    .setMessage(it.message)
                    .show()
            }
        binding.descripcion.setText("")
        binding.division.setText("")
        binding.canEmpleados.setText("")
        mostrarEnLista()
    }


    private fun mostrarEnLista() {

        FirebaseFirestore.getInstance()
            .collection("area")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //si hubo error!
                    AlertDialog.Builder(this.requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                dataLista.clear()
                for (documento in query!!) {
                    val cadena = " Descripcion: ${documento.getString("descripcion")}\n" +
                            " Division: ${documento.getString("division")}\n" +
                            " No. de Empleados: ${documento.getLong("canEmpleados").toString()}"


                    dataLista.add(cadena)

                    listaID.add(documento.id)
                }
                binding.lista.adapter = ArrayAdapter<String>(
                    this.requireContext(),
                    android.R.layout.simple_list_item_1, dataLista
                )
                binding.lista.setOnItemClickListener { adapter, view, posicion, l ->

                    dialogoEliminarAcualizar(posicion)

                }
                binding.insertar.setOnClickListener() {
                    insertarArea()
                }

            }
    }


    private fun dialogoEliminarAcualizar(posicion: Int) {
        val idElegido = listaID.get(posicion)

        AlertDialog.Builder(this.requireContext()).setTitle("CUENTAME")
            .setMessage("¿Que deceas hacer con \n${dataLista.get(posicion)}?")

            .setPositiveButton("Eliminar"){d,i->
                eliminar(idElegido)
            }
            .setNeutralButton("Actualizar"){d,i->
                actualizar(idElegido)
                binding.insertar.isEnabled=false
            }
            .setNegativeButton("+SubDepartamento"){d,i->
                agregarDep(idElegido)
                //AlertDialog.Builder(requireContext()).setMessage(idElegido).show()


            }
            .show()
    }


    private fun eliminar(idElegido: String) {
        FirebaseFirestore.getInstance()
            .collection("area")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this.requireContext(),"Se Elimino con exito!", Toast.LENGTH_LONG).show()
                mostrarEnLista()
            }
            .addOnFailureListener{
                AlertDialog.Builder(this.requireContext())
                    .setMessage(it.message)
                    .show()
            }

    }

    private fun actualizar(idElegido: String) {
        binding.actualizar.isEnabled=true

        FirebaseFirestore.getInstance()
            .collection("area")
            .document(idElegido)
            .get()// Obtiene un documento
            .addOnSuccessListener {
                //si se pudo

                binding.descripcion.setText(it.getString("descripcion"))
                binding.division.setText(it.getString("division"))
                binding.canEmpleados.setText(it.getLong("canEmpleados").toString())

                Toast.makeText(this.requireContext(),"Se Cargaron los datos.", Toast.LENGTH_LONG).show()

                binding.insertar.isEnabled=false
                binding.actualizar.isEnabled=true

            }
            .addOnFailureListener{
                //no se pudo
                AlertDialog.Builder(this.requireContext())
                    .setMessage(it.message)
                    .show()
            }

        binding.actualizar.setOnClickListener {

            FirebaseFirestore.getInstance()
                .collection("area")
                .document(idElegido)
                .update("descripcion" , binding.descripcion.text.toString(),
                    "division", binding.division.text.toString(),
                    "canEmpleados",binding.canEmpleados.text.toString().toInt())
                .addOnSuccessListener {

                    binding.insertar.isEnabled=true
                    binding.actualizar.isEnabled=false

                    binding.descripcion.text.clear()
                    binding.division.text.clear()
                    binding.canEmpleados.text.clear()

                    mostrarEnLista()

                }
                .addOnFailureListener{
                    AlertDialog.Builder(this.requireContext())
                        .setMessage(it.message)
                        .show()
                }


        }


    }

    private fun agregarDep(idElegido: String) {

         val otraVentana = Intent(this.requireContext(),AgrgarSubDepartamento::class.java)
          otraVentana.putExtra("idArea",idElegido)
          startActivity(otraVentana)



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}