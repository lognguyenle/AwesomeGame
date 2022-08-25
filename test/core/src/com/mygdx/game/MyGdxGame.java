package com.mygdx.game;
import java.util.Scanner;
import org.w3c.dom.Text;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Group group;
	private Table table;

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

		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.top().left();

		Table VisualNovelTable = new Table();

		Table DialogueBoxTable = new Table();

		Table DialogueTextTable = new Table();

		//Setup tables for ui

		Pixmap visualNovelBG = new Pixmap(Gdx.files.internal("background1.jpg"));
		TextureRegionDrawable visualNovelBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(visualNovelBG)));
		VisualNovelTable.setBackground(visualNovelBGDrawable);
		table.add(VisualNovelTable).padLeft(20).padTop(20);

		Pixmap DialogueBG = new Pixmap(Gdx.files.internal("dialogue box.png"));
		TextureRegionDrawable DialogueBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(DialogueBG)));
		DialogueBoxTable.setBackground(DialogueBGDrawable);
		DialogueBoxTable.setTouchable(Touchable.enabled);
		VisualNovelTable.add(DialogueBoxTable).padTop(508);
		VisualNovelTable.left().top();

		//UI DialogueLabel Widget
		Label.LabelStyle TextDialogueLabelStyle = new Label.LabelStyle();
		BitmapFont defaultFont = new BitmapFont(Gdx.files.internal("font.fnt"));
		TextDialogueLabelStyle.font = defaultFont;
		TextDialogueLabelStyle.fontColor = Color.WHITE;
		final Label DialogueLabel = new Label("", TextDialogueLabelStyle);

		DialogueLabel.setAlignment(Align.left);
		DialogueLabel.setWrap(true); 
		DialogueLabel.setTouchable(Touchable.disabled);
	
		//Scroll pane that takes in DialogueLabel
		final ScrollPane scrolly = new ScrollPane(DialogueLabel);
		scrolly.setScrollbarsVisible(false);
		scrolly.setScrollY(20);
		scrolly.setTouchable(Touchable.disabled);
	
		DialogueTextTable.add(scrolly).width(1250);
		DialogueTextTable.setZIndex(6);
		DialogueTextTable.left().top();
		DialogueTextTable.setTouchable(Touchable.disabled);
		DialogueBoxTable.add(DialogueTextTable).width(1250).height(188);

		//Intialize DialogueMarker actor
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

		
		//Typewriter effect for clicking DialogueBoxTable
		DialogueBoxTable.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				final String bruh = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
					if(Timer.instance().isEmpty()){
					Timer.schedule(new Task() {
						int i = 0;
						public void run() {
							dialogueMarker.setVisible(false);
							if (i < bruh.length() - 1) {
								DialogueLabel.setText(bruh.substring(0,i));
								i++;
								scrolly.scrollTo(0, 0, 0, 0);
								}
								if(bruh.length()-1 == i){
									dialogueMarker.setVisible(true);
									dialogueMarker.setColor(255, 255, 255, 1);
							}}
						}, 0, 0.02f, bruh.length());}
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
