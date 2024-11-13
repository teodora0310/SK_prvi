package org.example

class Calculate : Calculation {
    override fun calculate(values: List<Double>, calculationType: Type, condition: ((Double) -> Boolean)?): Double {
        return when (calculationType) {
            Type.SUM -> values.sum()
            Type.AVERAGE -> if (values.isNotEmpty()) values.average() else 0.0
            Type.COUNT -> if (condition != null) values.count(condition).toDouble() else values.size.toDouble()
            Type.DIFFERENCE -> if (values.size >= 2) values[0] - values[1] else 0.0
            Type.MULTIPLICATION -> values.fold(1.0) { acc, num -> acc * num }
            Type.DIVISION -> if (values.size >= 2 && values[1] != 0.0) values[0] / values[1] else 0.0
        }
    }

    /**
     * Metoda za primenu kalkulacija na više kolona.
     * Zbir i množenje se primenjuju na sve vrednosti u prosleđenim kolonama.
     * Razlika i deljenje se primenjuju samo na prve dve vrednosti.
     */
    override fun calculateAcrossColumns(data: List<List<Double>>, calculationType: Type): Double {
        val flattenedValues = data.flatten()
        return when (calculationType) {
            Type.SUM -> flattenedValues.sum()
            Type.MULTIPLICATION -> flattenedValues.fold(1.0) { acc, num -> acc * num }
            Type.DIFFERENCE -> if (flattenedValues.size >= 2) flattenedValues[0] - flattenedValues[1] else 0.0
            Type.DIVISION -> if (flattenedValues.size >= 2 && flattenedValues[1] != 0.0) flattenedValues[0] / flattenedValues[1] else 0.0
            else -> 0.0
        }
    }
}
