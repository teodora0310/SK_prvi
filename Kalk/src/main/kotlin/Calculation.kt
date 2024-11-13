package org.example

interface Calculation {

    fun calculate(values: List<Double>, calculationType: Type, condition: ((Double) -> Boolean)?): Double

    fun calculateAcrossColumns(data: List<List<Double>>, calculationType: Type): Double
}