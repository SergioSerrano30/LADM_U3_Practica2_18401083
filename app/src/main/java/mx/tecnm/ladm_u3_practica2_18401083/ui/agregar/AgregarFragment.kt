package mx.tecnm.ladm_u3_practica2_18401083.ui.agregar

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import mx.tecnm.ladm_u3_practica2_18401083.MainActivity
import mx.tecnm.ladm_u3_practica2_18401083.SubDepartamento_Agregar
import mx.tecnm.ladm_u3_practica2_18401083.databinding.FragmentAgregarBinding

class AgregarFragment : Fragment() {

    private var _binding: FragmentAgregarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val baseRemota = FirebaseFirestore.getInstance()
    var listaId = ArrayList<String>()
    var listaDatos = ArrayList<String>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(AgregarViewModel::class.java)

        _binding = FragmentAgregarBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
                binding.lvArea.adapter = ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1, listaDatos)
            }
        //mostrarLista()

        binding.btnAgregar.setOnClickListener {
            //agregarArea()
            val datos = hashMapOf(
                "DESCRIPCION" to binding.txtDescripcion.text.toString(),
                "DIVISION" to binding.txtDivision.text.toString(),
                "CANTIDAD_EMPLEADOS" to binding.txtCantUsuarios.text.toString().toInt()
            )
            baseRemota.collection("Area")
                .add(datos)
                .addOnSuccessListener {
                    //Si se pudo
                    Toast.makeText(requireContext(),"Insertado correctamente", Toast.LENGTH_LONG)
                        .show()

                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }

        binding.lvArea.setOnItemClickListener { adapterView, view, pos, l ->
            dialogEliminaAgrega(pos)
        }
        return root

    }
    private fun mostrarLista(){
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
                binding.lvArea.adapter = ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1, listaDatos)
            }
    }

    private fun agregarArea() {
        val datos = hashMapOf(
            "DESCRIPCION" to binding.txtDescripcion.text.toString(),
            "DIVISION" to binding.txtDivision.text.toString(),
            "CANTIDAD_EMPLEADOS" to binding.txtCantUsuarios.text.toString().toInt()
        )
       baseRemota.collection("Area")
                .add(datos)
                .addOnSuccessListener {
                    //Si se pudo
                    Toast.makeText(requireContext(),"Insertado correctamente", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
    }

    fun dialogEliminaAgrega(pos:Int) {
        var idElegido = listaId.get(pos)
        AlertDialog.Builder(requireContext())
            .setTitle("Aviso")
            .setMessage("¿Que deseas hacer con: \n" +
                    "${idElegido}}?")
            .setPositiveButton("Agregar subDepartamento"){d,i->
                agregarSubDepartamento(idElegido)
            }
            .setNegativeButton("Eliminar"){d,i->
                eliminar(idElegido)
            }
            .setNeutralButton("Cancelar"){d,i->

            }
            .show()

    }
    private fun agregarSubDepartamento(idElegido: String) {
        var ventana = Intent(requireContext(),SubDepartamento_Agregar::class.java)
        ventana.putExtra("idElegido",idElegido)
        startActivity(ventana)
    }

    private fun eliminar(idElegido: String) {
        baseRemota.collection("Area")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Eliminado correctamente",Toast.LENGTH_LONG)
                    .show()
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