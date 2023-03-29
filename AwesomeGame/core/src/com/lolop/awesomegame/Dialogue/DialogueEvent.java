package com.lolop.awesomegame.Dialogue;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DialogueEvent {

    Event[] eventArray;

    enum EventType{
        BACKGROUNDCHANGE, CHARACTERHIGHLIGHT, CHARACTERUNHIGHLIGHT, CHARACTERSPRITECHANGE, NONE
    }

    class BackgroundChange extends Event {
        EventType type = EventType.BACKGROUNDCHANGE;
        String backgroundId;
        String effectId;
        
        void changeBackground() {

        }
    }

    class CharacterHighlight extends Event {
        EventType type = EventType.CHARACTERHIGHLIGHT;

        void CharacterHighlight() {

        }
    }

    class CharacterUnhiglight extends Event {
        EventType type = EventType.CHARACTERUNHIGHLIGHT;
        Image sprite;

        void CharacterUnhiglight(Image inputSprite) {
            sprite = inputSprite;
        }

        void performEvent() {
            sprite.act(0);
        }
    }

    class CharacterSpriteChange extends Event {
        EventType type = EventType.CHARACTERSPRITECHANGE;
        Image sprite;
        String characterId;

        void CharacterSpriteChange(Image inputSprite, String textureId) {
            sprite = inputSprite;
            characterId = textureId;
        }

        void performEvent() {
            sprite.setDrawable(null);
        }
    }

    class Event {
        EventType type = EventType.NONE;
    }

    DialogueEvent(int numberOfEvents) {
        eventArray = new Event[numberOfEvents];
    }

    public void performEvents() {

    }



    
}
