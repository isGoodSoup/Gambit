package com.soup.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.service.ServiceFactory;

public class GameScreen implements Screen {
    private final ServiceFactory service;
    private Stage stage;

    public GameScreen(ServiceFactory service) {
        this.service = service;
    }

    @Override
    public void show() {
        stage = new Stage();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
