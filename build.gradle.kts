@file:Suppress("PropertyName") // Linter suppression for underscores in property names.

plugins {
    java
}

val mod_org_id: String by project
val mod_version: String by project
val mod_url: String by project
val mod_org: String by project
val mod_name: String by project
val starmade_root: String by project

group = mod_org_id
version = mod_version

// Still requires java 8 to build, because newer gradle versions require Java 8!
java.sourceCompatibility = JavaVersion.VERSION_1_7
java.targetCompatibility = JavaVersion.VERSION_1_7

repositories {
    mavenCentral()
}

dependencies {
    // Uncomment these and the test task at the bottom to enable unit tests. Heavily recommended for mods with data storage.
    // Make sure to supply invalid values and other garbage in your tests, not just valid stuff, or you're not doing it right.
    // https://www.jetbrains.com/help/idea/junit.html - Easy enough to follow tutorial.
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    // testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    implementation(files(starmade_root + "StarMade.jar"))
    implementation(fileTree(mapOf("dir" to starmade_root + "lib", "include" to listOf("*.jar"))))
}

tasks.withType<Jar> {
    manifest {
        attributes["Built-By"] = System.getProperty("user.name")
        attributes["url"] = mod_url
        attributes["Implementation-Title"] = mod_name
        attributes["Implementation-Version"] = mod_version
        attributes["Implementation-Vendor-Id"] = mod_org_id
        attributes["Implementation-Vendor"] = mod_org
    }
}

tasks.register<JavaExec>("runGame") {
    dependsOn(tasks.getByName<Jar>("jar"))
    group = "Game"
    description = "Run the game with the mod injected for debugging."
    classpath = sourceSets["main"].compileClasspath
    mainClass.set("me.jakev.starloader.LaunchClassLoader")
    systemProperty("java.library.path", starmade_root + "native/" + starmade_root + "native/windows/x64/" + starmade_root + "native/solaris/x64")
    //noinspection GroovyAssignabilityCheck
    args = listOf("-client", "-force")
    doFirst {
        //noinspection GroovyAssignabilityCheck
        workingDir = File(starmade_root)
        val jarFile = tasks.getByName<Jar>("jar").archiveFile.get().asFile
        copy {
            from(jarFile.parent)
            into(starmade_root + "mods")

            include(jarFile.name)
        }
    }
}

// See the note inside dependencies.
//tasks.withType<Test> {
//    useJUnitPlatform()
//}
