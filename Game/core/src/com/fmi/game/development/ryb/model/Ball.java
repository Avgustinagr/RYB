package com.fmi.game.development.ryb.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fmi.game.development.ryb.RYB;
import com.fmi.game.development.ryb.assets.enums.Color;


public class Ball extends Image {

    private RYB ryb;
    private World physicsWorld;
    private Body body;
    private Color backgroundColor;

    private Sound impact;

    public Ball(RYB ryb, World physicsWorld, Texture appearance, float x, float y,
                float width, float height) {
        super(appearance);
        this.setX(x);
        this.setY(y);
        this.setOrigin(x, y);
        this.setWidth(width);
        this.setHeight(height);
        this.ryb = ryb;
        this.physicsWorld = physicsWorld;
        this.initBody();
        this.backgroundColor = Color.COLORLESS;

        this.impact = Gdx.audio.newSound(Gdx.files.internal("splash/impact_laser2.ogg"));
    }


    private void initBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = physicsWorld.createBody(bodyDef);

        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(1.8f);
        body.createFixture(bodyShape, 0.0f);

        body.setUserData(this);


        bodyShape.dispose();
    }


    public void setBackgroundColorToColorless() {
        this.backgroundColor = Color.COLORLESS;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void die() {
        impact.play(0.3f);
        setBackgroundColorToColorless();
        ryb.gameState = RYB.GAME_STATE.MENU;
    }


}
