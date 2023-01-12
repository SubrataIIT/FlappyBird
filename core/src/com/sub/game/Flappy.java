package com.sub.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.utils.ScreenUtils;


import java.util.Random;


import javax.swing.JInternalFrame;

public class Flappy extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture gameOver;
	Texture[] birds;
	int flapeState=0;
	float birdY=0;
	float velocity=0;
	int score=0;
	int scoringTube=0;
	Circle birdCircle;
	int gameState=0;
	float gravity=1;
	Texture topTube;
	Texture bottomTube;
	BitmapFont font;
	float gap=400;
	float maxTubeOffset;
	Random randomGenerator;

	float tubeVelocity=2;
	int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
	float[] tubeOffset=new float[numberOfTubes];

	float gapBetweenTubes;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver=new Texture("gameover.png");
		birds=new Texture[2];
		//shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");
		birdY=Gdx.graphics.getHeight()/2-birds[flapeState].getHeight()/2;
		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(10);
		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator=new Random();

		gapBetweenTubes=Gdx.graphics.getWidth() * 3/4;
		topTubeRectangle=new Rectangle[numberOfTubes];
		bottomTubeRectangle=new Rectangle[numberOfTubes];


     startGame();
	}
	public void startGame()
	{
		birdY=Gdx.graphics.getHeight()/2-birds[flapeState].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++)
		{
			tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2 +Gdx.graphics.getWidth() +i* gapBetweenTubes;
			topTubeRectangle[i]=new Rectangle();
			bottomTubeRectangle[i]=new Rectangle();
		}
	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState ==1) {
			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2)
			{
				Gdx.app.log("score",String.valueOf(score));
				score++;
				if(scoringTube<numberOfTubes-1)
				{
                  scoringTube++;
				}
				else
				{
					scoringTube=0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity=-17;
				if (flapeState == 0) {
					flapeState = 1;
				} else {
					flapeState = 0;
				}

			}
			for(int i=0;i<numberOfTubes;i++) {
				if(tubeX[i]< -topTube.getWidth())
				{
					tubeX[i]+=numberOfTubes*gapBetweenTubes;
					tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight()-gap-200);
				}
				else
				{
					tubeX[i]=tubeX[i]-tubeVelocity;
				}
				tubeX[i] -= tubeVelocity;
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				topTubeRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			}
			if(birdY>0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else
			{
				gameState=2;
			}
			if (flapeState == 0) {
				flapeState = 1;
			} else {
				flapeState = 0;
			}

		}
		else if(gameState==0)
		{
			if(Gdx.input.justTouched())
			{
				gameState=1;
				if (flapeState == 0) {
					flapeState = 1;
				} else {
					flapeState = 0;
				}

			}
		}
		else if(gameState==2) {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight());

			if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
			if (flapeState == 0) {
				flapeState = 1;
			} else {
				flapeState = 0;
			}


			batch.draw(birds[flapeState], Gdx.graphics.getWidth() / 2 - birds[flapeState].getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 100, 200);
			batch.end();
			birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapeState].getHeight() / 2, birds[flapeState].getWidth() / 2);
			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
			for (int i = 0; i < numberOfTubes; i++) {
				//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
				if (Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i])) {
					gameState = 2;
				}
			}
			//shapeRenderer.end();

	}


}
