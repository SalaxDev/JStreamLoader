# JStreamLoader

JStreamLoader is a utility library for Java that simplifies the process of loading various resources, such as images, text files, and audio files, from different locations in your application. It also includes developer options for debugging and enhanced logging.

## Features

- **Load Images:** Load images from various locations including the program's directory or user home directory. If loading fails, a default error icon is returned.
- **Load Files:** Load text files from specified locations, including the program directory and user home.
- **Save Files:** Save files to the program's directory or the user's home directory with logging options.
- **Load and Play Audio:** Load and play `.wav` audio files from various locations, with the option to stop or restart the currently playing audio.

## Installation

To use JStreamLoader in your Java project, you can either download the `JStreamLoader.jar` file and include it in your project’s classpath, or include it via your preferred dependency management system like Maven or Gradle.

### Maven
Add this dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
Step 2. Add the dependency
<dependency>
    <groupId>com.github.SalaxDev</groupId>
    <artifactId>JStreamLoader</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
