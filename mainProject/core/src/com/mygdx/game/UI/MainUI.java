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
import com.mygdx.game.Dialogue.DialogueReference;
import com.mygdx.game.Dialogue.DialogueUI;

public class MainUI {
	private static Stage stage;
	private static Group group;
	private static Table table;

	// Gui padding calculator, variables have been added for easier understanding
	public static int ChoiceGUICalc(String Type, int ChoiceAmount) {
		int result = 0;
		int ChoiceHeight = 121;
		int DialogueBoxPad = 508;
		int VisualNovelHeight = 760;
		int TextBoxHeight = 252;
		int ChoiceBump = ((VisualNovelHeight - TextBoxHeight) - (ChoiceHeight * ChoiceAmount)) / (ChoiceAmount + 1);
		int ChoiceAll = ChoiceBump + ChoiceHeight;

		switch (Type) {
			case "DialogueBox":
				result = DialogueBoxPad - (ChoiceAmount * ChoiceAll);
				break;

			case "ChoiceBox":

				break;

			case "ChoiceBump":
				result = ChoiceBump;
				break;
		}
		return result;
	}

	// DialogueMarker Actor
	public class DialogueMarker extends Actor {
		Texture texture = new Texture(Gdx.files.internal("downarrow.png"));

		@Override
		public void draw(Batch batch, float alpha) {
			batch.setColor(this.getColor());
			batch.draw(texture, 1310, 320);
			batch.setColor(batch.getColor());
		}

		@Override
		public void act(float delta) {
			super.act(delta);
		}
	}
	enum Status{
		RUNNING, FINISHED, READY
	}

	public static class DialogueStatus{
		
		private Status currentStatus = Status.READY;
		DialogueStatus(){
		}

		public void setRunning(){
			currentStatus = Status.RUNNING;
			System.out.println(currentStatus);
		}

		public void setFinished(){
			currentStatus = Status.FINISHED;
			System.out.println(currentStatus);
		}

		public void setReady(){
			currentStatus = Status.READY;
		}

		public boolean isReady(){
			if(currentStatus == Status.READY){
				return true;
			}
			return false;
		}

		public boolean isRunning(){
			if(currentStatus == Status.RUNNING){
				return true;
			}
			return false;
		}

		public boolean isFinished(){
			System.out.println(currentStatus);
			if(currentStatus == Status.FINISHED){
				return true;
			}
			return false;
		}
	}

	final static DialogueStatus dialogueStatus = new DialogueStatus();

	public Table getTable() {
		return table;
	}

	public Stage getStage() {
		return stage;
	}

	public static Group getGroup() {
		return group;
	}

	public static void typewriterEffect(final Table dialogueTextTable, final Label target, final String input, final DialogueMarker dialogueMarker) { // typewriterEffect method
		
		Timer.schedule(new Task() { // a delayed for() loop
			
			int i = 0;
			boolean finished = false; // finished boolean to prevent overlapping events

			public void run() {

				dialogueMarker.setVisible(false);
				if (i <= input.length() && dialogueStatus.isFinished()) {
					dialogueStatus.setRunning();
					target.setText(input.substring(0, i));
					i++;
				}
				if (input.length() + 1 == i && finished == false) {
					dialogueMarker.setVisible(true);
					dialogueMarker.setColor(255, 255, 255, 1);
					System.out.println("done?"); // console test print
					finished = true;
				}
			}
		}, 0, 0.02f, input.length());
	}

	public static void typewriterEffectUntouchable(final Table dialogueBoxTable, final Label target, final String input,
			final DialogueMarker dialogueMarker) { // typewriterEffect method
		Timer.schedule(new Task() { // a delayed for() loop
			int i = 0;
			boolean finished = false; // finished boolean to prevent overlapping events

			public void run() {
				dialogueMarker.setVisible(false);
				if(dialogueStatus.isFinished()){
					Timer.instance().clear();

				}else{
				dialogueStatus.setRunning();
				if (i <= input.length()) {
					target.setText(input.substring(0, i));
					i++;
				}
				if (input.length() + 1 == i && finished == false) {
					dialogueMarker.setVisible(true);
					dialogueMarker.setColor(255, 255, 255, 1);
					System.out.println("done?"); // console test print
					finished = true;
					dialogueBoxTable.setTouchable(Touchable.disabled);
					dialogueStatus.setFinished();
				}
			}
			}
		}, 0, 0.02f, input.length());
	}

