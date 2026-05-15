rootProject.name = "learning-assistant"

// --- Module declarations ---
// Each module under modules/ is a Gradle subproject.
// Dependencies between modules are declared in their individual build.gradle.kts files.

include("modules:search-engine")
include("modules:mcp-common")
include("modules:mcp-learning-resources")
include("modules:mcp-atlassian")
include("modules:app")
include("modules:brain-models")
include("modules:mac-os")
