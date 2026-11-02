plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("build.sourceset").toString()
            from(components["java"])
        }
    }

    repositories {
        maven {
            val repo = if (
                System.getenv("IS_SNAPSHOT") == null || System.getenv("IS_SNAPSHOT").toInt() == 0
            ) "releases" else "snapshots"

            if (System.getenv("CCM_USER") == null || System.getenv("CCM_PASS") == null) {
                throw RuntimeException("Did you forget your publication credentials?")
            }

            url = uri("https://maven.cobbled-creators.org/$repo")
            credentials {
                username = System.getenv("CCM_USER")
                password = System.getenv("CCM_PASS")
            }
        }
    }
}
