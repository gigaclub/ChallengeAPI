plugins {
    `java-library`
    `maven-publish`
}

group = "net.gigaclub"
version = "1.0.2"

val myArtifactId: String = rootProject.name
val myArtifactGroup: String = project.group.toString()
val myArtifactVersion: String = project.version.toString()

val myGithubUsername = "GigaClub"
val myGithubHttpUrl = "https://github.com/${myGithubUsername}/${myArtifactId}"
val myGithubIssueTrackerUrl = "https://github.com/${myGithubUsername}/${myArtifactId}/issues"
val myLicense = "MIT"
val myLicenseUrl = "https://opensource.org/licenses/MIT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_16
}

repositories {
    mavenCentral()
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${myGithubUsername}/${myArtifactId}")
            credentials {
                username = System.getenv("GITHUB_PACKAGES_USERID")
                password = System.getenv("GITHUB_PACKAGES_IMPORT_TOKEN")
            }
        }
    }
}

publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = myArtifactGroup
            artifactId = myArtifactId
            version = myArtifactVersion

            from(components["java"])

            artifact(sourcesJar)

            pom {
                packaging = "jar"
                name.set(myArtifactId)
                url.set(myGithubHttpUrl)
                scm {
                    url.set(myGithubHttpUrl)
                }
                issueManagement {
                    url.set(myGithubIssueTrackerUrl)
                }
                licenses {
                    license {
                        name.set(myLicense)
                        url.set(myLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(myGithubUsername)
                    }
                }
            }
        }
    }
}
