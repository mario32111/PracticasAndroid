package org.example

//Es una constante, por lo que su valor tampoco puede cambiar

const val PI= 3.1416
fun main() {
    println("hello world")
    //se puede quitar el :int
    var dinero : Int = 10
    println(dinero)
    dinero = 5
    println(dinero)
    //val hace que la variable no se pueda cambar
    val nombre = "Maria"
    println(nombre)
    println(PI)
    val boolean : Boolean= true
    //Puede ser 3L
    val numeroLargo : Long = 300000000000000
    val double : Double = 2.7182
    val float : Float =1.1f

    val primerValor = 20
    val segundoValor = 10

    val tercerValor = primerValor.minus(segundoValor)
    val cuartoValor= primerValor - segundoValor
    println(tercerValor+ cuartoValor)

}