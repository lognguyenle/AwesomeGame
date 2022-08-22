package com.mygdx.game;

import java.util.Scanner;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Group group;

	//Background Actor
	public class Background extends Actor {
		Texture texture = new Texture(Gdx.files.internal("background1.jpg"));
			float actorX = 0, actorY = 0;
		
		public void draw(Batch batch, float alpha){
			batch.setColor(this.getColor());
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
			batch.setColor(this.getColor());
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

	//DialogueMarker Actor
	public class DialogueMarker extends Actor {
		Texture texture = new Texture(Gdx.files.internal("downarrow.png"));
		@Override
		public void draw(Batch batch, float alpha){
			
			batch.setColor(this.getColor());
			batch.draw(texture, 1300, 320);
			batch.setColor(batch.getColor());
			
		}
		@Override
		public void act(float delta){
			super.act(delta);
		}
		

	}
	
	//Start of ApplicationListener life-cycle
	@Override
	public void create() {

		//Creates stage and then respective actors
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Background background = new Background();
		background.setTouchable(Touchable.enabled);
		stage.addActor(background);
		
		final DialogueBox dialogueBox = new DialogueBox();
		dialogueBox.setTouchable(Touchable.enabled);
		stage.addActor(dialogueBox);

		final DialogueText dialogueText = new DialogueText("sada");
		dialogueText.setTouchable(Touchable.disabled);
		stage.addActor(dialogueText);

		final DialogueMarker dialogueMarker = new DialogueMarker();
		stage.addActor(dialogueMarker);
		

		//Looper for flashing dialogueMarker
		final ColorAction transparent = new ColorAction();
		transparent.setEndColor(new Color(0, 0, 0, 0));
		transparent.setDuration(1);

		final ColorAction opaque = new ColorAction();
		opaque.setEndColor(new Color(255, 255, 255, 1));
		opaque.setDuration(1);

		DelayAction delay = new DelayAction();
		delay.setDuration(0.5f);

		final SequenceAction dialogueMarkerLoop = new SequenceAction();
		dialogueMarkerLoop.addAction(delay);
		dialogueMarkerLoop.addAction(transparent);
		dialogueMarkerLoop.addAction(delay);
		dialogueMarkerLoop.addAction(opaque);

		final RepeatAction dialogueMarkerLooper = new RepeatAction();
		dialogueMarkerLooper.setCount(RepeatAction.FOREVER);
		dialogueMarkerLooper.setAction(dialogueMarkerLoop);
		dialogueMarker.addAction(dialogueMarkerLooper);

		
		//Typewriter effect for clicking dialogueBox
		dialogueBox.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				final String bruh = "Bruhhhhhhhhhhhhhhhh";
					if(Timer.instance().isEmpty()){
					Timer.schedule(new Task() {
						int i = 0;
						public void run() {
							dialogueMarker.setVisible(false);
							if (i < bruh.length() - 1) {
								dialogueText.updateText(bruh.substring(0, i));
								i++;
								}
								if(bruh.length()-1 == i){
									dialogueMarker.setVisible(true);
									dialogueMarker.setColor(255, 255, 255, 1);
									
							}}
						}, 0, 0.05f, bruh.length());}
							System.out.println("done?");
			}});
		
	}
		
		

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stage.act();
		stage.draw();
		
		
	}
	
	@Override
	public void dispose () {
		stage.dispose();
		
	}
}
