rootProject.name = "practice-8-microservices"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

include(
    "api-gateway",
    "auth-service",
    "commons",
    "config-server",
    "driver-service",
    "eureka-server",
    "team-service",
    "race-service"
)
