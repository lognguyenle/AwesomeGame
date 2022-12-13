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

    public void tick() {
        dialogueMap.setCurrentDialogueId("b1");
        currentDialogue = dialogueMap.getDialogue();
    }

}
