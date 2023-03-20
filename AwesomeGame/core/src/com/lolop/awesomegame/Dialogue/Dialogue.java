package com.lolop.awesomegame.Dialogue;

//Dialogue object that includes the text, person speaking, and the id of the dialogue.
public class Dialogue{
    private String characterName;
    private String dialogueText;
    private String id;
    public Dialogue(String idInput, String characterNameInput, String dialogueTextInput){
        id = idInput;
        characterName = characterNameInput;
        dialogueText = dialogueTextInput;
    }
    public void setId(String idInput){
        id = idInput;
    }
    public String getId(){
        return id;
    }
    public void setDialogueText(String input){
        dialogueText = input;
    }
    public String getDialogueText(){
        return dialogueText;
    }
    public void setName(String input){
        characterName = input;
    }
    public String getName(){
        return characterName;
    }

}