package com.mygdx.game;
import java.util.ArrayList;
import java.util.Scanner;
import org.w3c.dom.Text;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Json.*;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Group group;
	private Table table;

	// copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
	private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;
    private static final float ASPECT_RATIO =
        (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private Camera camera;
    private Rectangle viewport;

	//DialogueMarker Actor
	public class DialogueMarker extends Actor {
		Texture texture = new Texture(Gdx.files.internal("downarrow.png"));
		@Override
		public void draw(Batch batch, float alpha){
			batch.setColor(this.getColor());
			batch.draw(texture, 1310, 320);
			batch.setColor(batch.getColor());
		}
		@Override
		public void act(float delta){
			super.act(delta);
		}
	}

	//Gui padding calculator, variables have been added for easier understanding
	public static int ChoiceGUICalc(String Type, int ChoiceAmount){
		int result = 0;
		int ChoiceHeight = 121;
		int DialogueBoxPad = 508;
		int VisualNovelHeight = 760;
		int TextBoxHeight = 252;
		int ChoiceBump = ((VisualNovelHeight-TextBoxHeight) - (ChoiceHeight*ChoiceAmount)) / (ChoiceAmount+1);
		int ChoiceAll = ChoiceBump + ChoiceHeight;
		
		switch(Type) {
			case "DialogueBox":
			result = DialogueBoxPad - (ChoiceAmount*ChoiceAll);
			break;

			case "ChoiceBox":

			break;

			case "ChoiceBump":
			result = ChoiceBump;
			break;
		}
		return result;
	} 

	//Start of ApplicationListener life-cycle
	@Override
	public void create() {

		
		// copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		//Creates stage and then respective actors
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.top().left();

		

		//Setup tables for ui
		final Table VisualNovelTable = new Table();
		Pixmap visualNovelBG = new Pixmap(Gdx.files.internal("background2.jpg"));
		TextureRegionDrawable visualNovelBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(visualNovelBG)));
		VisualNovelTable.setBackground(visualNovelBGDrawable);
		table.add(VisualNovelTable).padLeft(20).padTop(20);

		final Table Choices1 = new Table();
		Pixmap ChoiceBG = new Pixmap(Gdx.files.internal("choicebox 3.png"));
		TextureRegionDrawable ChoiceBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(ChoiceBG)));
		Choices1.setBackground(ChoiceBGDrawable);
		Choices1.setTouchable(Touchable.enabled);

		final Table Choices2 = new Table();
		Choices2.setBackground(ChoiceBGDrawable);
		Choices2.setTouchable(Touchable.enabled);

		VisualNovelTable.setDebug(true);
		final Table DialogueBoxTable = new Table();
		Pixmap DialogueBG = new Pixmap(Gdx.files.internal("dialogue box.png"));
		TextureRegionDrawable DialogueBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(DialogueBG)));
		DialogueBoxTable.setBackground(DialogueBGDrawable);
		DialogueBoxTable.setTouchable(Touchable.enabled);

		//UI DialogueLabel Widget
		final Table DialogueTextTable = new Table();
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

		//Intialize DialogueMarker actor
		final DialogueMarker dialogueMarker = new DialogueMarker();
		stage.addActor(dialogueMarker);

		//Looper for flashing dialogueMarker
		final ColorAction transparent = new ColorAction();
		transparent.setEndColor(new Color(255, 255, 255, 0));
		transparent.setDuration(1);

		final ColorAction opaque = new ColorAction();
		opaque.setEndColor(new Color(255, 255, 255, 255));
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

		//Adding Texttable 
		DialogueTextTable.add(scrolly).width(1250);
		DialogueTextTable.setZIndex(6);
		DialogueTextTable.left().top();
		DialogueTextTable.setTouchable(Touchable.disabled);
		DialogueBoxTable.add(DialogueTextTable).width(1250).height(188 - Choices1.getHeight());


		
		//Adding Choice buttons, will move later, will probably add to type writer effect below.
		int Choices = 2;
		int ChoiceBump = ChoiceGUICalc("ChoiceBump", Choices);
		VisualNovelTable.add(Choices1).padTop(ChoiceBump);
		VisualNovelTable.row();
		VisualNovelTable.add(Choices2).padTop(ChoiceBump);
		VisualNovelTable.row();
		VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", Choices));
		VisualNovelTable.left().top();


		//Typewriter effect for clicking DialogueBoxTable, will add script scanner and move choice gui generator to stuff below.
		DialogueBoxTable.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				final String tempText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
				VisualNovelTable.clearChildren();
				//VisualNovelTable.removeActor(DialogueBoxTable);
				VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", 0));
					if(Timer.instance().isEmpty()){
					Timer.schedule(new Task() {
						int i = 0;
						public void run() {
							dialogueMarker.setVisible(false);
							if (i < tempText.length() - 1) {
								DialogueLabel.setText(tempText.substring(0,i));
								i++;
								scrolly.scrollTo(0, 0, 0, 0);
								}
								if(tempText.length()-1 == i){
									dialogueMarker.setVisible(true);
									dialogueMarker.setColor(255, 255, 255, 1);
							}}
						}, 0, 0.02f, tempText.length());}
					
							System.out.println("done?");
			}});
		
		




	}
		
	@Override
	public void render () {
		// update camera,  copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
        camera.update();
        // camera.apply,  copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
		// clear previous frame
		ScreenUtils.clear(0, 0, 0, 1);
		stage.act();
		stage.draw();
		
		
	}
	@Override
    public void resize(int width, int height){
        // calculate new viewport, copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
		if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    }
	@Override
	public void dispose () {
		stage.dispose();
		
	}
}
