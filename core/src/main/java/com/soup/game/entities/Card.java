package com.soup.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

@SuppressWarnings("all")
public class Card extends Actor
    implements Entity {
    private static final TextureRegion back =
        new TextureRegion(new Texture("sprites/cards_back.png"));
    private static long NEXT_ID = 1;
    private final long id;
    private TextureRegion region;
    private Suit suit;
    private Rank rank;
    private static float globalTime = 0f;
    private final float points;
    private final float moveAmount = 16f;
    private final float floatAmplitude = 4f;
    private final float baseY = 180f;
    private float width, height;
    private final boolean isJoker;
    private boolean isDragging;
    private boolean isSelected;
    private boolean isAnimating;
    private boolean isFlipped;

    public Card(Suit suit, Rank rank, boolean isJoker, TextureRegion region) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = rank;
        this.points = (rank != null) ? rank.getPoints() : 0f;
        this.isJoker = isJoker;
        this.region = region;
        this.width = region.getRegionWidth();
        this.height = region.getRegionHeight();

        setSize(width, height);
        setPosition(getX(), baseY);
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
                setSize(width + 5f, height + 5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                setSize(width, height);
            }
        });
    }

    public Card(boolean isJoker, TextureRegion region) {
        this.id = NEXT_ID++;
        this.points = 50f;
        this.isJoker = isJoker;
        this.region = region;
    }

    @SuppressWarnings("all")
    public Card(Card card) {
        this(card.suit, card.rank, card.isJoker, card.region);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isDragging && !isAnimating) {
            float offset = (float) Math.sin(globalTime * Math.PI) * floatAmplitude;
            setY(baseY + offset + (isSelected ? moveAmount : 0));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(isFlipped ? back : region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
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
    public boolean isAnimating() {
        return isAnimating;
    }
    public void setAnimating(boolean animating) {
        isAnimating = animating;
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
    }

    public static void updateGlobalTime(float delta) {
        globalTime += delta;
    }

    public float getBaseY() {
        return baseY;
    }

    public void flip() {
        setOrigin(getWidth()/2f, getHeight()/2f);
        addAction(Actions.sequence(
            Actions.scaleBy(-getWidth()/2f, 1f),
            Actions.run(() -> isFlipped = !isFlipped),
            Actions.scaleTo(1f, 1f)
        ));

    }
}
