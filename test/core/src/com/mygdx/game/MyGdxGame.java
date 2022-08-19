package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.*;

public class MyGdxGame extends ApplicationAdapter {

	public class MyActor extends Actor {
		Texture texture = new Texture(Gdx.files.internal("background1.jpg"));
			float actorX = 0, actorY = 0;
		
		public void draw(Batch batch, float alpha){
			batch.draw(texture, 10, 20);
		
		}	
	}
	private Stage stage;

	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		MyActor Actor = new MyActor();
		Actor.setTouchable(Touchable.enabled);
		stage.addActor(Actor);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stage.draw();
		
	}
	
	@Override
	public void dispose () {
		
	}
}
