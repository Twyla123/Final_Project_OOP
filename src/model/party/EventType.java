package model.party;

import controller.EviteMenu;

/**
 * Enum EventType Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */

//referenced from the StoreMenu in A11
public enum EventType {
    BIRTHDAY,
    ANNIVERSARY,
    GRADUATION,
    WEDDING,
    GIFT_EXCHANGE,
    QUIT;

    //return the String that prints out different types of EventType
    public static String getType() {
        String prompt = "\nHere are different types of parties!\n";
        for(EventType e : EventType.values()){
            prompt += "\n" + (e.ordinal()) + ": "+ e.toString();
        }
        prompt+="\n**********************************************\n";
        return prompt;
    }

}
