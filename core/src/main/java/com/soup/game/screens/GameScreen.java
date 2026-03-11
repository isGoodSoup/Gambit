package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.Button;
import com.soup.game.entities.Card;
import com.soup.game.entities.Window;
import com.soup.game.scene.Hand;
import com.soup.game.service.*;

@SuppressWarnings("all")
public class GameScreen implements Screen {
    private final ServiceFactory service;
    private final Stage stage;
    private final float buttonWidth = 60f;
    private final float buttonHeight = 60f;
    private final float spacing = buttonWidth/4f;

    private GameService gameService;

    public GameScreen(ServiceFactory service, Stage stage) {
        this.service = service;
        this.stage = stage;

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
        Gdx.input.setInputProcessor(stage);

        float mainWidth = 800f;
        float mainHeight = 200f;
        float mainX = Gdx.graphics.getWidth()/2f - mainWidth/2f;
        float mainY = hand.getCardsY() - 50f;
        stage.addActor(new Window(mainX, mainY, mainWidth, mainHeight));
        hand.layout(stage);

        float rectWidth = 225f;
        float rectHeight = Gdx.graphics.getHeight() - 150f;
        float rectX = 150f;
        float rectY = Gdx.graphics.getHeight()/2f - rectHeight/2f;
        Window buttonWindow = new Window(rectX, rectY, rectWidth, rectHeight);
        stage.addActor(buttonWindow);

        float totalButtonWidth = buttonWidth * 2 + spacing;
        float buttonsStartX = rectX + (rectWidth - totalButtonWidth)/2f;
        float buttonsY = rectY + rectHeight/2f - buttonHeight/2f;

        stage.addActor(new Button(false, buttonsStartX, buttonsY,
            buttonWidth, buttonHeight, () -> hand.setReady(true)));

        stage.addActor(new Button(true, buttonsStartX + buttonWidth + spacing,
            buttonsY, buttonWidth, buttonHeight, () -> gameService.discardHand()));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        gameService.update(delta);
        Card.updateGlobalTime(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        updateScore();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateScore() {
        BitmapFont font = service.get(UIAssets.class).getFont();
        String score = String.valueOf((int) gameService.getTable().getScore());
        GlyphLayout layout = new GlyphLayout(font, score);

        stage.getBatch().begin();

        float rectWidth = 225f;
        float rectX = 150f;
        float rectHeight = Gdx.graphics.getHeight() - 150f;
        float rectY = Gdx.graphics.getHeight()/2f - rectHeight/2f;

        float centerX = rectX + rectWidth/2f;
        float textX = centerX - layout.width/2f;

        float buttonsY = rectY + rectHeight/2f - buttonHeight/2f;
        float textY = buttonsY + buttonHeight + 50f;

        font.draw(stage.getBatch(), layout, textX, textY);
        service.get(RenderService.class).drawBack(stage);
        stage.getBatch().end();
    }
}
