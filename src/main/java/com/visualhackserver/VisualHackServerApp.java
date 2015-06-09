package com.visualhackserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;

import com.visualhackserver.ui.VisualHackServerView;

/**
 * The main class of the application.
 */
public class VisualHackServerApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        try {
            show(new VisualHackServerView(this));
        } catch (IOException ex) {
            Logger.getLogger(VisualHackServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable()  {
            public void run() {
                SubstanceLookAndFeel.setSkin(new GraphiteGlassSkin());
            }
        });
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of VisualHackServerApp
     */
    public static VisualHackServerApp getApplication() {
        return Application.getInstance(VisualHackServerApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(VisualHackServerApp.class, args);
    }
}
