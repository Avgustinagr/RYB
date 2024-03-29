package com.fmi.game.development.ryb.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fmi.game.development.ryb.RYB;
import com.fmi.game.development.ryb.assets.enums.Color;
import com.fmi.game.development.ryb.game.GameWorld;

public class EnemyBlock extends Image {

    private RYB ryb;
    private World physicsWorld;
    private Body body;
    private Color color;
    private GameWorld gameWorld;
    private float velocity;

    public EnemyBlock(RYB ryb, World physicsWorld, Texture appearance,
                      float x, float y, float width, float height, Color color, GameWorld gameWorld) {
        super(appearance);
        this.ryb = ryb;
        this.physicsWorld = physicsWorld;
        setPosition(x, y);
        setOrigin(x, y);
        setWidth(width);
        setHeight(height);
        this.gameWorld = gameWorld;
        this.velocity = this.gameWorld.getVelocity();
        this.color = color;
        initBody();
        addBlock();
    }

    private void initBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = physicsWorld.createBody(bodyDef);

        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(getWidth() / 2, getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        body.setLinearVelocity(0,  this.velocity);

        bodyShape.dispose();
    }

    private void addBlock() {
        gameWorld.stage.addActor(this);
    }

    @Override
    public void act(float delta) {
        this.setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public Color getEnemyColor() {
        return color;
    }

}
