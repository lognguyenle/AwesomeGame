package com.mygdx.game;

import java.util.Scanner;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

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
			float actorX = 20, actorY = 300;
		public DialogueBox() {
			setBounds(20,300,texture.getWidth(),texture.getHeight());
		}
		

		public void draw(Batch batch, float alpha){
			batch.draw(texture, 20, 300);
		}
	}

	//DialogueText Actor, will have a typewriting effect
	final public class DialogueText extends Actor {
		BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt")); 
			float actorX = 0, actorY = 0;
		String text;
		public DialogueText(String input){
			text = input; 
		}

		public void draw(Batch batch, float alpha){
			font.draw(batch, text, 60, 510);
		}

		public void updateText(final String input){
			text = input;
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

		final DialogueText dialogueText = new DialogueText("sada");
		dialogueText.setTouchable(Touchable.disabled);
		stage.addActor(dialogueText);

		//Typewriter effect for clicking dialogueBox
		dialogueBox.addListener(new ClickListener(){
			boolean textFinished = false;
			boolean clicked = true;
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("true!");
				final String bruh = "Bruhhhhhhhhhhhhhhhh";
				clicked = true;
				if(clicked){
				Timer.schedule(new Task(){
					int i = 0;
					public void run() {
						
						if(i < bruh.length()-1){
							dialogueText.updateText(bruh.substring(0,i));
							i++;
					}
				}}, 0,0.05f,bruh.length());}
				textFinished = true;
				clicked = false;
				System.out.println("text finished!");
			}});
		
	}
		
		

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		
	}
	
	@Override
	public void dispose () {
		stage.dispose();
		
	}
}
