package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.*;

public class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Group group;

	//Background Actor
	public class Background extends Actor {
		Texture texture = new Texture(Gdx.files.internal("background1.jpg"));
			float actorX = 0, actorY = 0;
		
		public void draw(Batch batch, float alpha){
			batch.draw(texture, 20, 300);
		
		}	
	}

	//DialogueBox Actor
	public class DialogueBox extends Actor {
		Texture texture = new Texture(Gdx.files.internal("dialogue box.png"));
			float actorX = 0, actorY = 0;

		public void draw(Batch batch, float alpha){
			batch.draw(texture, 20, 300);
		}
	}

	//DialogueText Actor, will have a typewriting effect
	public class DialogueText extends Actor {
		BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt")); 
			float actorX = 0, actorY = 0;
		String text;
		public DialogueText(String input){
			text = input; 
		}

		public void draw(Batch batch, float alpha){
			font.draw(batch, text, 39, 30);
		}
	}
	
	//Start of ApplicationListener life-cycle
	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Background background = new Background();
		background.setTouchable(Touchable.enabled);
		stage.addActor(background);
		
		DialogueBox dialogueBox = new DialogueBox();
		dialogueBox.setTouchable(Touchable.enabled);
		stage.addActor(dialogueBox);

		DialogueText dialogueText = new DialogueText("sada");
		dialogueText.setTouchable(Touchable.disabled);
		stage.addActor(dialogueText);

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
