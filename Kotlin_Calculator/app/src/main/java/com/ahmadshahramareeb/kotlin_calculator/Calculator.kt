package com.ahmadshahramareeb.kotlin_calculator

class Calculator(var numberOne:Double,var numberTwo: Double) {
    /*private var numberOne:Double=0.0
    private var numberTwo:Double=0.0
*/
    fun add(numberOne:Double,numberTwo:Double):Double {
        this.numberOne=numberOne
        this.numberTwo=numberTwo
        return (numberOne+numberTwo)
    }

    fun subtract(numberOne:Double,numberTwo:Double):Double {
        this.numberOne=numberOne
        this.numberTwo=numberTwo
        return (numberOne-numberTwo)
    }

    fun multiplu(numberOne:Double,numberTwo:Double):Double {
        this.numberOne=numberOne
        this.numberTwo=numberTwo
        return (numberOne*numberTwo)
    }

    fun divide(numberOne:Double,numberTwo:Double):Double {
        this.numberOne=numberOne
        this.numberTwo=numberTwo
        return (numberOne/numberTwo)
    }
}