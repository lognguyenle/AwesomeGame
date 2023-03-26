package com.lolop.awesomegame.Dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Json;

public class DialogueMap {
    
    private Map<String, Dialogue> keysToDialogue;
    private Map<String, List<DialogueChoice>> keysToDialogueChoices;
    private String currentId;

    //No validation tests have been added
    public DialogueMap(){
        keysToDialogue = new HashMap<>();
        keysToDialogueChoices = new HashMap<>();
        currentId = "";
    }

    public DialogueMap(HashMap<String, Dialogue> inputDialogueMap, String inputId){
        setDialogue(inputDialogueMap);
        setCurrentDialogueId(inputId);
    }

    public void setDialogue(HashMap<String, Dialogue> inputDialogueMap){
        this.keysToDialogue = inputDialogueMap;
        this.keysToDialogueChoices = new HashMap<String, List<DialogueChoice>>(keysToDialogue.size());

        for(Dialogue dialogue: inputDialogueMap.values()){
            keysToDialogueChoices.put(dialogue.getId(), new ArrayList<DialogueChoice>());
        }
        this.keysToDialogue = inputDialogueMap;
    }
    

    public String getDialogue(){
        return keysToDialogue.get(currentId).getDialogueText();
    }

    public void setCurrentDialogueId(String inputId){
        currentId = inputId;
    }

    public String getCurrentDialogueId(){
        return currentId;
    }

    public String getSpeaker(){
        return keysToDialogue.get(currentId).getName();
    }

    // NO idea how this is useful
    // public ArrayList<DialogueChoice> getCurrentChoiceList(){
    //     return keysToDialogueChoices.get(currentId);
    // }


    //addChoice method where dialogueChoices are added into the map
    public void addChoice(DialogueChoice dialogueChoiceInput){
        List<DialogueChoice> inputList = new ArrayList<DialogueChoice>();
        inputList.add(0, dialogueChoiceInput);
        keysToDialogueChoices.put(dialogueChoiceInput.getSource(), inputList);
    }
    public void addChoice(DialogueChoice dialogueChoiceInput, DialogueChoice dialogueChoiceInput2){
        List<DialogueChoice> inputList = new ArrayList<DialogueChoice>();
        inputList.add(0, dialogueChoiceInput);
        inputList.add(1, dialogueChoiceInput2);
        keysToDialogueChoices.put(dialogueChoiceInput.getSource(), inputList);
    }
    public void addChoice(DialogueChoice dialogueChoiceInput, DialogueChoice dialogueChoiceInput2, DialogueChoice dialogueChoiceInput3 ){
        List<DialogueChoice> inputList = new ArrayList<DialogueChoice>();
        inputList.add(0, dialogueChoiceInput);
        inputList.add(1, dialogueChoiceInput2);
        inputList.add(2, dialogueChoiceInput3);
        keysToDialogueChoices.put(dialogueChoiceInput.getSource(), inputList);
    }

    public int choiceCount(){
        return getChoiceText().size();
    }

    //returns the Choice text as a String list
    public List<String> getChoiceText(){
        List<String> outputList = new ArrayList<String>();
        for(DialogueChoice outputChoice: keysToDialogueChoices.get(currentId)){
            outputList.add(outputChoice.getChoiceText());
        }
        return outputList;
    }

    //To be called when the game progresses through a choice, the method will change the current id of the map to the destination of that choice vertex
    public void chooseChoice(int choicePicked){
        currentId = keysToDialogueChoices.get(currentId).get(choicePicked - 1).getDestination();
    }

    public void loadJson(String input){
        Json json = new Json();
        DialogueMap inputMap = new DialogueMap();
        inputMap = json.fromJson(DialogueMap.class, input);
    }






}
