package com.mygdx.game.Dialogue;

public class DialogueReference {
    //Used as a reference class for stupid scope errors
    private DialogueMap dialogueMap;
    private String currentDialogue;

    public DialogueReference(DialogueMap inputDialogueMap){
        this.dialogueMap = inputDialogueMap;
    }

    public DialogueReference(){
        this.dialogueMap = DialogueUI.getDialogueMap();
        currentDialogue = dialogueMap.getDialogue();
    }

    public String getDialogue(){
        return currentDialogue;
    }

    public String getChoice(int i){
        return dialogueMap.getChoiceText().get(i);
    }

    public int getChoiceCount(){
        System.out.println(dialogueMap.choiceCount());
        System.out.println(dialogueMap.getCurrentDialogueId());
        return dialogueMap.choiceCount();
    }

    public void nextDialogue() {
        dialogueMap.chooseChoice(1);
        currentDialogue = dialogueMap.getDialogue();
    }

    public void chooseChoice(int choice){
        dialogueMap.chooseChoice(choice);
        currentDialogue = dialogueMap.getDialogue();
    }



}
