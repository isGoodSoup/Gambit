package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import org.w3c.dom.Text;

public class Card extends Actor
    implements Entity {
    private static long NEXT_ID = 1;
    private final long id;
    private TextureRegion region;
    private final Suit suit;
    private final Rank rank;
    private final float points;
    private final float moveAmount = 30f;
    private final float duration = 0.15f;
    private float width, height;
    private final boolean isJoker;
    private boolean isDragging;
    private boolean isSelected;

    public Card(Suit suit, Rank rank, float points, boolean isJoker, TextureRegion region) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = rank;
        this.points = points;
        this.isJoker = isJoker;
        this.region = region;
        this.width = region.getRegionWidth()/2f;
        this.height = region.getRegionHeight()/2f;

        setSize(width, height);
        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            float offsetX, offsetY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toFront();
                offsetX = x; offsetY = y;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                isDragging = true;
                moveBy(x - offsetX, y - offsetY);
                setSize(width + 25f, height + 25f);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDragging = false;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    public void select() {
        if(isDragging || isSelected) {
            return;
        }

        isSelected = true;
        clearActions();
        addAction(Actions.moveBy(0, moveAmount,
            duration, Interpolation.sineOut));
    }

    public void deselect() {
        if(!isSelected) {
            return;
        }
        isSelected = false;
        clearActions();
        addAction(Actions.moveBy(0, -moveAmount,
            duration, Interpolation.sineIn));
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
}
