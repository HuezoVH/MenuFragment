package cr.ac.menufragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import cr.ac.menufragment.entity.Empleado
import cr.ac.menufragment.repository.EmpleadoRepository
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val PICK_IMAGE = 100
/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)
        val edit_id = view.findViewById<TextInputEditText>(R.id.edit_id)
        val edit_nombre = view.findViewById<TextInputEditText>(R.id.edit_nombre)
        val edit_puesto = view.findViewById<TextInputEditText>(R.id.edit_puesto)
        val edit_departamento = view.findViewById<TextInputEditText>(R.id.edit_departamento)
        img_avatar= view.findViewById(R.id.edit_avatar)

        //  Rellena los campos para editar
        edit_id.setText(empleado?.identificacion.toString())
        edit_nombre.setText(empleado?.nombre.toString())
        edit_puesto.setText(empleado?.puesto.toString())
        edit_departamento.setText(empleado?.departamento.toString())
        if(empleado?.avatar !=""){
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }

//        empleado?.identificacion = edit_id.text.toString()
//        empleado?.nombre = edit_nombre.text.toString()
//        empleado?.puesto = edit_puesto.text.toString()
//        empleado?.departamento = edit_departamento.text.toString()

        view.findViewById<FloatingActionButton>(R.id.edit_imagen_boton).setOnClickListener{ OnCLickEditImage() }
        view.findViewById<Button>(R.id.edit_cancelar_boton).setOnClickListener{ OnClickClose() }//en el on create
        view.findViewById<Button>(R.id.boton_editar).setOnClickListener{ OnClickEdit() }//en el on create
        view.findViewById<Button>(R.id.boton_eliminar).setOnClickListener { OnClickEliminar() }
        return view
    }

    private fun OnCLickEditImage(){
        var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            var imageUri = data?.data
            var imagen = view?.findViewById<CircleImageView>(R.id.edit_avatar)
            Picasso.get().load(imageUri).resize(170,170).centerCrop().into(imagen)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }

    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun OnClickEdit(){//crear esta funcion

        val edit_id = view?.findViewById<TextInputEditText>(R.id.edit_id)
        val edit_nombre = view?.findViewById<TextInputEditText>(R.id.edit_nombre)
        val edit_puesto = view?.findViewById<TextInputEditText>(R.id.edit_puesto)
        val edit_departamento = view?.findViewById<TextInputEditText>(R.id.edit_departamento)
        //  val edit_avatar = view?.findViewById<CircleImageView>(R.id.edit_avatar)

        //  Recupera los datos de cada TextEdit
        empleado?.identificacion = edit_id?.text.toString()
        empleado?.nombre = edit_nombre?.text.toString()
        empleado?.puesto = edit_puesto?.text.toString()
        empleado?.departamento = edit_departamento?.text.toString()
        empleado?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!

        //
        empleado?.let { EmpleadoRepository.instance.edit(it) }

        var fragmento : Fragment = CamaraFragment.newInstance("Camara" )
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.home_content, fragmento)
            ?.commit()
        activity?.setTitle("Camara")
    }

    private fun OnClickEliminar(){

        val builder = AlertDialog.Builder(context)
        builder.setMessage("¿Desea modificar el registro?")
            .setCancelable(false)
            .setPositiveButton("Sí") { dialog, id ->
                empleado?.let { EmpleadoRepository.instance.borrar(it) }
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                // logica del no
            }
        val alert = builder.create()
        alert.show()
//        var fragmento : Fragment = CamaraFragment.newInstance("Camara" )
//        fragmentManager
//            ?.beginTransaction()
//            ?.replace(R.id.home_content, fragmento)
//            ?.commit()
//        activity?.setTitle("Camara")
    }

    private fun OnClickClose(){//crear esta funcion
        var fragmento : Fragment = CamaraFragment.newInstance("Camara" )
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.home_content, fragmento)
            ?.commit()
        activity?.setTitle("Camara")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empelado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado : Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}