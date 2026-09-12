package controller;

import model.party.EventType;
import model.people.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 5/3/23
 */
//referenced from the StoreMenu in A11
public enum PartySteps {
    NAME,
    THEME,
    GUEST_LIST,
    LOCATION,
    TIME,
    AGE_LIMIT,
    OPEN,
    QUIT;


    public static String getType() {
        String prompt = "\nHere are the steps to create a party!\n";
        for(PartySteps p : PartySteps.values()){
            prompt += "\n" + (p.ordinal()) + ":" +  p.toString();
        }
        prompt+="\nWhich step do you want to start?\n";
        return prompt;
    }


}
