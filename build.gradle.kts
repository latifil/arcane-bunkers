import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

group = "me.latifil.bunkers"
version = "1.0"
description = "Arcane Bunkers clone."

plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }
}

tasks {
    compileJava {
        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkitPluginYaml {
    main           = "me.latifil.bunkers.Bunkers"
    load           = BukkitPluginYaml.PluginLoadOrder.POSTWORLD
    authors.add("Latifil")
    apiVersion     = "1.21"
    description    = project.description
    foliaSupported = false
    commands {
        register("team") {
            description = "The main team command"
            usage = "/team"
            aliases = listOf("t", "team")
        }
        register("teamlocation") {
            description = "Send your coords to your teammates"
            usage = "/teamlocation"
            aliases = listOf("teamlocation", "tl")
        }
    }
}
