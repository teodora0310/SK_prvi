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
    //termin 4 i dop
    //5-5 fat h


    override fun generateReport(data: List<List<String>>, options: ReportOptions) {

        val htmlReport = generateHtmlReport(data, options)


        val pdfBytes = convertHtmlToPdf(htmlReport)


        savePdfToFile(pdfBytes, "izvestaj.pdf")
    }

    private fun generateHtmlReport(data: List<List<String>>, options: ReportOptions): String {
        val sb = StringBuilder()

        // HTML start and styles
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

        // Add title if it exists
        if (!options.title.isNullOrEmpty()) {
            sb.append("<h1>${options.title}</h1>")
        }

        // Start table
        sb.append("<table>")

        // Add header if included in options
        if (options.includeHeader && data.isNotEmpty()) {
            sb.append("<tr>")
            data.first().forEach { sb.append("<th>$it</th>") }
            sb.append("</tr>")
        }

        // Add data rows, skipping header if already added
        val rows = if (options.includeHeader) data.drop(1) else data
        rows.forEach { row ->
            sb.append("<tr>")
            row.forEach { sb.append("<td>$it</td>") }
            sb.append("</tr>")
        }

        // Close table and body
        sb.append("</table>")
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

