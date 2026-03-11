package com.soup.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Logo;
import com.soup.game.service.ServiceFactory;

public class OpenScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final ServiceFactory service;
    private Logo logo;

    public OpenScreen(Game game, Stage stage, ServiceFactory service) {
        this.game = game;
        this.stage = stage;
        this.service = service;
    }

    @Override
    public void show() {
        logo = new Logo();
        stage.addActor(logo);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            logo.addAction(Actions.sequence(
                Actions.moveBy(0f, Gdx.graphics.getHeight(), 0.5f, Interpolation.sine),
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
