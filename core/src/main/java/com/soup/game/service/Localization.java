package com.soup.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.soup.game.intf.Service;

import java.util.Locale;

public class Localization implements Service {
    public static Localization lang =
        new Localization(Locale.forLanguageTag("en"));
    private I18NBundle bundle;
    private Locale locale;

    public Localization(Locale locale) {
        setLocale(locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        FileHandle baseFile = Gdx.files.internal("i18n/lang");
        bundle = I18NBundle.createBundle(baseFile, locale, "ISO-8859-1");
    }

    public String t(String key) {
        try {
            return bundle.get(key);
        } catch (Exception e) {
            return key;
        }
    }

    public String f(String key, Object... args) {
        try {
            return bundle.format(key, args);
        } catch (Exception e) {
            return key;
        }
    }

    public Locale getLocale() {
        return locale;
    }
}
