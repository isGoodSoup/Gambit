package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

@SuppressWarnings("all")
public class Card extends Actor implements Entity {
    private static long NEXT_ID = 1;
    private final long id;
    private final TextureRegion region;
    private final Suit suit;
    private final Rank rank;
    private static float globalTime = 0f;
    private final float points;
    private final float moveAmount = 30f;
    private final float floatAmplitude = 4f;
    private final float cardBaseY = 150f;
    private float width, height;
    private float baseY;
    private final boolean isJoker;
    private boolean isDragging;
    private boolean isSelected;

    public Card(Suit suit, Rank rank, float points, boolean isJoker,
                TextureRegion region) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = rank;
        this.points = points;
        this.isJoker = isJoker;
        this.region = region;
        this.width = region.getRegionWidth()/2f;
        this.height = region.getRegionHeight()/2f;

        setSize(width, height);
        setPosition(getX(), cardBaseY);
        baseY = getY();
        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            float offsetX, offsetY;
            float originalX, originalY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toFront();
                offsetX = x; offsetY = y;
                originalX = getX();
                originalY = getY();
                isDragging = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setPosition(event.getStageX() - offsetX, event.getStageY() - offsetY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDragging = false;
                setSize(width, height);
                setPosition(originalX, originalY);
                deselect();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                setSize(width + 25f, height + 25f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                setSize(width, height);
            }
        });
    }

    public Card(Suit suit, float points, boolean isJoker, TextureRegion region) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = null;
        this.points = points;
        this.isJoker = isJoker;
        this.region = region;
    }

    @SuppressWarnings("all")
    public Card(Card card) {
        this(card.suit, card.rank, card.points, card.isJoker, card.region);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isDragging) {
            float offset = (float) Math.sin(globalTime * Math.PI) * floatAmplitude;
            setY(baseY + offset + (isSelected ? moveAmount : 0));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if(!isDragging) { baseY = y; }
    }

    public long getId() {
        return id;
    }
    public Suit getSuit() {
        return suit;
    }
    public Rank getRank() {
        return rank;
    }
    public float getPoints() {
        return points;
    }
    public boolean isJoker() {
        return isJoker;
    }
    public boolean isDragging() {
        return isDragging;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public float getCardWidth() {
        return width;
    }
    public float getCardHeight() {
        return height;
    }

    public void select() {
        if(isDragging || isSelected) {
            return;
        }
        isSelected = true;
    }

    public void deselect() {
        if(!isSelected) {
            return;
        }
        isSelected = false;
        baseY = cardBaseY;
    }

    public static void updateGlobalTime(float delta) {
        globalTime += delta;
    }
}
