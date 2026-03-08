package com.soup.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.soup.game.intf.Service;

public class FontService implements Service {
    private final FreeTypeFontGenerator generator;
    private final BitmapFont small;
    private final BitmapFont medium;
    private final BitmapFont large;

    public FontService() {
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
//        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BoldPixels.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        small = generator.generateFont(params);

        params.size = 32;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        medium = generator.generateFont(params);

        params.size = 40;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        large = generator.generateFont(params);
        generator.dispose();
    }

    public BitmapFont getSmall() {
        return small;
    }

    public BitmapFont getMedium() {
        return medium;
    }

    public BitmapFont getLarge() {
        return large;
    }
}
