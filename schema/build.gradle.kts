plugins {
    `java-library`
}

val classGraphVersion : String by extra

dependencies {
    implementation(project(":annotation"))
    implementation("io.github.classgraph:classgraph:$classGraphVersion")
}