package org.example

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class PDFReportGenerator : ReportGenerator() {

    override val type: String = "PDF"
    override val fileName: String = generateFileName()
    private val calculator = Calculate()


    override fun generateReport(data: List<List<String>>, options: ReportOptions) {
        // Generisanje HTML-a za izveštaj
        val htmlReport = generateHtmlReport(data, options)

        // Konvertovanje HTML-a u PDF
        val pdfBytes = convertHtmlToPdf(htmlReport)

        // Čuvanje PDF-a u fajl
        savePdfToFile(pdfBytes, "izvestaj.pdf")
    }

    private fun generateHtmlReport(data: List<List<String>>, options: ReportOptions): String {
        // Kreiranje osnovne HTML strukture
        val sb = StringBuilder()

        // Početak HTML-a
        sb.append("<html><head><style>")
        sb.append("table { width: 100%; border-collapse: collapse; }")
        sb.append("table, th, td { border: 1px solid black; }")
        sb.append("th { text-align: left; padding: 8px; }")
        sb.append("td { text-align: left; padding: 8px; }")
        if (options.titleFormat?.bold == true) {
            sb.append("h1 { font-weight: bold; }")
        }
        if (options.titleFormat?.italic == true) {
            sb.append("h1 { font-style: italic; }")
        }
        sb.append("</style></head><body>")

        // Dodavanje naslova
        sb.append("<h1>${options.title}</h1>")

        // Dodavanje tabele sa podacima
        sb.append("<table>")
        sb.append("<tr>")
        data.first().forEach { sb.append("<th>$it</th>") }  // Dodavanje zaglavlja
        sb.append("</tr>")

        data.forEach { row ->
            sb.append("<tr>")
            row.forEach { sb.append("<td>$it</td>") }
            sb.append("</tr>")
        }

        sb.append("</table>")

        // Kraj HTML-a
        sb.append("</body></html>")

        return sb.toString()
    }

    private fun convertHtmlToPdf(html: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        PdfRendererBuilder()
            .useFastMode()
            .withHtmlContent(html, null)
            .toStream(outputStream)
            .run()
        return outputStream.toByteArray()
    }

    private fun savePdfToFile(pdfBytes: ByteArray, fileName: String) {
        val path = Paths.get(fileName)
        Files.write(path, pdfBytes)
        println("PDF fajl je uspešno generisan: $fileName")
    }

    private fun generateFileName(): String {
        var counter = 1
        var file = File("PDFreport$counter.pdf")
        while (file.exists()) {
            counter++
            file = File("PDFreport$counter.pdf")
        }
        return file.name
    }





}

