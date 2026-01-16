# HDiscordSRV - Hytale DiscordSRV-like Mod

This is a Hytale server plugin project that implements two-way chat synchronization between Hytale and Discord.

## Prerequisites

- **Java 25** or later.
- **HytaleServer.jar** (must be added to your local Maven repository or as a library in your IDE).

## Project Structure

- `src/main/java`: Plugin source code.
  - `com.livesgood.ExamplePlugin`: Main plugin class.
  - `com.livesgood.commands`: Custom command implementations.
  - `com.livesgood.events`: Custom event listeners.
- `src/main/resources`: Plugin assets and configuration.
  - `manifest.json`: Plugin metadata.

## Setup Instructions

1. **Java Version**: The project is configured to use Java 25.
2. **Dependencies**:
   - Download the `HytaleServer.jar`.
   - To build with Maven, you should install the JAR to your local repository:
     ```bash
     mvn install:install-file -Dfile=path/to/HytaleServer.jar -DgroupId=com.hypixel.hytale -DartifactId=HytaleServer-parent -Dversion=1.0-SNAPSHOT -Dpackaging=jar
     ```
3. **Build**:
   - Run `mvn package` to build the plugin JAR.

