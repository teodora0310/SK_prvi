package org.example

// Opcije za formatiranje teksta


data class ReportOptions(
    val includeHeader: Boolean = false,
    val includeSummary: Boolean = false,
    val summaryCalculations: Map<String, Type> = emptyMap(), // Koristi Type iz Kalk modula
    val calculations: Map<Int, Type> = emptyMap(), // Koristi Type iz Kalk modula
    val formattingOptions: FormattingOptions? = null,

    val title: String? = null,
    val titleFormat: FormattingOptions? = null,
    val headerFormat: FormattingOptions? = null,
    val rowFormat: FormattingOptions? = null,
    val summaryFormat: FormattingOptions? = null
)

data class FormattingOptions(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val color: String? = null,
    val lineThickness: Int = 1
)


abstract class ReportGenerator {

    abstract val type: String
    abstract val fileName: String

    private val calculator = Calculate()

    /**
     * Glavna metoda za generisanje izveštaja koja koristi prosleđene opcije (zaglavlje, rezime, kalkulacije, formatiranje).
     */
    abstract fun generateReport(data: List<List<String>>, options: ReportOptions)

    /**
     * Metoda koja koristi zaglavlje ako je zadato, i generiše sadržaj izveštaja sa dodatnim kriterijumima.
     * Opcije omogućavaju podešavanje zaglavlja, rezimea, kalkulacija i formatiranja.
     */
    fun generateReport(
        data: List<List<String>>,
        header: List<String> = emptyList(),
        options: ReportOptions
    ) {
        val reportData = if (options.includeHeader && header.isNotEmpty()) {
            listOf(header) + data
        } else {
            data
        }
        generateReport(reportData, options)
    }

    /**
     * Metoda za numerisanje redova. Dodaje redni broj kao prvi element svakom redu podataka.
     */
    fun addRowNumbers(data: List<List<String>>): List<List<String>> {
        return data.mapIndexed { index, row -> listOf((index + 1).toString()) + row }
    }

    /**
     * Metoda za kreiranje rezimea, u skladu sa opcijama u `ReportOptions`. Može sadržati vrednosti ili kalkulacije.
     * Na primer, zbir, prosek, ili brojanje na osnovu uslova za određene kolone.
     */
    fun generateSummary(data: List<List<String>>, options: ReportOptions): List<Pair<String, String>> {
        val summary = mutableListOf<Pair<String, String>>()
        options.summaryCalculations.forEach { (label, calculationType) ->
            val columnValues = data.flatten().mapNotNull { it.toDoubleOrNull() } // Pretpostavlja da su svi podaci numerički
            val result = when (calculationType) {
                Type.SUM, Type.AVERAGE, Type.COUNT -> calculator.calculate(columnValues, calculationType, null)
                Type.DIFFERENCE, Type.MULTIPLICATION, Type.DIVISION -> calculator.calculateAcrossColumns(listOf(columnValues), calculationType)
            }
            summary.add(label to result.toString())
        }
        return summary
    }


    /**
     * Metoda za obradu kalkulacija za specifične kolone. Koristi se prilikom generisanja izveštaja,
     * i vrednosti za kolone sa kalkulacijama se generišu u toku izvršavanja.
     */
    fun applyCalculations(data: List<List<String>>, calculations: Map<Int, Type>): List<List<String>> {
        return data.map { row ->
            row.mapIndexed { index, value ->
                if (calculations.containsKey(index)) {
                    val calculationType = calculations[index]!!
                    val numericValues = data.mapNotNull { it.getOrNull(index)?.toDoubleOrNull() }
                    calculator.calculate(numericValues, calculationType, null).toString()
                } else {
                    value
                }
            }
        }
    }
}
