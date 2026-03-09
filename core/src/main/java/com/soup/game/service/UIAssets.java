package com.soup.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.soup.game.intf.Service;

public class UIAssets implements Service {
    public static BitmapFont getFont() {
        BitmapFont font = null;
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter params =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 32;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

        font = generator.generateFont(params);
        generator.dispose();
        return font;
    }
}
