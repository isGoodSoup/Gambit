package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Card;
import com.soup.game.scene.Hand;
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
        Hand hand = gameService.getTable().getHand();
        Gdx.input.setInputProcessor(stage);

        gameService.update(0f);

        float startX = Gdx.graphics.getWidth()/2f;
        float handY = 150f;

        for(Card c : hand.getCards()) {
            c.setPosition(startX, handY);
            stage.addActor(c);
            startX += 48f;
        }

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();
                if(target instanceof Card card) {
                    hand.select(card);
                }
            }
        });

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
