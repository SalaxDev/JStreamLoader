/*
 * Copyright 2024 Amin Salaxdev (salaxdev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.salaxdev.stloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * A utility class for loading various types of resources, including images, files, and audio.
 * Provides options for loading resources from the program's location, user home, or custom paths.
 * Also includes developer options for debugging.
 *
 * @author Salaxdev
 * @version 1.0
 */

public class JStreamLoader {
    private static final Logger logger = LoggerFactory.getLogger(JStreamLoader.class);
    private static final String DEFAULT_ERROR_ICON_PATH = "/com/icon/error-icon.png"; // Default Icon if Image Failed To Load.
    private static boolean developerOption = false;
    private static Clip currentClip = null;

    /**
     * Constructs a new JStreamLoader with the specified developer option.
     *
     * @param developerOption If true, enables additional logging for debugging purposes.
     */

    public JStreamLoader(boolean developerOption) {
        JStreamLoader.developerOption = developerOption;
    }

    /**
     * Default constructor for JStreamLoader.
     */
    public JStreamLoader() {
        // Default constructor
    }

    /**
     * Loads an image from the specified location. If the image cannot be loaded, a default error icon will be returned.
     *
     * @param location The location (path) of the image to load.
     * @return A BufferedImage representing the loaded image.
     * @throws ResourceNotFoundException If the image cannot be loaded and the developer option is not enabled.
     */
    public BufferedImage loadImageStream(String location) {
        BufferedImage image = null;
        try {
            return image = loadImageInternal(location);
        } catch (NullPointerException | IOException e) {
            logger.info("Failed to load image at '{}'. Attempting to load default error icon.", location);
            try {
                return image = loadImageInternal(DEFAULT_ERROR_ICON_PATH);
            } catch (NullPointerException | IOException ex) {
                if (developerOption) {
                    logger.info("Developer option is enabled [{}]. Returning null as the image failed to load.", developerOption);
                    return image;
                }
                throw new ResourceNotFoundException(errorFormatLoader("Image", location), e);
            }
        }
    }

