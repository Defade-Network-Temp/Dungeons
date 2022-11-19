plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.defade"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.defade.net/defade") {
        name = "defade"
        credentials(PasswordCredentials::class)
    }
}

dependencies {
    compileOnly("net.defade:Yokura:1.19.2-R1.1-SNAPSHOT")
    api("org.json:json:20220924")
}