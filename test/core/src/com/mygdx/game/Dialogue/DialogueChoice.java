package com.mygdx.game.Dialogue;

public class DialogueChoice {
    private String sourceId = "";
    private String destinationId = "";
    private String choiceText = "";
    //Sometime later connect the dialogue choice with a respective dialogue choice event
    public DialogueChoice(String sourceIdInput, String destinationIdInput, String choiceInput){
        sourceId = sourceIdInput;
        destinationId = destinationIdInput;
        choiceText = choiceInput;
    }
    public void setSource(String input){
        sourceId = input;
    }
    public String getSource(){
        return sourceId;
    }
    public void setDestination(String input){
        destinationId = input;
    }
    public String getDestination(){
        return destinationId;
    }
    public void setChoiceText(String input){
        choiceText = input;
    }
    public String getChoiceText(){
        return choiceText;
    }
}
