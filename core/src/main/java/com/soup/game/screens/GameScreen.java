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

import java.util.List;

public class GameScreen implements Screen {
    private final ServiceFactory service;
    private final Stage stage;
    private GameService gameService;

    public GameScreen(ServiceFactory service, Stage stage) {
        this.service = service;
        this.stage = stage;

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();
                if(target instanceof Card card) {
                    gameService.getTable().getHand().select(card);
                }
            }
        });
    }

    @Override
    public void show() {
        gameService = service.get(GameService.class);
        gameService.update(0f);
        Hand hand = gameService.getTable().getHand();
        List<Card> snapshot = List.copyOf(hand.getCards());
        Gdx.input.setInputProcessor(stage);

        float handY = 150f;
        float overlapFactor = 0.6f;
        int numCards = snapshot.size();
        if(numCards == 0) {
            return;
        }

        float cardWidth = snapshot.getFirst().getCardWidth();
        float spacing = cardWidth * overlapFactor;

        float totalWidth = spacing * (numCards - 1) + cardWidth;

        float maxHandWidth = Gdx.graphics.getWidth() * 0.9f;
        if(totalWidth > maxHandWidth) {
            spacing = (maxHandWidth - cardWidth) / (numCards - 1);
            totalWidth = maxHandWidth;
        }

        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        for(int i = 0; i < numCards; i++) {
            Card c = snapshot.get(i);
            c.setPosition(startX + i * spacing, handY);
            stage.addActor(c);
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
