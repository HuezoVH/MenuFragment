package cr.ac.menufragment

import android.app.Activity
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val PICK_IMAGE = 100
/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var img_avatar : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_empleado, container, false)

        view.findViewById<FloatingActionButton>(R.id.add_imagen_boton).setOnClickListener{ OnCLickAddImage() }
        view.findViewById<Button>(R.id.add_cancelar_boton).setOnClickListener{ OnClickClose() }
        view.findViewById<Button>(R.id.boton_agregar).setOnClickListener{ OnClickAdd() }

        return view
    }

    private fun OnCLickAddImage(){
        var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            var imageUri = data?.data
            var imagen = view?.findViewById<CircleImageView>(R.id.add_avatar)
            Picasso.get().load(imageUri).resize(170,170).centerCrop().into(imagen)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }

    fun OnClickAdd(){//crear esta funcion

        //  Variables
        val add_id = view?.findViewById<TextInputEditText>(R.id.add_id)
        val add_nombre = view?.findViewById<TextInputEditText>(R.id.add_nombre)
        val add_puesto = view?.findViewById<TextInputEditText>(R.id.add_puesto)
        val add_departamento = view?.findViewById<TextInputEditText>(R.id.add_departamento)
        img_avatar = view?.findViewById(R.id.add_avatar)!!
        val add_avatar = encodeImage(img_avatar.drawable.toBitmap())!!

        var idEmpleado : Int = EmpleadoRepository.instance.data().size+1

        //  Se crea la instancia del Empleado con cada uno de los parametros
        var empleado : Empleado = Empleado(
            idEmpleado,
            add_id?.text.toString(),
            add_nombre?.text.toString(),
            add_puesto?.text.toString(),
            add_departamento
                ?.text.toString(),
            add_avatar,
        )
        //  Guarda al empleado
        empleado?.let { EmpleadoRepository.instance.crear(it) }

        //  Regresa al menu de Empleados
        var fragmento : Fragment = CamaraFragment.newInstance("Camara" )
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.home_content, fragmento)
            ?.commit()
        activity?.title = ("Camara")
    }

    fun OnClickClose(){//crear esta funcion
        var fragmento : Fragment = CamaraFragment.newInstance("Camara" )
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.home_content, fragmento)
            ?.commit()
        activity?.title = ("Camara")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}