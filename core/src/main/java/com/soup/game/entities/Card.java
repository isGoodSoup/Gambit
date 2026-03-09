package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import org.w3c.dom.Text;

public class Card extends Actor
    implements Entity {
    private static long NEXT_ID = 1;
    private final long id;
    private final Suit suit;
    private final Rank rank;
    private final float points;
    private final boolean isJoker;
    private TextureRegion region;

    public Card(Suit suit, Rank rank, float points, boolean isJoker, TextureRegion region) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = rank;
        this.points = points;
        this.isJoker = isJoker;
        this.region = region;

        setSize(region.getRegionWidth()/2f, region.getRegionHeight()/2f);
        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            float offsetX, offsetY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toFront();
                offsetX = x;
                offsetY = y;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                moveBy(x - offsetX, y - offsetY);
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
}
