package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.*;
import com.soup.game.scene.Hand;
import com.soup.game.service.AudioService;
import com.soup.game.service.GameService;
import com.soup.game.service.ServiceFactory;

@SuppressWarnings("all")
public class GameScreen implements Screen {
    private final ServiceFactory service;
    private final Stage stage;
    private final Group group;
    private final float buttonWidth = 60f;
    private final float buttonHeight = 60f;
    private final float spacing = buttonWidth/4f;

    private GameService gameService;

    public GameScreen(ServiceFactory service, Stage stage) {
        this.service = service;
        this.stage = stage;
        this.group = new Group();

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();
                if(target instanceof Card card) {
                    service.get(AudioService.class).playFX(0);
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
        Deck deck = gameService.getTable().getDeck();
        Score score = new Score(service);
        Gdx.input.setInputProcessor(stage);

        float mainWidth = 800f;
        float mainHeight = 200f;
        float mainX = Gdx.graphics.getWidth()/2f - mainWidth/2f;
        float mainY = hand.getCardsY() - 50f;
        group.addActor(new Window(mainX, mainY, mainWidth, mainHeight));
        hand.layout(stage, false);

        for(Card c : hand.getCards()) {
            group.addActor(c);
        }

        float rectWidth = 225f;
        float rectHeight = Gdx.graphics.getHeight() - 150f;
        float rectX = 150f;
        float rectY = Gdx.graphics.getHeight()/2f - rectHeight/2f;
        Window buttonWindow = new Window(rectX, rectY, rectWidth, rectHeight);
        group.addActor(buttonWindow);

        float totalButtonWidth = buttonWidth * 2 + spacing;
        float buttonsStartX = rectX + (rectWidth - totalButtonWidth)/2f;
        float buttonsY = rectY + rectHeight/2f - buttonHeight/2f;

        group.addActor(new Button(false, buttonsStartX, buttonsY,
            buttonWidth, buttonHeight, () -> hand.setReady(true)));

        group.addActor(new Button(true, buttonsStartX + buttonWidth + spacing,
            buttonsY, buttonWidth, buttonHeight, () -> gameService.discardHand()));

        deck.setPosition(Gdx.graphics.getWidth() - 300f, 150f);
        group.addActor(deck);

        float centerX = rectX + rectWidth/2f;
        float textY = buttonsY + buttonHeight + 50f;
        score.setPosition(centerX, textY);
        group.addActor(score);

        stage.addActor(group);
        group.setPosition(0, -Gdx.graphics.getHeight());
        group.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.moveTo(0, 0, 0.8f, Interpolation.sine)
        ));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        gameService.update(delta);
        Card.updateGlobalTime(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());
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
