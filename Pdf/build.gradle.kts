plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":Spec"))
    implementation(project(":Kalk"))

    implementation("com.openhtmltopdf:openhtmltopdf-pdfbox:1.0.0")  // Za konverziju HTML-a u PDF
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}