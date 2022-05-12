package com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.ui.notifications

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_firebase_mapeo_empresas_jorge_alberto_perez_soto_20buena.databinding.FragmentConsultaBinding
import com.google.firebase.firestore.FirebaseFirestore

class ConsultaFragment : Fragment() {




    val dataLista=ArrayList<String>()
    var listaID =ArrayList<String>()


    private var _binding: FragmentConsultaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentConsultaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.descripcion.setOnClickListener {
            consultarDescripcion()
        }


        binding.division.setOnClickListener {
            consultarDivision()
        }

        binding.edificio.setOnClickListener {
            consultarEdificio()
        }
        binding.subdivision.setOnClickListener {

        }
        binding.subdescripcion.setOnClickListener {

        }









        return root
    }

    private fun consultarDescripcion() {
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
                    val cadena = "Descripcion de Area: ${documento.getString("descripcion")}"


                    dataLista.add(cadena)

                    listaID.add(documento.id)
                }

                binding.lista.adapter = ArrayAdapter<String>(
                    this.requireContext(),
                    R.layout.simple_list_item_1, dataLista
                )
                binding.lista.setOnItemClickListener { adapter, view, posicion, l ->
                    AlertDialog.Builder(this.requireContext()).setTitle("Contenido")
                        .setMessage("Descripcion: ${dataLista.get(posicion)}")

                        .setNeutralButton("Cancelar") { d, i ->
                        }
                        .show()
                }

            }
    }

    private fun consultarDivision() {
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
                    val cadena = "Division: ${documento.getString("division")}"


                    dataLista.add(cadena)

                    listaID.add(documento.id)
                }
                binding.lista.adapter = ArrayAdapter<String>(
                    this.requireContext(),
                    R.layout.simple_list_item_1, dataLista
                )
                binding.lista.setOnItemClickListener { adapter, view, posicion, l ->
                    AlertDialog.Builder(this.requireContext()).setTitle("Contenido")
                        .setMessage("${dataLista.get(posicion)}")

                        .setNeutralButton("Cancelar") { d, i ->
                        }
                        .show()
                }

            }
    }

    private fun consultarEdificio() {
        FirebaseFirestore.getInstance().collection("area").document()
            .collection("subDepartamento")
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
                    val cadena = "Edificio: ${documento.getString("idEdificio")}"


                    dataLista.add(cadena)

                    listaID.add(documento.id)
                }
                binding.lista.adapter = ArrayAdapter<String>(
                    this.requireContext(),
                    R.layout.simple_list_item_1, dataLista
                )
                binding.lista.setOnItemClickListener { adapter, view, posicion, l ->
                    AlertDialog.Builder(this.requireContext()).setTitle("Contenido")
                        .setMessage("${dataLista.get(posicion)}")

                        .setNeutralButton("Cancelar") { d, i ->
                        }
                        .show()
                }

            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}