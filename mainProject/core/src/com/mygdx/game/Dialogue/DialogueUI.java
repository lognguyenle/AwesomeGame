package com.mygdx.game.Dialogue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class DialogueUI {
    private Map<String, Dialogue> DialogueHashMap = new HashMap<String, Dialogue>();
    int a;

    public DialogueUI() {
        DialogueHashMap.put(dialogue.getId(), dialogue);
        DialogueHashMap.put(dialogue2.getId(), dialogue2);
        DialogueHashMap.put(dialogue3.getId(), dialogue3);
        DialogueHashMap.put(dialogue4.getId(), dialogue4);
        dialogueMap = new DialogueMap(new HashMap<String, Dialogue>(DialogueHashMap), "a1");
        dialogueMap.addChoice(choice1, choice2, choice3);
        dialogueMap.addChoice(choice4);
        a = 0;
    }

    public static DialogueMap dialogueMap;
    Dialogue dialogue = new Dialogue("a1", "narrator", "Testing, testing, Do you yield?");
    DialogueChoice choice1 = new DialogueChoice("a1", "a2", "Yes");
    DialogueChoice choice2 = new DialogueChoice("a1", "a3", "No");
    DialogueChoice choice3 = new DialogueChoice("a1", "a4", "Why?");
    Dialogue dialogue2 = new Dialogue("a2", "narrator", "Hey man......");
    Dialogue dialogue3 = new Dialogue("a3", "narrator", "Testing, testing, Do you yield????");
    DialogueChoice choice4 = new DialogueChoice("a2", "a3", "Yes");
    Dialogue dialogue4 = new Dialogue("a4", "narrator", "Testing, testing, Do you yield??????");

    public void print() {
        System.out.println(dialogueMap.getDialogue());
    }

    Json json = new Json();

    public String toJson() {
        json.setOutputType(OutputType.json);
        System.out.println(json.prettyPrint(dialogueMap));
        return json.prettyPrint(dialogueMap);
    }

    public void writeJson(String input) {
        try {
            FileWriter file = new FileWriter("mainProject/assets/script/gamescript.json");
            file.write(input);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DialogueMap getDialogueMap() {
        return dialogueMap;
    }

}
