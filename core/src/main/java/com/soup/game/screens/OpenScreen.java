package com.soup.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Logo;
import com.soup.game.entities.FloatingText;
import com.soup.game.service.Localization;
import com.soup.game.service.ServiceFactory;

public class OpenScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final ServiceFactory service;
    private Logo logo;
    private FloatingText text;
    private Group group;

    public OpenScreen(Game game, Stage stage, ServiceFactory service) {
        this.game = game;
        this.stage = stage;
        this.service = service;
    }

    @Override
    public void show() {
        this.group = new Group();
        String start = Localization.lang.t("menu.start");
        this.logo = new Logo();
        this.text = new FloatingText(service, start);

        text.setPosition(Gdx.graphics.getWidth()/2f - text.getWidth()/2f, 150f);

        group.addActor(logo);
        group.addActor(text);
        stage.addActor(group);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            group.addAction(Actions.sequence(
                Actions.moveBy(0f, Gdx.graphics.getHeight(), 0.5f, Interpolation.sine),
                Actions.delay(0.5f),
                Actions.run(() -> game.setScreen(new GameScreen(service, stage)))
            ));
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
