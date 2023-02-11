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
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
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
				if (ChoiceAmount == 1) {
					result = DialogueBoxPad;
				} else
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

	enum Status {
		RUNNING, FINISHED, READY
	}

	public static class DialogueStatus {

		private Status currentStatus = Status.READY;

		DialogueStatus() {
		}

		public void setRunning() {
			currentStatus = Status.RUNNING;
		}

		public void setFinished() {
			currentStatus = Status.FINISHED;
		}

		public void setReady() {
			currentStatus = Status.READY;
		}

		public boolean isReady() {
			if (currentStatus == Status.READY) {
				return true;
			}
			return false;
		}

		public boolean isRunning() {
			if (currentStatus == Status.RUNNING) {
				return true;
			}
			return false;
		}

		public boolean isFinished() {
			if (currentStatus == Status.FINISHED) {
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

	public static void typewriterEffect(final Table dialogueBoxTable, final Label target, final String input,
			final DialogueMarker dialogueMarker, final boolean setUntouchable, final DialogueReference inputReference) { // typewriterEffect
																															// method
		Timer.schedule(new Task() { // a delayed for() loop
			int i = 0;
			boolean finished = false; // finished boolean to prevent overlapping events

			public void run() {
				dialogueMarker.setVisible(false);
				if (dialogueStatus.isFinished()) {
					Timer.instance().clear();
					dialogueStatus.setReady();
				} else {
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
						if (inputReference.getChoiceCount() == 1) {
							inputReference.chooseChoice(1);
						}
						if (setUntouchable) {
							dialogueBoxTable.setTouchable(Touchable.disabled);
						} else {
							dialogueBoxTable.setTouchable(Touchable.enabled);
						}

						dialogueStatus.setReady();
					}
				}
			}
		}, 0, 0.02f, input.length());
	}

	public static void skipDialogueGeneration(Table dialogueBoxTable, Label target, String input,
			DialogueMarker dialogueMarker, boolean setUntouchable, DialogueReference inputReference) {
		if (dialogueStatus.isFinished() == false) {
			dialogueStatus.setFinished();
			target.setText(input);
			dialogueMarker.setVisible(true);
			dialogueMarker.setColor(255, 255, 255, 1);
			if (inputReference.getChoiceCount() == 1) {
				inputReference.chooseChoice(1);
			}
			if (setUntouchable) {
				dialogueBoxTable.setTouchable(Touchable.disabled);
			} else {
				dialogueBoxTable.setTouchable(Touchable.enabled);
			}
		}
	}

	public static void dialogueMainProcess(Table visualNovelTable, Label DialogueLabel, Table choice1, Table choice2,
			Table choice3,
			Label choiceLabel1, Label choiceLabel2, Label choiceLabel3, Table dialogueBoxTable,
			DialogueMarker dialogueMarker, DialogueReference inputReference) {
		DialogueReference dialogueReference = inputReference;
		String currentDialogueText = dialogueReference.getDialogue();
		visualNovelTable.clearChildren(); // Temporary fix to clear tables like choice sprites
		int choiceBump = ChoiceGUICalc("ChoiceBump", dialogueReference.getChoiceCount());
		switch (dialogueReference.getChoiceCount()) {
			case 1:
				visualNovelTable.add(dialogueBoxTable)
						.padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount()));
				currentDialogueText = dialogueReference.getDialogue();
				if (dialogueStatus.isRunning()) {
					skipDialogueGeneration(dialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker,
							false, inputReference);
					System.out.println("Choice 1 chosen");
				}

				if (dialogueStatus.isReady()) {
					typewriterEffect(dialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker,
							false, inputReference);
					System.out.println("Choice 1 chosen");
				}

				break;

			case 2:
				visualNovelTable.add(choice1).padTop(choiceBump);
				choiceLabel1.setText(dialogueReference.getChoice(0));
				visualNovelTable.row();
				choice1.setTouchable(Touchable.enabled);

				visualNovelTable.add(choice2).padTop(choiceBump);
				choiceLabel2.setText(dialogueReference.getChoice(1));
				visualNovelTable.row();
				choice2.setTouchable(Touchable.enabled);

				visualNovelTable.add(dialogueBoxTable)
						.padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount()));
				visualNovelTable.left().top();
				currentDialogueText = dialogueReference.getDialogue();
				if (dialogueStatus.isRunning()) {
					skipDialogueGeneration(dialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker,
							true, dialogueReference);
				}

				if (dialogueStatus.isReady()) {
					typewriterEffect(dialogueBoxTable, DialogueLabel, currentDialogueText,
							dialogueMarker, true, dialogueReference);
				}
				break;

			case 3:
				visualNovelTable.add(choice1).padTop(choiceBump);
				choiceLabel1.setText(dialogueReference.getChoice(0));
				visualNovelTable.row();
				choice1.setTouchable(Touchable.enabled);

				visualNovelTable.add(choice2).padTop(choiceBump);
				choiceLabel2.setText(dialogueReference.getChoice(1));
				visualNovelTable.row();
				choice2.setTouchable(Touchable.enabled);

				visualNovelTable.add(choice3).padTop(choiceBump);
				choiceLabel3.setText(dialogueReference.getChoice(2));
				visualNovelTable.row();
				choice3.setTouchable(Touchable.enabled);

				visualNovelTable.add(dialogueBoxTable)
						.padTop(ChoiceGUICalc("DialogueBox", dialogueReference.getChoiceCount()));
				visualNovelTable.left().top();
				currentDialogueText = dialogueReference.getDialogue();
				if (dialogueStatus.isRunning()) {
					skipDialogueGeneration(dialogueBoxTable, DialogueLabel, currentDialogueText, dialogueMarker,
							true, dialogueReference);
				}

				if (dialogueStatus.isReady()) {
					typewriterEffect(dialogueBoxTable, DialogueLabel, currentDialogueText,
							dialogueMarker, true, dialogueReference);
				}
				break;

			default:

				break;
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
		final Table visualNovelTable = new Table();
		Pixmap visualNovelBG = new Pixmap(Gdx.files.internal("background2.jpg"));
		TextureRegionDrawable visualNovelBGDrawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(visualNovelBG)));
		visualNovelTable.setBackground(visualNovelBGDrawable);
		table.add(visualNovelTable).padLeft(20).padTop(20);

		// UI DialogueLabel Widget
		final Table dialogueTextTable = new Table();
		Label.LabelStyle textDialogueLabelStyle = new Label.LabelStyle();
		BitmapFont defaultFont = new BitmapFont(Gdx.files.internal("font.fnt"));
		textDialogueLabelStyle.font = defaultFont;
		textDialogueLabelStyle.fontColor = Color.WHITE;
		final Label cialogueLabel = new Label("", textDialogueLabelStyle);
		cialogueLabel.setAlignment(Align.topLeft);
		cialogueLabel.setWrap(true);
		cialogueLabel.setTouchable(Touchable.disabled);
		cialogueLabel.setDebug(true);

		// Intialize DialogueMarker actor
		final DialogueMarker dialogueMarker = new DialogueMarker();
		stage.addActor(dialogueMarker);

		// Intialize Dialogue choice tables
		final Table choice1 = new Table();
		Pixmap choiceBG = new Pixmap(Gdx.files.internal("choicebox 3.png"));
		TextureRegionDrawable choiceBGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(choiceBG)));
		choice1.setBackground(choiceBGDrawable);
		choice1.setTouchable(Touchable.disabled);

		final Table choice2 = new Table();
		choice2.setBackground(choiceBGDrawable);
		choice2.setTouchable(Touchable.disabled);

		final Table choice3 = new Table();
		choice3.setBackground(choiceBGDrawable);
		choice3.setTouchable(Touchable.disabled);

		final Table choiceOutline = new Table();
		Pixmap ChoiceOutlineIMG = new Pixmap(Gdx.files.internal("choiceoutline.png"));
		TextureRegionDrawable ChoiceOutlineIMGDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(ChoiceOutlineIMG)));
		choiceOutline.setBackground(ChoiceOutlineIMGDrawable);
		choiceOutline.setTouchable(Touchable.disabled);
		choiceOutline.debugAll();

		final Stack choiceStack1 = new Stack();
		final Stack choiceStack2 = new Stack();
		final Stack choiceStack3 = new Stack();

		choice1.add(choiceStack1).size(1000, 121);
		choice2.add(choiceStack2).size(1000, 121);
		choice3.add(choiceStack3).size(1000, 121);

		final Label choiceLabel1 = new Label("", textDialogueLabelStyle);
		choiceLabel1.setAlignment(Align.center);
		choiceLabel1.setWrap(true);
		choiceLabel1.setTouchable(Touchable.disabled);
		choiceStack1.add(choiceLabel1);

		final Label choiceLabel2 = new Label("", textDialogueLabelStyle);
		choiceLabel2.setAlignment(Align.center);
		choiceLabel2.setWrap(true);
		choiceLabel2.setTouchable(Touchable.disabled);
		choiceStack2.add(choiceLabel2);

		final Label choiceLabel3 = new Label("", textDialogueLabelStyle);
		choiceLabel3.setAlignment(Align.center);
		choiceLabel3.setWrap(true);
		choiceLabel3.setTouchable(Touchable.disabled);
		choiceStack3.add(choiceLabel3);
		choiceLabel3.setDebug(true);

		visualNovelTable.setDebug(true);
		final Table dialogueBoxTable = new Table();
		Pixmap DialogueBG = new Pixmap(Gdx.files.internal("dialogue box.png"));
		TextureRegionDrawable DialogueBGDrawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(DialogueBG)));
		dialogueBoxTable.setBackground(DialogueBGDrawable);
		dialogueBoxTable.setTouchable(Touchable.enabled);

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
		dialogueTextTable.add(cialogueLabel).width(1250);
		dialogueTextTable.setZIndex(6);
		dialogueTextTable.left().top();
		dialogueTextTable.setTouchable(Touchable.enabled);
		dialogueBoxTable.add(dialogueTextTable).width(1250).height(188 - choice1.getHeight());

		visualNovelTable.add(dialogueBoxTable).padTop(ChoiceGUICalc("DialogueBox", 0));

		// Test json printing in console
		DialogueUI dog3DialogueUI = new DialogueUI();
		dog3DialogueUI.print();
		dog3DialogueUI.toJson();
		dog3DialogueUI.writeJson(dog3DialogueUI.toJson());

		// Typewriter effect for clicking DialogueBoxTable, will add script scanner and
		// move choice gui generator to stuff below.
		final DialogueReference mainReference = new DialogueReference();

		dialogueBoxTable.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogueMainProcess(visualNovelTable, cialogueLabel, choice1, choice2, choice3, choiceLabel1,
						choiceLabel2, choiceLabel3, dialogueBoxTable, dialogueMarker, mainReference);
			}
		});

		choice1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogueStatus.setReady();
				mainReference.chooseChoice(1);
				System.out.println("Choice 1 chosen");
				dialogueBoxTable.setTouchable(Touchable.enabled);
				dialogueMainProcess(visualNovelTable, cialogueLabel, choice1, choice2, choice3, choiceLabel1,
						choiceLabel2, choiceLabel3, dialogueBoxTable, dialogueMarker, mainReference);
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				choiceStack1.add(choiceOutline);

			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				choiceStack1.removeActor(choiceOutline);
			}
		});

		choice2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogueStatus.setReady();
				mainReference.chooseChoice(2);
				System.out.println("Choice 2 chosen");
				dialogueBoxTable.setTouchable(Touchable.enabled);
				dialogueMainProcess(visualNovelTable, cialogueLabel, choice1, choice2, choice3, choiceLabel1,
						choiceLabel2, choiceLabel3, dialogueBoxTable, dialogueMarker, mainReference);
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				choiceStack2.add(choiceOutline);

			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				choiceStack2.removeActor(choiceOutline);
			}
		});

		choice3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogueStatus.setReady();
				mainReference.chooseChoice(3);
				System.out.println("Choice 3 chosen");
				dialogueBoxTable.setTouchable(Touchable.enabled);
				dialogueMainProcess(visualNovelTable, cialogueLabel, choice1, choice2, choice3, choiceLabel1,
						choiceLabel2, choiceLabel3, dialogueBoxTable, dialogueMarker, mainReference);
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				choiceStack3.add(choiceOutline);

			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				choiceStack3.removeActor(choiceOutline);
			}
		});
	}
}
