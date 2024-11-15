package org.example


import java.io.File
import java.io.FileWriter
import java.io.IOException

class TXTReportGenerator : ReportGenerator() {

    override val type: String = "TXT"
    override val fileName: String = generateFileName()

    private val calculator = Calculate()
    override fun generateReport(data: List<List<String>>, options: ReportOptions) {

        // Naslov i zaglavlje
        val title = options.title ?: "Izveštaj"
        // Zaglavlje se preuzima kao prvi red podataka ako je `includeHeader` postavljeno na true
        val headers = if (options.includeHeader && data.isNotEmpty()) data.first() else null
        // Uzimamo samo podatke bez zaglavlja, ako je `includeHeader` postavljeno na true
        val actualData = if (options.includeHeader && data.isNotEmpty()) data.drop(1) else data

        // Primeni kalkulacije i numerisanje redova ako su definisani u opcijama
        val processedData = processData(actualData, options)

        // Sačuvaj u TXT formatu koristeći `saveTXT` metodu
        saveTXT(processedData, headers, title, options)
        println("TXT izveštaj je sačuvan kao $fileName")

    }


    private fun generateFileName(): String {
        var counter = 1
        var file = File("TXTreport$counter.txt")
        while (file.exists()) {
            counter++
            file = File("TXTreport$counter.txt")
        }
        return file.name
    }


    private fun processData(data: List<List<String>>, options: ReportOptions): List<List<String>> {
        var processedData = data

        // Primeni kalkulacije na određene kolone ako postoje u `options`
        if (options.calculations.isNotEmpty()) {
            processedData = applyCalculations(processedData, options.calculations)
        }

        // Dodaj numerisanje redova ako je uključeno
        return if (options.includeHeader) addRowNumbers(processedData) else processedData
    }


    private fun saveTXT(data: List<List<String>>, header: List<String>?, title: String, options: ReportOptions) {
        try {
            FileWriter(fileName).use { writer ->
                val columnWidths = (header ?: emptyList()).map { it.length.coerceAtLeast(10) }

                // Ispis naslova
                writer.write("${formatText(title, options.titleFormat)}\n\n")
                // Formatiranje naslova (ako postoji)
                header?.let {
                    writer.write(formatRow(it, columnWidths) + "\n")
                    writer.write("-".repeat(columnWidths.sum() + (columnWidths.size - 1) * 3) + "\n")
                }

                // Formatiranje redova podataka
                data.forEach { row ->
                    writer.write(formatRow(row, columnWidths) + "\n")
                }

                // Rezime ili kalkulacije, ako su definisani
                if (options.calculations.isNotEmpty()) {
                    writer.write("\nRezime:\n")
                    options.calculations.forEach { (label, calc) ->
                        val columnValues = getColumnValues(data, label)
                        val calcResult = calculate(columnValues, calc)
                        writer.write("${formatText("$label: $calcResult", options.summaryFormat)}\n")
                    }
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun calculate(values: List<Double>, calc: Type): Double {
        return when (calc) {
            Type.SUM -> values.sum()
            Type.AVERAGE -> values.average()
            Type.COUNT -> values.size.toDouble()
            Type.DIFFERENCE -> values.reduceOrNull { acc, value -> acc - value } ?: 0.0
            Type.MULTIPLICATION -> values.fold(1.0) { acc, value -> acc * value }
            Type.DIVISION -> values.reduceOrNull { acc, value -> if (value != 0.0) acc / value else acc } ?: 0.0
            else -> 0.0
        }
    }

    private fun formatRow(row: List<String>, columnWidths: List<Int>): String {
        return row.mapIndexed { index, value ->
            value.padEnd(columnWidths.getOrElse(index) { 10 })
        }.joinToString(" | ")
    }


    private fun formatText(text: String, format: FormattingOptions?): String {
        return format?.let {
            val boldText = if (it.bold) "**$text**" else text
            val italicText = if (it.italic) "*$boldText*" else boldText
            italicText
        } ?: text
    }


    private fun getColumnValues(data: List<List<String>>, columnIndex: Int): List<Double> {
        return data.mapNotNull { row ->
            row.getOrNull(columnIndex)?.toDoubleOrNull()
        }
    }


}
