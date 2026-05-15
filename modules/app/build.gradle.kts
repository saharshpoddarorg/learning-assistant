// app: Main application entry point — wires all servers together

plugins {
    application
}

application {
    mainClass = "Main"
}

dependencies {
    implementation(project(":modules:mcp-common"))
    implementation(project(":modules:mcp-learning-resources"))
    implementation(project(":modules:mcp-atlassian"))
}
