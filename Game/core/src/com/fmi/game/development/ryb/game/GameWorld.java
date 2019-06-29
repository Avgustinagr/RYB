package com.fmi.game.development.ryb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.fmi.game.development.ryb.RYB;
import com.fmi.game.development.ryb.assets.Assets;
import com.fmi.game.development.ryb.listener.SameColorsContactListener;
import com.fmi.game.development.ryb.model.EnemyBlock;
import com.fmi.game.development.ryb.model.Ball;
import com.fmi.game.development.ryb.screen.MenuScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.fmi.game.development.ryb.RYB.WORLD_HEIGHT;

public class GameWorld {

    private final int maxNumberOfEnemies = 2;
    private final int maxNumberOfBlockFiles = 36;
    private final int blockDifference = 15;

    private RYB ryb;
    private World physicsWorld;
    private Ball ball;
    public Stage stage;
    private float worldWidth;
    private int score;
    private int blockVelocity;
    List<EnemyBlock> enemies;

    private float blockWidth;
    private float blockHeight;

    public GameWorld(RYB ryb) {
        this.blockVelocity = -4;

        this.ryb = ryb;
        this.physicsWorld = new World(new Vector2(0, -0.5f), true);
        this.physicsWorld.setContactListener(new SameColorsContactListener(this));
        float ratio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        this.worldWidth = WORLD_HEIGHT / ratio;
        this.ball = new Ball(ryb, physicsWorld, ryb.assets.manager.get(Assets.player, Texture.class),
                worldWidth / 2 - 1 , WORLD_HEIGHT / 3, 2, 2);
        this.stage = new Stage(new StretchViewport(worldWidth, WORLD_HEIGHT));

        this.blockWidth = worldWidth + 1;
        this.blockHeight = WORLD_HEIGHT / 20 + 0.5f;

        this.stage.addActor(ball);
        this.initEnemy();
        this.score = 0;
    }

    private void initEnemy() {
        enemies = new ArrayList<EnemyBlock>(maxNumberOfEnemies);

        Random random = new Random();
        int index = random.nextInt(maxNumberOfBlockFiles);

        float blockX = worldWidth / 2;
        float blockY = WORLD_HEIGHT;

        for(int i = 0; i < maxNumberOfEnemies; i++) {
            EnemyBlock enemy = new EnemyBlock(ryb, physicsWorld,
                    ryb.assets.manager.get(Assets.blocks[index].getFilename(), Texture.class),
                    blockX, blockY , this.blockWidth, this.blockHeight, Assets.blocks[index].getColor(), this);
            blockY += blockDifference;
            enemies.add(enemy);
            index = random.nextInt(maxNumberOfBlockFiles);
        }

    }

    public void render() {
        this.stage.draw();
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    public void update() {

        this.stage.act();
        this.regenerateEnemy();
        int highScore = ryb.getHighScore();

        if (ryb.gameState == RYB.GAME_STATE.MENU) {

            if (highScore < this.score) {
                ryb.setHighScore(this.score);
                ryb.updateHighScore(this.score);
            }

            ryb.setScreen(new MenuScreen(ryb));
        }
    }


    private void regenerateEnemy() {

        if (enemies.get(0).getY() < (stage.getCamera().position.y - worldWidth / 2)) {
            enemies.get(0).remove();
            enemies.remove(0);
        }
        if (enemies.size() == 1) {

            Random random = new Random();
            int index = random.nextInt(maxNumberOfBlockFiles);
            EnemyBlock enemy = new EnemyBlock(ryb, physicsWorld,
                    ryb.assets.manager.get(Assets.blocks[index].getFilename(), Texture.class),
                    worldWidth / 2, enemies.get(enemies.size() - 1).getY() + blockDifference, this.blockWidth, this.blockHeight, Assets.blocks[index].getColor(), this);
            enemies.add(enemy);
        }
    }


    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public int getVelocity() {
        return this.blockVelocity;
    }

    public void decreaseVelocity() {
        this.blockVelocity -= 1;
    }

    public Ball getBall() {
        return this.ball;
    }
}
