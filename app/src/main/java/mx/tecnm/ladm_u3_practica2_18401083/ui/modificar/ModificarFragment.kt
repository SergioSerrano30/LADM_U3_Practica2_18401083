package mx.tecnm.ladm_u3_practica2_18401083.ui.modificar

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.ladm_u3_practica2_18401083.databinding.FragmentModificarBinding

class ModificarFragment : Fragment() {

    private var _binding: FragmentModificarBinding? = null
    lateinit var idElegido:String
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val baseRemota = FirebaseFirestore.getInstance()
    var listaId = ArrayList<String>()
    var listaDatos = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ModificarViewModel::class.java)

        _binding = FragmentModificarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        baseRemota.collection("Area")
            .addSnapshotListener { query, error ->

                if (error != null) {
                    //Si hubo error
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }


                listaId.clear()
                listaDatos.clear()
                for (documento in query!!) {
                    var cadena = "Descripcion: ${documento.getString("DESCRIPCION")}\n" +
                            "División: ${documento.getString("DIVISION")}\n" +
                            "Cant. Empleados: ${documento.getLong("CANTIDAD_EMPLEADOS")}"
                    listaDatos.add(cadena)
                    listaId.add(documento.id.toString())
                }
                binding.lvArea.adapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.simple_list_item_1, listaDatos
                )
                binding.lvArea.setOnItemClickListener { adapterView, view, pos, l ->
                    idElegido = listaId.get(pos)
                    cargarDatos()
                }

            }

        binding.btnModificar.setOnClickListener {
            baseRemota.collection("Area")
                .document(idElegido)
                .update("DESCRIPCION",binding.txtDescripcion.toString(),
                    "DIVISION",binding.txtDivision.toString(),
                    "CANTIDAD_EMPLEADOS",binding.txtCantUsuarios.toString().toInt())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Modificado con éxito", Toast.LENGTH_LONG).show()
                    binding.txtDescripcion.setText("")
                    binding.txtDivision.setText("")
                    binding.txtCantUsuarios.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }

        return root
    }

    private fun cargarDatos() {
        baseRemota.collection("Area")
            .document(idElegido)
            .get()
            .addOnSuccessListener {
                binding.txtDescripcion.setText(it.getString("DESCRIPCION"))
                binding.txtDivision.setText(it.getString("DIVISION"))
                binding.txtCantUsuarios.setText(it.getLong("CANTIDAD_EMPLEADOS").toString())

            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}