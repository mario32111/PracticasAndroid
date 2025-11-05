package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val nombre = "xd"

    if (nombre.isNotEmpty()){
        println("El largo de la variable nombre es: ${nombre.length}")
    }
    else{
        println("Error, ka variable esta vacia")
    }

    val mensaje : String

    if (nombre.length >4){
        mensaje= "Tu nombre es largo"
    }else if(nombre.isEmpty()){
        mensaje="El nombre esta vacio"
    }else{
        mensaje= "Tienes un nombre corto"
    }
    println(mensaje)
}