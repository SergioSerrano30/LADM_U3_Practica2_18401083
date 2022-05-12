package mx.tecnm.ladm_u3_practica2_18401083

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.ladm_u3_practica2_18401083.databinding.ActivitySubDepartamentoAgregarBinding
import mx.tecnm.ladm_u3_practica2_18401083.databinding.FragmentAgregarBinding

class SubDepartamento_Agregar : AppCompatActivity() {
    lateinit var binding: ActivitySubDepartamentoAgregarBinding
    val baseRemota = FirebaseFirestore.getInstance()
    var listaId = ArrayList<String>()
    var listaDatos = ArrayList<String>()
    lateinit var idElegido:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idElegido = intent.extras!!.getString("idElegido")!!
        binding = ActivitySubDepartamentoAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        baseRemota.collection("Area").document(idElegido).collection("SubDepartamento")
            .addSnapshotListener { query, error ->

                if (error !=null){
                    //Si hubo error
                    AlertDialog.Builder(this)
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }


                listaId.clear()
                listaDatos.clear()
                for (documento in query!!){
                    var cadena = "Id_Edificio: ${documento.getString("ID_EDIFICIO")}\n" +
                            "Piso: ${documento.getString("PISO")}"
                    listaDatos.add(cadena)
                    listaId.add(documento.id.toString())
                }
                binding.lvSubDepartamento.adapter = ArrayAdapter<String>(this,
                    R.layout.simple_list_item_1, listaDatos)
                binding.lvSubDepartamento.setOnItemClickListener { adapterView, view, pos, l ->
                    dialogEliminaActualiza(pos)
                }

            }
        binding.btnRegresar.setOnClickListener {
            finish()
        }
        binding.btnAgregar.setOnClickListener {
            val datos = hashMapOf(
                "ID_EDIFICIO" to binding.txtIdEdificio.text.toString(),
                "PISO" to binding.txtPiso.text.toString()
            )

            baseRemota.collection("Area").document(idElegido).collection("SubDepartamento")
                .add(datos)
                .addOnSuccessListener {
                    //Si se pudo
                    Toast.makeText(this,"Insertado correctamente", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
    private fun eliminar(idEliminar: String) {
        baseRemota.collection("Area").document(idElegido).collection("SubDepartamento")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Eliminado correctamente",Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }
    }
    private fun dialogEliminaActualiza(pos:Int) {
        var idSeleccionado = listaId.get(pos)
        AlertDialog.Builder(this)
            .setTitle("Aviso")
            .setMessage("¿Que deseas hacer con: \n" +
                    "${listaDatos.get(pos)}?")
            .setPositiveButton("Eliminar"){d,i->
                eliminar(idSeleccionado)
            }
            .setNegativeButton("Actualizar"){d,i->
                modificarSubDepartamento(idSeleccionado)
            }
            .setNeutralButton("Cancelar"){d,i->

            }
            .show()

    }


    private fun modificarSubDepartamento(idSeleccionado: String) {
        var ventana = Intent(this,SubDepartamento_Modificar::class.java)
        ventana.putExtra("idSeleccionado",idSeleccionado)
        ventana.putExtra("idElegido",idElegido)
        startActivity(ventana)
    }
}