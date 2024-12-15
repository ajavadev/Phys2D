package com.github.ajavadev.render;

// Java Standard Library Imports
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// LWJGL Core and Utilities
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

// Custom Classes
import com.github.ajavadev.util.Vector2;
import com.github.ajavadev.entities.Shape;

// LWJGL Static Imports
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A robust render engine using LWJGL for window creation and OpenGL rendering.
 * Provides basic window management, rendering loop, and error handling.
 */
public class RenderEngine implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(RenderEngine.class.getName());

    // Window configuration parameters
    private final Vector2 windowSize;
    private final String windowTitle;

    // Window handle
    private long window;

    // ArrayList for shape objects
    private final ArrayList<Shape> objectsInScene = new ArrayList<Shape>();

    /**
     * Default constructor with preset window dimensions and title.
     */
    public RenderEngine() {
        this(new Vector2(800, 600), "LWJGL Render Engine");
    }

    /**
     * Parameterized constructor for custom window configuration.
     *
     * @param windowSize The size of the window as a Vector2 object
     * @param title      The title of the window
     */
    public RenderEngine(Vector2 windowSize, String title) {
        this.windowSize = Objects.requireNonNull(windowSize, "Window size cannot be null");
        this.windowTitle = Objects.requireNonNull(title, "Window title cannot be null");
    }

    /**
     * Initializes and runs the render engine.
     * Sets up GLFW, creates window, and starts rendering loop.
     */
    public void run() {
        LOGGER.info(() -> "Initializing LWJGL Version " + Version.getVersion());
        try {
            initializeGLFW();
            createWindow();
            setupWindowCallbacks();
            centerWindow();
            startRenderLoop();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Critical error in render engine", e);
        } finally {
            close();
        }
    }

    /**
     * Initializes GLFW with error handling and configuration.
     *
     * @throws IllegalStateException if GLFW initialization fails
     */
    private void initializeGLFW() {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW. Check system compatibility.");
        }

        // Configure window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }

    /**
     * Creates the GLFW window with predefined or custom dimensions.
     *
     * @throws RuntimeException if window creation fails
     */
    private void createWindow() {
        window = glfwCreateWindow((int) windowSize.getX(), (int) windowSize.getY(), windowTitle, NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window. Check graphics driver and system capabilities.");
        }
    }

    /**
     * Sets up key callbacks and window-specific event handling.
     */
    private void setupWindowCallbacks() {
        // Escape key handler to close window
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        // Make OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);
    }

    /**
     * Centers the window on the primary monitor.
     */
    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get window size
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get primary monitor video mode
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidMode != null) {
                glfwSetWindowPos(
                        window,
                        (vidMode.width() - pWidth.get(0)) / 2,
                        (vidMode.height() - pHeight.get(0)) / 2
                );
            }
        }

        // Make window visible
        glfwShowWindow(window);
    }

    /**
     * Adds a new object to the scene.
     */
    public void addObject(Shape objectToAdd) {
        objectsInScene.add(objectToAdd);
    }


    /**
     * Starts the main rendering loop.
     */
    private void startRenderLoop() {
        // Create OpenGL capabilities
        GL.createCapabilities();

        // Set initial clear color (red in this example)
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Rendering loop
        while (!glfwWindowShouldClose(window)) {
            renderFrame();

        }
    }

    /**
     * Renders a single frame.
     * Override this method to implement custom rendering logic.
     */
    protected void renderFrame() {
        // Clear color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Swap front and back buffers
        glfwSwapBuffers(window);

        // Poll for window events
        glfwPollEvents();
    }

    /**
     * Releases GLFW resources and closes the window.
     */
    @Override
    public void close() {
        try {
            // Free window callbacks
            if (window != NULL) {
                glfwFreeCallbacks(window);
                glfwDestroyWindow(window);
            }
        } finally {
            // Terminate GLFW
            glfwTerminate();

            // Clear error callback
            GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
            if (errorCallback != null) {
                errorCallback.free();
            }
        }
    }
}
