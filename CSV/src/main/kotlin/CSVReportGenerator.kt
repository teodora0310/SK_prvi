package org.example

import java.io.FileWriter

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.IOException

class CSVReportGenerator : ReportGenerator() {

    override val type: String = "CSV"
    override val fileName: String = generateFileName()

    private val calculator = Calculate()

    /**
     * Generiše jedinstveno ime za CSV fajl u formatu "CSVreportX.csv", gde X predstavlja sledeći broj.
     */
    private fun generateFileName(): String {
        var counter = 1
        var file = File("CSVreport$counter.csv")
        while (file.exists()) {
            counter++
            file = File("CSVreport$counter.csv")
        }
        return file.name
    }

    /**
     * Glavna metoda koja generiše CSV izveštaj sa opcionalnim numerisanjem redova i kalkulacijama za određene kolone.
     */
    override fun generateReport(data: List<List<String>>, options: ReportOptions) {
        // Zaglavlje se preuzima kao prvi red podataka ako je `includeHeader` postavljeno na true
        val actualData = if (options.includeHeader && data.isNotEmpty()) data.drop(1) else data

        // Primeni kalkulacije i numerisanje redova ako su definisani u opcijama
        val processedData = processData(actualData, options)

        saveCSV(processedData, if (options.includeHeader) data.first() else null)
        println("CSV izveštaj je sačuvan kao $fileName")
    }

    /**
     * Procesira podatke primenom kalkulacija i dodavanjem numerisanja redova.
     */
    private fun processData(data: List<List<String>>, options: ReportOptions): List<List<String>> {
        var processedData = data

        // Primeni kalkulacije na određene kolone ako postoje u `options`
        if (options.calculations.isNotEmpty()) {
            processedData = applyCalculations(processedData, options.calculations)
        }

        // Dodaj numerisanje redova ako je uključeno
        return if (options.includeHeader) addRowNumbers(processedData) else processedData
    }

    /**
     * Zapisuje obrađene podatke u CSV fajl koristeći Apache Commons CSV biblioteku.
     */
    private fun saveCSV(data: List<List<String>>, header: List<String>?) {
        try {
            FileWriter(fileName).use { writer ->
                val csvFormat = if (header != null) {
                    CSVFormat.DEFAULT.builder().setHeader(*header.toTypedArray()).build()
                } else {
                    CSVFormat.DEFAULT
                }
                CSVPrinter(writer, csvFormat).use { csvPrinter ->
                    data.forEach { row -> csvPrinter.printRecord(row) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
