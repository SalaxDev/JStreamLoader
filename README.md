[![License](https://img.shields.io/github/license/SalaxDev/JStreamLoader?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
# JStreamLoader 
**JStreamLoader** is a lightweight, efficient Java library for loading files and resources directly from streams. Created by **SalaxDev**, it offers fast and easy resource management for Java projects.

## Features

- **Load Images:** Load images from various locations including the program's directory or user home directory. If loading fails, a default error icon is returned.
- **Load Files:** Load text files from specified locations, including the program directory and user home.
- **Save Files:** Save files to the program's directory or the user's home directory with logging options.
- **Load and Play Audio:** Load and play `.wav` audio files from various locations, with the option to stop or restart the currently playing audio.

## Installation

To use JStreamLoader in your Java project, you can either download the `JStreamLoader.jar` file and include it in your project’s classpath, or include it via your preferred dependency management system like Maven or Gradle.

### Maven
Step 1. Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Step 2. Add the dependency
```xml
<dependency>
    <groupId>com.github.SalaxDev</groupId>
    <artifactId>JStreamLoader</artifactId>
    <version>1.0.0</version>
</dependency>
```

