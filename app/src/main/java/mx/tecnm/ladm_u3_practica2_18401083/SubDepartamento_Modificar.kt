package mx.tecnm.ladm_u3_practica2_18401083

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.ladm_u3_practica2_18401083.databinding.ActivitySubDepartamentoModificarBinding
import mx.tecnm.ladm_u3_practica2_18401083.databinding.FragmentModificarBinding

class SubDepartamento_Modificar : AppCompatActivity() {
    lateinit var binding: ActivitySubDepartamentoModificarBinding
    val baseRemota = FirebaseFirestore.getInstance()
    var listaDatos = ArrayList<String>()
    lateinit var idElegido:String
    lateinit var idSeleccionado:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubDepartamentoModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idElegido = intent.extras!!.getString("idElegido")!!
        idSeleccionado = intent.extras!!.getString("idSeleccionado")!!
        binding.btnRegresar.setOnClickListener {
            finish()
        }
        cargarDatos()
        binding.btnModificar.setOnClickListener {
            baseRemota.collection("Area").document(idElegido)
                .collection("SubDepartamento")
                .document(idSeleccionado)
                .update("ID_EDIFICIO",binding.txtIdEdificio.text.toString(),
                "PISO",binding.txtPiso.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"Modificado con éxito", Toast.LENGTH_LONG).show()
                    binding.txtIdEdificio.setText("")
                    binding.txtPiso.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
    private fun cargarDatos() {
        baseRemota.collection("Area").document(idElegido)
            .collection("SubDepartamento")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.txtIdEdificio.setText(it.getString("ID_EDIFICIO"))
                binding.txtPiso.setText(it.getString("PISO"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }
    }

}