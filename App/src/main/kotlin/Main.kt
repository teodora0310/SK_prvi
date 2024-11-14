package org.example

import java.io.File
import java.util.ServiceLoader
import kotlin.system.exitProcess

fun main() {
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

    // Učitajte podatke iz fajla koji će predstavljati "bazu podataka"
    val data = loadDataFromFile("App/src/main/kotlin/data.txt") // Pretpostavimo da imamo fajl `data.txt` sa podacima

    // Unesite dodatne opcije za generisanje izveštaja
    println("Želite li da uključite zaglavlje (da/ne)?")
    val includeHeader = readLine()?.equals("da", ignoreCase = true) == true

    // Kreirajte opcije za generisanje izveštaja
    val options = ReportOptions(
        includeHeader = includeHeader,
        calculations = mapOf(2 to Type.SUM) // Primer: suma za treću kolonu
    )

    // Pokrenite generisanje izveštaja
    reportGenerator.generateReport(data, options)
}

fun loadDataFromFile(fileName: String): List<List<String>> {
    val file = File(fileName)
    return file.readLines().map { line ->
        line.split(",").map { it.trim() }
    }
}