    /**
     * Loads a file as a BufferedReader from the given location.
     *
     * @param location The location (path) of the file to load.
     * @return A BufferedReader for reading the file's contents.
     * @throws ResourceNotFoundException If the file cannot be found and the developer option is not enabled.
     */
    public BufferedReader loadFile(String location) {
        try {
            return new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(getClass().getResourceAsStream(location))
                    )
            );
        } catch (Exception e) {
            if (developerOption) {
                logger.info("Developer Options Is On, File Failed To Load at {}, Returning [Null]", location);
                return null;
            }
            throw new ResourceNotFoundException(errorFormatLoader("File", location), e);
        }
    }

    /**
     * Saves content to a file in the program's directory.
     *
     * @param location The path where the file should be saved.
     * @param content The content to write to the file.
     * @param name The name of the file.
     * @param noticeCreation If true, logs a message when the directory is created.
     */
    public void programLocationSaveFile(String location, String content, String name, boolean noticeCreation) {
        saveCreate(location, content, name, noticeCreation, "user.dir");
    }

    /**
     * Loads a saved file from the program's directory.
     *
     * @param name The name of the file to load.
     * @param location The path where the file is located.
     * @return The content of the saved file as a String.
     * @throws ResourceNotFoundException If the file cannot be found or loaded.
     */
    public String programLocationLoadSaveFile(String name, String location) {
        return loadSave(name, location, "user.dir");
    }
    /**
     * Saves the specified content to a file in the user's home directory.
     *
     * @param location       The relative path where the file should be saved within the user's home directory.
     * @param content        The content to be written to the file.
     * @param name           The name of the file to be created or overwritten.
     * @param noticeCreation If {@code true}, logs a message indicating whether the directory was successfully created.
     *
     * @throws ResourceNotFoundException if the file could not be saved and developerOption is disabled.
     */
    public void userHomeLocationSaveFile(String location, String content, String name, boolean noticeCreation) {
        saveCreate(location, content, name, noticeCreation, "user.home");
    }
    /**
     * Loads and returns the contents of a file from the user's home directory.
     *
     * @param name     The name of the file to be loaded.
     * @param location The relative path of the directory within the user's home directory where the file is located.
     *
     * @return The content of the file as a {@code String}.
     *
     * @throws ResourceNotFoundException if the file does not exist or an error occurs during reading,
     *         unless developerOption is enabled, in which case it returns {@code null}.
     */
    public String userHomeLocationLoadSaveFile(String name, String location) {
        return loadSave(name, location, "user.home");
    }

    /**
     * Loads and plays a .wav audio file from the specified location.
     *
     * @param location The location (path) of the .wav audio file to load and play.
     */
    public void loadStreamWawMusic(String location) {
        musicWavLoader(location, null, true);
    }
    /**
     * Loads and plays a .wav audio file from the program's directory.
     *
     * @param location The location (path) of the .wav audio file to load and play.
     */
    public void loadProgramLocationWawMusic(String location) {
        musicWavLoader(location, "user.dir", false);
    }
    /**
     * Loads and plays a .wav audio file from the user's home directory.
     *
     * @param location The location (path) of the .wav audio file to load and play.
     */
    public void loadUserHomeLocationWawMusic(String location) {
        musicWavLoader(location, "user.home", false);
    }

    // Private helper methods


    /**
     * Loads and returns the content of a save file from a specific location.
     *
     * @param name The name of the save file.
     * @param location The location where the save file is located.
     * @param userLocation The system property to use for locating the save file.
     * @return The content of the save file as a String.
     */
    private String loadSave(String name, String location, String userLocation) {
        File saveFile = new File(System.getProperty(userLocation), new File(location, name).getPath());
        if (!saveFile.exists()) {
            throw new ResourceNotFoundException("File Not Found at '" + saveFile.getAbsolutePath() + "'", null);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            if (developerOption) {
                logger.info("Developer Option Is On, Failed To Load File at {}", saveFile.getAbsolutePath());
                return null;
            }
            throw new ResourceNotFoundException(errorFormatLoader("File", saveFile.getAbsolutePath()), e);
        }
    }

    /**
     * Saves content to a file at a specific location, creating directories if necessary.
     *
     * @param location The path where the file should be saved.
     * @param content The content to write to the file.
     * @param name The name of the file.
     * @param noticeCreation If true, logs a message when the directory is created.
     * @param userLocation The system property to use for locating the save file.
     */
    private void saveCreate(String location, String content, String name, boolean noticeCreation, String userLocation) {
        File directory = new File(System.getProperty(userLocation), location);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (noticeCreation) {
                if (created) {
                    logger.info("Directory was Successfully Created at '{}'", directory.getAbsolutePath());
                } else {
                    logger.warn("Failed To Create Directory at '{}', check file permissions or path issues.", directory.getAbsolutePath());
                }
            }
        }

        File saveFile = new File(directory, name);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(content);
        } catch (IOException e) {
            if (developerOption) {
                logger.info("Developer Option Is On, Failed To Save File at {}", location);
                return;
            }
            throw new ResourceNotFoundException(errorFormatLoader("File", saveFile.getAbsolutePath()), e);
        }
    }

    /**
     * Loads an image internally using the specified location.
     *
     * @param location The location (path) of the image to load.
     * @return A BufferedImage representing the loaded image.
     * @throws IOException If an error occurs while loading the image.
     * @throws NullPointerException If the image location is null or invalid.
     */
    private BufferedImage loadImageInternal(String location) throws IOException, NullPointerException {
        return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(location)));
    }

    /**
     * Formats an error message for resource loading errors.
     *
     * @param type The type of resource (e.g., "Image", "File", "Music").
     * @param location The location of the resource.
     * @return A formatted error message.
     */
    private String errorFormatLoader(String type, String location) {
        return String.format("Failed to load or save %s: %s", type, location);
    }

    /**
     * Loads and plays a .wav audio file from the specified location.
     *
     * @param location The location (path) of the .wav audio file.
     * @param userLocation The system property for determining the resource location.
     * @param isStreamStored If true, loads the resource as a stream from the classpath.
     */
    private void musicWavLoader(String location, String userLocation, boolean isStreamStored) {
        BufferedInputStream musicFileStream;
        AudioInputStream audioStream = null;
        File musicFile;

        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.start();
        }

        if (isStreamStored) {
            musicFileStream = new BufferedInputStream(
                    getClass().getResourceAsStream(location)
            );
            if (musicFileStream == null) {
                if (developerOption) {
                    logger.warn("Developer Option is On, Failed To load music at {}", location);
                    return;
                }
                throw new ResourceNotFoundException(errorFormatLoader("Music", location), null);
            }
            try {
                audioStream = AudioSystem.getAudioInputStream(musicFileStream);
            } catch (IOException | UnsupportedAudioFileException e) {
                logger.error("Failed to load music at {}", location, e);
            }
        } else {
            if (userLocation == null) {
                if (developerOption) {
                    logger.warn("Developer Option is On. userLocation is null");
                    return;
                }
                throw new NullPointerException("userLocation is null, can't Load music.");
            }
            musicFile = new File(System.getProperty(userLocation), location);
            if (!musicFile.exists()) {
                if (developerOption) {
                    logger.warn("Developer Option is On. Failed To Load Music at {}", musicFile.getAbsolutePath());
                    return;
                }
                throw new ResourceNotFoundException(errorFormatLoader("Music", musicFile.getAbsolutePath()), null);
            }
            try {
                audioStream = AudioSystem.getAudioInputStream(musicFile);
            } catch (IOException | UnsupportedAudioFileException e) {
                logger.error("Failed To Load Audio at {}", musicFile.getAbsolutePath(), e);
            }
        }

        try {
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.start();
        } catch (LineUnavailableException | IOException e) {
            logger.error("Failed To Play music", e);
        }
    }

    /**
     * Stops the currently playing .wav audio, if any.
     */
    private void stopWavMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            logger.info("music stopped");
        } else {
            logger.info("No Music is currently plaing.");
        }
    }
}

class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

