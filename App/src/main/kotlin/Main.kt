package org.example

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.ServiceLoader
import kotlin.system.exitProcess

fun main() {

//    fun getConnection(): Connection {
//        val url = "jdbc:h2:file:./database/testdb2;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE"
//        return DriverManager.getConnection(url,  "user", "password")
//    }

//    val connection = getConnection()
//    initializeDatabase(connection)

    val serviceLoader = ServiceLoader.load(ReportGenerator::class.java)
    val reportServices = mutableMapOf<String, ReportGenerator>()

    serviceLoader.forEach { service ->
        reportServices[service.type] = service
    }

    if (reportServices.isEmpty()) {
        println("Nema dostupnih implementacija za generisanje izveštaja.")
        exitProcess(1)
    }

    println("Dostupni formati izveštaja:")
    reportServices.keys.forEach { println(it) }

    print("Izaberite format izveštaja: ")
    val selectedFormat = readLine()?.trim()
    val reportGenerator = reportServices[selectedFormat]

    if (reportGenerator == null) {
        println("Nepoznat format izveštaja.")
        exitProcess(1)
    }

    fun loadDataFromFile(fileName: String): List<List<String>> {
        val file = File(fileName)
        return file.readLines().map { line ->
            line.split(",").map { it.trim() }
        }
    }

    val data = loadDataFromFile("App/src/main/kotlin/data.txt")


    val reportType = ReportType.valueOf(selectedFormat!!.uppercase())
    val config = reportConfigurations[reportType] ?: error("Konfiguracija nije pronađena za izabrani format")

    // SQL upit od korisnika
    //val query = getUserQuery()


    //val data = executeQuery(connection, query)

    // Prikaz dostupnih opcija
    val includeHeader = if (config.supportsHeader) {
        println("Želite li da uključite zaglavlje (da/ne)?")
        readLine()?.equals("da", ignoreCase = true) == true
    } else false

    val title = if (config.supportsTitle) {
        println("Unesite naslov izveštaja:")
        readLine() ?: ""
    } else null

    val titleFormat = if (config.supportsFormatting && config.supportsTitle) {
        println("Da li želite da naslov bude podebljan (da/ne)?")
        val bold = readLine()?.equals("da", ignoreCase = true) == true
        println("Želite li da naslov bude u italic stilu (da/ne)?")
        val italic = readLine()?.equals("da", ignoreCase = true) == true
        println("Izaberite boju naslova (R/G/B):")
        val color = readLine()

        FormattingOptions(bold = bold, italic = italic, color = color)
    } else null

    val headerFormat = if (config.supportsFormatting) {
        println("Da li želite da zaglavlje bude podebljano (da/ne)?")
        val bold = readLine()?.equals("da", ignoreCase = true) == true
        FormattingOptions(bold = bold)
    } else null

    val summaryFormat = if (config.supportsSummary && config.supportsFormatting) {
        println("Da li želite da rezime bude u italic stilu (da/ne)?")
        val italic = readLine()?.equals("da", ignoreCase = true) == true
        println("Izaberite boju rezimea (R/G/B):")
        val color = readLine()

        FormattingOptions(italic = italic, color = color)
    } else null

    // Kreiranje opcija za izveštaj
    val options = ReportOptions(
        includeHeader = includeHeader,
        title = title,
        titleFormat = titleFormat,
        headerFormat = headerFormat,
        summaryFormat = summaryFormat
    )



    reportGenerator.generateReport(data, options)
    //connection.close() // Zatvaranje konekcije sa bazom
}

//fun getUserQuery(): String {
//    println("Unesite SQL upit (npr., SELECT * FROM employees):")
//    return readLine() ?: ""
//}

//fun executeQuery(connection: Connection, query: String): List<List<String>> {
//    val statement = connection.createStatement()
//    val resultSet = statement.executeQuery(query)
//    val metaData = resultSet.metaData
//    val columnCount = metaData.columnCount
//
//    val data = mutableListOf<List<String>>()
//
//    // Dodavanje imena kolona kao zaglavlja
//    val header = mutableListOf<String>()
//    for (i in 1..columnCount) {
//        header.add(metaData.getColumnName(i))
//    }
//    data.add(header) // Prvi red u data listi će biti zaglavlje
//
//
//    while (resultSet.next()) {
//        val row = mutableListOf<String>()
//        for (i in 1..columnCount) {
//            row.add(resultSet.getString(i) ?: "")
//        }
//        data.add(row)
//    }
//
//    resultSet.close()
//    statement.close()
//    return data
//}
