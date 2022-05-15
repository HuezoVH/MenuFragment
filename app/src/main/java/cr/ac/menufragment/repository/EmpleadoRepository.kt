package cr.ac.menufragment.repository

import cr.ac.menufragment.entity.Empleado
import cr.ac.menufragment.service.EmpleadoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmpleadoRepository {
    val empleadoService : EmpleadoService

    companion object{
        @JvmStatic
        val instance by lazy{
            EmpleadoRepository().apply {  }
        }
    }

    constructor(){
        var retrofit = Retrofit.Builder()
            .baseUrl("https://etiquicia.click/restAPI/api.php/records/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        empleadoService = retrofit.create(EmpleadoService::class.java)
    }

    fun crear(empleado: Empleado){
        empleadoService.save(empleado).execute()
    }

    fun edit(empleado: Empleado){
        empleadoService.update(empleado.idEmpleado, empleado).execute()
    }

    fun borrar (empleado: Empleado){
        empleadoService.delete(empleado.idEmpleado).execute()
    }

    fun data():List<Empleado>{
        return empleadoService.getEmpleado().execute().body()!!.records
    }
}