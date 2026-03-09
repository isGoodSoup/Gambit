package com.soup.game.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.soup.game.main.GambitGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        new Lwjgl3Application(new GambitGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        config.setTitle("Gambit v0.2");
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        config.setWindowedMode(displayMode.width, displayMode.height);
        config.setDecorated(false);
        config.setWindowPosition(0, 0);
        config.useVsync(true);
        config.setWindowIcon("logo_altx128.png", "logo_altx64.png", "logo_altx32.png");
        return config;
    }
}