	public static void skipDialogueGeneration(Label target, String input, DialogueMarker dialogueMarker){
		if(dialogueStatus.isFinished() == false){
		dialogueStatus.setFinished();
		target.setText(input);
		dialogueMarker.setVisible(true);
		dialogueMarker.setColor(255, 255, 255, 1);
		}
	}

	public MainUI() {
		// Creates stage and then respective actors
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.top().left();

		// Setup tables for ui
		final Table VisualNovelTable = new Table();
		Pixmap visualNovelBG = new Pixmap(Gdx.files.internal("background2.jpg"));
		TextureRegionDrawable visualNovelBGDrawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(visualNovelBG)));
		VisualNovelTable.setBackground(visualNovelBGDrawable);
		table.add(VisualNovelTable).padLeft(20).padTop(20);

		// UI DialogueLabel Widget
		final Table DialogueTextTable = new Table();
		Label.LabelStyle TextDialogueLabelStyle = new Label.LabelStyle();
		BitmapFont defaultFont = new BitmapFont(Gdx.files.internal("font.fnt"));
		TextDialogueLabelStyle.font = defaultFont;
		TextDialogueLabelStyle.fontColor = Color.WHITE;
		final Label DialogueLabel = new Label("", TextDialogueLabelStyle);
		DialogueLabel.setAlignment(Align.topLeft);
		DialogueLabel.setWrap(true);
		DialogueLabel.setTouchable(Touchable.disabled);
		DialogueLabel.setDebug(true);
		
		// Intialize DialogueMarker actor
		final DialogueMarker dialogueMarker = new DialogueMarker();
		stage.addActor(dialogueMarker);

		// Intialize Dialogue choice tables
		final Table Choices1 = new Table();
		Pixmap ChoiceBG = new Pixmap(Gdx.files.internal("choicebox 3.png"));
		TextureRegionDrawable ChoiceBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(ChoiceBG)));
		Choices1.setBackground(ChoiceBGDrawable);
		Choices1.setTouchable(Touchable.disabled);

		final Table Choices2 = new Table();
		Choices2.setBackground(ChoiceBGDrawable);
		Choices2.setTouchable(Touchable.disabled);

		final Table Choices3 = new Table();
		Choices3.setBackground(ChoiceBGDrawable);
		Choices3.setTouchable(Touchable.disabled);

		final Label ChoiceLabel1 = new Label("", TextDialogueLabelStyle);
		ChoiceLabel1.setAlignment(Align.center);
		ChoiceLabel1.setWrap(true);
		Choices1.add(ChoiceLabel1).width(950);

		final Label ChoiceLabel2 = new Label("", TextDialogueLabelStyle);
		ChoiceLabel2.setAlignment(Align.center);
		ChoiceLabel2.setWrap(true);
		Choices2.add(ChoiceLabel2).width(950);

		final Label ChoiceLabel3 = new Label("", TextDialogueLabelStyle);
		ChoiceLabel3.setAlignment(Align.center);
		ChoiceLabel3.setWrap(true);
		Choices3.add(ChoiceLabel3).width(950);

		VisualNovelTable.setDebug(true);
		final Table DialogueBoxTable = new Table();
		Pixmap DialogueBG = new Pixmap(Gdx.files.internal("dialogue box.png"));
		TextureRegionDrawable DialogueBGDrawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(DialogueBG)));
		DialogueBoxTable.setBackground(DialogueBGDrawable);
		DialogueBoxTable.setTouchable(Touchable.enabled);

		// Looper for flashing dialogueMarker
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

		// Adding Texttable
		DialogueTextTable.add(DialogueLabel).width(1250);
		DialogueTextTable.setZIndex(6);
		DialogueTextTable.left().top();
		DialogueTextTable.setTouchable(Touchable.enabled);
		DialogueBoxTable.add(DialogueTextTable).width(1250).height(188 - Choices1.getHeight());

		VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", 0));

		// Test json printing in console
		DialogueUI dog3DialogueUI = new DialogueUI();
		dog3DialogueUI.print();
		dog3DialogueUI.toJson();
		dog3DialogueUI.writeJson(dog3DialogueUI.toJson());
		String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
				+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
				+ "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt"
				+ "mollit anim id est laborum.";

		// Typewriter effect for clicking DialogueBoxTable, will add script scanner and
		// move choice gui generator to stuff below.
		final DialogueReference mainReference = new DialogueReference();

		

		DialogueBoxTable.addListener(new ClickListener() {	
			DialogueReference dialogueReference = mainReference; // Intializes dialogueReference object that
																			// references the dialogue tree initialized
																			// in dialogue UI
			String currentDialogueText = dialogueReference.getDialogue(); // Receives the current dialogue of the node
																			// in the tree through the dialogueReference
																			// object

			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisualNovelTable.clearChildren(); // Temporary fix to clear tables like choice sprites
				int choiceBump = ChoiceGUICalc("ChoiceBump", dialogueReference.getChoiceCount());
				switch (dialogueReference.getChoiceCount()) {
					case 1:
						VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount())); 
						currentDialogueText = dialogueReference.getDialogue();
						if(dialogueStatus.isRunning()){
							skipDialogueGeneration(DialogueLabel, currentDialogueText, dialogueMarker);
							}
	
							if(dialogueStatus.isReady()){
							typewriterEffect(DialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker);
							}
						break;

					case 2:
						VisualNovelTable.add(Choices1).padTop(choiceBump);
						ChoiceLabel1.setText(dialogueReference.getChoice(0));
						VisualNovelTable.row();
						Choices1.setTouchable(Touchable.enabled);

						VisualNovelTable.add(Choices2).padTop(choiceBump);
						ChoiceLabel2.setText(dialogueReference.getChoice(1));
						VisualNovelTable.row();
						Choices2.setTouchable(Touchable.enabled);
						
						VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount()));
						VisualNovelTable.left().top();
						currentDialogueText = dialogueReference.getDialogue();
						if(dialogueStatus.isRunning()){
							skipDialogueGeneration(DialogueLabel, currentDialogueText, dialogueMarker);
							}
	
							if(dialogueStatus.isReady()){
							typewriterEffectUntouchable(DialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker);
							}
						break;

					case 3:
						VisualNovelTable.add(Choices1).padTop(choiceBump);
						ChoiceLabel1.setText(dialogueReference.getChoice(0));
						VisualNovelTable.row();
						Choices1.setTouchable(Touchable.enabled);

						VisualNovelTable.add(Choices2).padTop(choiceBump);
						ChoiceLabel2.setText(dialogueReference.getChoice(1));
						VisualNovelTable.row();
						Choices2.setTouchable(Touchable.enabled);

						VisualNovelTable.add(Choices3).padTop(choiceBump);
						ChoiceLabel3.setText(dialogueReference.getChoice(2));
						VisualNovelTable.row();
						Choices3.setTouchable(Touchable.enabled);

						VisualNovelTable.add(DialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount()));
						VisualNovelTable.left().top();
						currentDialogueText = dialogueReference.getDialogue();
						if(dialogueStatus.isRunning()){
						skipDialogueGeneration(DialogueLabel, currentDialogueText, dialogueMarker);
						}

						if(dialogueStatus.isReady()){
						typewriterEffectUntouchable(DialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker);
						}
						break;

					default:

						break;
				}

			}
		});

		Choices1.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				dialogueStatus.setReady();
				mainReference.chooseChoice(1);
				System.out.println("Choice 1 chosen");
				DialogueBoxTable.setTouchable(Touchable.enabled);
				}
		});

		Choices2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				dialogueStatus.setReady();
				mainReference.chooseChoice(2);
				System.out.println("Choice 2 chosen");
				DialogueBoxTable.setTouchable(Touchable.enabled);
			}
		});

		Choices3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				dialogueStatus.setReady();
				mainReference.chooseChoice(3);
				System.out.println("Choice 3 chosen");
				DialogueBoxTable.setTouchable(Touchable.enabled);
			}
		});
	}
}
