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
    runtimeOnly(project(":CSV"))
    runtimeOnly(project(":Txt"))
    runtimeOnly(project(":Pdf"))
    runtimeOnly(project(":Excel"))
    implementation(project(":Kalk"))

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}