plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.github.codemonstur:embedded-redis:1.0.0")
}