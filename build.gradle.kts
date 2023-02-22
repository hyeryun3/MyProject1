plugins {
    java
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
    implementation("io.jsonwebtoken:jjwt:0.9.1")
// https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20220924")
// https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt:4.2.1")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    compileOnly("org.projectlombok:lombok:1.18.10")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
//     https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
//    implementation("com.mysql:mysql-connector-j:8.0.32")

    implementation("org.mybatis:mybatis-spring:2.0.6")
    implementation("org.mybatis:mybatis:3.5.6")



    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // https://mvnrepository.com/artifact/com.sun.mail/javax.mail
    implementation("com.sun.mail:javax.mail:1.6.2")
    // https://mvnrepository.com/artifact/javax.mail/javax.mail-api
    implementation("javax.mail:javax.mail-api:1.6.2")


}

tasks.withType<Test> {
    useJUnitPlatform()
}
