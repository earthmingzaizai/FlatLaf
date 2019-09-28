/*
 * Copyright 2019 FormDev Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

version = rootProject.version

plugins {
	`java-library`
	`maven-publish`
	id( "com.jfrog.bintray" ) version "1.8.4"
}

repositories {
	jcenter()
}

dependencies {
	testImplementation( "com.miglayout:miglayout-swing:5.2" )
	testImplementation( "com.jgoodies:jgoodies-forms:1.9.0" )
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
	jar {
		archiveBaseName.set( "flatlaf" )
	}

	javadoc {
		options {
			this as StandardJavadocDocletOptions
			tags = listOf( "uiDefault", "clientProperty" )
		}
		isFailOnError = false
	}

	register( "sourcesJar", Jar::class ) {
		archiveBaseName.set( "flatlaf" )
		archiveClassifier.set( "sources" )

		from( sourceSets.main.get().allJava )
	}

	register( "javadocJar", Jar::class ) {
		archiveBaseName.set( "flatlaf" )
		archiveClassifier.set( "javadoc" )

		from( javadoc )
	}
}

publishing {
	publications {
		create<MavenPublication>( "maven" ) {
			artifactId = "flatlaf-core"
			groupId = "com.formdev.flatlaf"

			from( components["java"] )

			artifact( tasks["sourcesJar"] )
			artifact( tasks["javadocJar"] )

			pom {
				name.set( "FlatLaf" )
				description.set( "Flat Look and Feel" )
				url.set( "https://github.com/JFormDesigner/FlatLaf" )

				licenses {
					license {
						name.set( "The Apache License, Version 2.0" )
						url.set( "http://www.apache.org/licenses/LICENSE-2.0.txt" )
					}
				}

				scm {
					url.set( "https://github.com/JFormDesigner/FlatLaf" )
				}
			}
		}
	}
}

bintray {
	user = System.getenv( "BINTRAY_USER" ) ?: System.getProperty( "bintray.user" )
	key = System.getenv( "BINTRAY_KEY" ) ?: System.getProperty( "bintray.key" )

	setPublications( "maven" )

	with( pkg ) {
		repo = "flatlaf"
		name = "flatlaf-core"
		setLicenses( "Apache-2.0" )
		vcsUrl = "https://github.com/JFormDesigner/FlatLaf"

		with( version ) {
			name = project.version.toString()
		}

		publish = true
	}
}
