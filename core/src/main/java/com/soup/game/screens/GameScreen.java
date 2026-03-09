package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Card;
import com.soup.game.service.GameService;
import com.soup.game.service.RenderService;
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
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        gameService.update(delta);
        stage.draw();
        stage.act(delta);

        float startX = Gdx.graphics.getWidth()/3f;

        stage.getBatch().begin();
        for(Card c : gameService.getTable().getHand().getCards()) {
            stage.getBatch().draw(service.get(RenderService.class).getRegion(c), startX, 150f);
            startX += 50f;
        }
        stage.getBatch().end();
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
