package cr.ac.menufragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.menufragment.adapter.EmpleadoAdapter
import cr.ac.menufragment.entity.Empleado
import cr.ac.menufragment.repository.EmpleadoRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CamaraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CamaraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_camara, container, false)
        val empleadoList = vista.findViewById<ListView>(R.id.empleados_list)
        val empleadoRepository = EmpleadoRepository()

        val empleadoAdapter = context?.let { EmpleadoAdapter(it, EmpleadoRepository.instance.data()) }

        //Lista de empleados
        empleadoList.onItemClickListener = AdapterView.OnItemClickListener{
            parent, view, position, id ->
            var empleado = parent.getItemAtPosition(position)
            var fragmento : Fragment = EditEmpleadoFragment.newInstance(empleado as Empleado)
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.home_content, fragmento)
                ?.commit()
            activity?.setTitle("Empleado")
        }

        empleadoList.setAdapter(empleadoAdapter)

        //Agregar empleado boton
        vista.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{ OnClickAdd() }

        return vista
    }

    fun OnClickAdd(){
        var fragmento : Fragment = AddEmpleadoFragment.newInstance("Agregar")
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.home_content, fragmento)
            ?.commit()
        activity?.setTitle("Agregar")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment CamaraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            CamaraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}