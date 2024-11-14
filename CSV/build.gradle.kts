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
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation(project(":Spec"))
    implementation(project(":Kalk"))

}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<Jar> {
    from("src/main/resources")
}
tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from("src/main/resources")
}
kotlin {
    jvmToolchain(21)
}