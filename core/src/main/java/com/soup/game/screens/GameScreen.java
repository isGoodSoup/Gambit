package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Card;
import com.soup.game.service.GameService;
import com.soup.game.service.ServiceFactory;

public class GameScreen implements Screen {
    private final ServiceFactory service;
    private final Stage stage;
    private GameService gameService;

    public GameScreen(ServiceFactory service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    @Override
    public void show() {
        gameService = service.get(GameService.class);
        Gdx.input.setInputProcessor(stage);

        gameService.update(0f);

        float startX = Gdx.graphics.getWidth()/2.5f;
        for(Card c : gameService.getTable().getHand().getCards()) {
            c.setPosition(startX, 150f);
            stage.addActor(c);
            startX += 48f;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        gameService.update(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
