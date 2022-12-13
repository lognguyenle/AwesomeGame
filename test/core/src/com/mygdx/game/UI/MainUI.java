package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.game.AwesomeGame;
import com.mygdx.game.Dialogue.DialogueMap;
import com.mygdx.game.Dialogue.DialogueReference;
import com.mygdx.game.Dialogue.DialogueUI;

public class MainUI {
	private static Stage stage;
	private static Group group;
	private static Table table;

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

	public Table getTable(){
		return table;
	}

	public Stage getStage(){
		return stage;
	}

	public static Group getGroup(){
		return group;
	}

    public MainUI(){
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

		//Test json printing in console
        DialogueUI dog3DialogueUI = new DialogueUI();
		dog3DialogueUI.print();
		dog3DialogueUI.toJson();
		dog3DialogueUI.writeJson(dog3DialogueUI.toJson());
		String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." 
		+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit" 
		+ "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt"
		+ "mollit anim id est laborum.";



		//Typewriter effect for clicking DialogueBoxTable, will add script scanner and move choice gui generator to stuff below.
		DialogueBoxTable.addListener(new ClickListener(){
			DialogueReference dialogueReference = new DialogueReference(); //Intializes dialogueReference object that references the dialogue tree initialized in dialogue UI
			String currentDialogueText = dialogueReference.getDialogue(); //Receives the current dialogue of the node in the tree through the dialogueReference object
			int timesClicked = 0; //Reads the times clicked for certain features
			@Override
			public void clicked(InputEvent event, float x, float y){
				timesClicked++; 
				currentDialogueText = dialogueReference.getDialogue();
				if(timesClicked > 1){ //Completes text generation when clicked twice, resets click timer
					Timer.instance().clear();
					dialogueMarker.setVisible(true);
					dialogueMarker.setColor(255, 255, 255, 1);
					DialogueLabel.setText(currentDialogueText);
					timesClicked = 0;
				}
				
				VisualNovelTable.clearChildren();	//Temporary fix to clear tables like choice sprites
				VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", 0)); //Adds DialogueBox table with proper spacing

				if(Timer.instance().isEmpty() && timesClicked == 1){  //Whole text generation timer loop, uses a timer that runs for the length of the string, basically
					Timer.schedule(new Task() {                       //a delayed for() loop
						int i = 0;
						boolean finished = false; //finished boolean to prevent overlapping events
						public void run() {
							dialogueMarker.setVisible(false);
							if (i <= currentDialogueText.length()) {
								DialogueLabel.setText(currentDialogueText.substring(0,i));
								i++;
								scrolly.scrollTo(0, 0, 0, 0);
								}
							if(currentDialogueText.length()+1 == i && finished == false) {
								dialogueMarker.setVisible(true);
								dialogueMarker.setColor(255, 255, 255, 1);
								System.out.println("done?"); //console test print
								finished = true;
								timesClicked = 0; //resets clicked variable
							}}
						}, 0, 0.02f, currentDialogueText.length());
					}
					dialogueReference.tick(); //ticks to reference the next node in the dialogue tree
							
			}});
    }
}
