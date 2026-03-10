package com.soup.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.soup.game.intf.Service;

public class UIAssets implements Service {
    private final BitmapFont font;
    private final BitmapFont smallFont;

    public UIAssets() {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/BoldPixels.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter params =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 64;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

        font = generator.generateFont(params);

        params.size = 32;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

        smallFont = generator.generateFont(params);
        generator.dispose();
    }

    public BitmapFont getFont() {
        return font;
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }
}
