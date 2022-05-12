package mx.tecnm.ladm_u3_practica2_18401083.ui.buscar

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.ladm_u3_practica2_18401083.databinding.FragmentBuscarBinding

class BuscarFragment : Fragment() {

    private var _binding: FragmentBuscarBinding? = null
    val baseRemota = FirebaseFirestore.getInstance()
    var listaId = ArrayList<String>()
    var listaDatos = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(BuscarViewModel::class.java)

        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarLista()


        binding.rbArea.setOnClickListener {
            ponerOpcionesArea()
            //mostrarArea_Todo()
        }
        binding.rbSubDep.setOnClickListener {
            ponerOpcionesSubDep()
            subDepa_Todo()
            //mostrarSubDepartamento_Todo()
        }
        binding.rbOp1.setOnClickListener {
            bloqueaBusqueda()
            //if (binding.rbArea.isChecked){mostrarArea_Todo()}
            //if (binding.rbSubDep.isChecked){mostrarSubDepartamento_Todo()}
        }
        binding.rbOp2.setOnClickListener {
            activaBusqueda()
        }
        binding.rbOp3.setOnClickListener {
            activaBusqueda()
        }
        binding.rbOp4.setOnClickListener {
            activaBusqueda()
        }

        return root
    }

    private fun mostrarLista() {
        baseRemota.collection("Area")
            .addSnapshotListener { query, error ->

                if (error !=null){
                    //Si hubo error
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }


                listaId.clear()
                listaDatos.clear()
                for (documento in query!!){
                    var cadena = "Descripcion: ${documento.getString("DESCRIPCION")}\n" +
                            "División: ${documento.getString("DIVISION")}\n" +
                            "Cant. Empleados: ${documento.getLong("CANTIDAD_EMPLEADOS")}"
                    listaDatos.add(cadena)
                    listaId.add(documento.id.toString())
                }
                binding.lvDatos.adapter = ArrayAdapter<String>(this.requireContext(),
                    R.layout.simple_list_item_1, listaDatos)
                binding.lvDatos.setOnItemClickListener { adapterView, view, pos, l ->
                    //dialogEliminaActualiza(pos)
                }

            }
    }
    private fun subDepa_Todo(){
        



    }

    private fun bloqueaBusqueda() {
        binding.txtBusqueda.setText("")
        binding.txtBusqueda.isEnabled = false
    }
    private fun activaBusqueda() {
        binding.txtBusqueda.setText("")
        binding.lvDatos.adapter = null
        binding.txtBusqueda.isEnabled = true
    }

    fun ponerOpcionesArea(){
        bloqueaBusqueda()
        binding.rbOp1.setText("Todos")
        binding.rbOp2.setText("Descripción")
        binding.rbOp3.setText("División")
        binding.rbOp1.isChecked = true
        binding.rbOp4.isVisible = false
    }
    fun ponerOpcionesSubDep(){
        bloqueaBusqueda()
        binding.rbOp1.setText("Todos")
        binding.rbOp2.setText("Id Edificio")
        binding.rbOp3.setText("Descripción")
        binding.rbOp4.setText("División")
        binding.rbOp1.isChecked = true
        binding.rbOp4.isVisible = true

    }

    override fun onStart() {
        super.onStart()
        binding.rbArea.isChecked = true
        ponerOpcionesArea()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}