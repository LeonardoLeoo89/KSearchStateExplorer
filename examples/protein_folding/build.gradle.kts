plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("com.leopr.protein_folding.MainKt")
}

dependencies {
    implementation(project(":framework"))
}
