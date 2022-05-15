package cr.ac.menufragment.entity

import android.media.Image
import android.widget.ImageView
import java.io.Serializable

class Empleado(
    val idEmpleado : Int,
    var identificacion : String,
    var nombre : String,
    var puesto : String,
    var departamento : String,
    var avatar : String):Serializable {

}