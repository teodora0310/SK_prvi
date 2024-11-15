package org.example
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class EXCELReportGenerator : ReportGenerator() {

    override val type: String = "EXCEL"
    override val fileName: String = generateFileName()

    // Implementacija generateReport koja sada koristi Apache POI za kreiranje Excel izveštaja
    override fun generateReport(data: List<List<String>>, options: ReportOptions) {
        // Kreirajte novu Excel radnu svesku
        val workbook = XSSFWorkbook()  // Koristi XSSFWorkbook za rad sa .xlsx formatom
        val sheet = workbook.createSheet("Izveštaj")

        // Dodajte podatke u Excel sheet
        for (i in data.indices) {
            val row = sheet.createRow(i)  // Kreirajte novi red u Excel-u
            val rowData = data[i]
            for (j in rowData.indices) {
                val cell = row.createCell(j)  // Kreirajte ćeliju za svaki podatak u redu
                cell.setCellValue(rowData[j])  // Postavite vrednost ćelije
            }
        }

        // Snimite Excel fajl sa generisanim imenom
        val outputFile = File(fileName)  // Koristite prethodno generisano ime fajla
        FileOutputStream(outputFile).use { fileOut ->
            workbook.write(fileOut)  // Zapisivanje Excel fajla
        }

        println("Excel fajl je uspešno generisan: ${outputFile.name}")
    }

    // Metoda za generisanje jedinstvenog imena fajla
    private fun generateFileName(): String {
        var counter = 1
        var file = File("ExcelReport$counter.xlsx")  // Korišćenje .xlsx ekstenzije
        while (file.exists()) {  // Proverava ako fajl već postoji, povećava broj
            counter++
            file = File("ExcelReport$counter.xlsx")
        }
        return file.name  // Vraća ime fajla sa brojem
    }
}
