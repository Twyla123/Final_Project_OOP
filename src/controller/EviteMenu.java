package controller;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 5/1/23
 */

//referenced from the StoreMenu in A11
public enum EviteMenu {
    LOGIN_CREATE("User Login or create new account"),
    LOG_OUT("Log out"),
    PLAN_EDIT_A_PARTY("Plan or edit a party(need to upgrade)"),
    RESERVE_A_PARTY("Make reservation for a party"),
    //ACCPET_INVITATIONS("Accept the invitations from a host"),
    //VIEW_EDIT_PARTY("View a party or edit it"),
    //INVITATIONS_INVITED("Check the invitations sent to guests or invitations received"),
    INVITE_GUEST("Invite a guest to your party."),
    CHECK_PARTIES_PLANNED_GO("Display parties that you plan to go"),
    VIEW_ALL_PARTIES("Display all the parties in the system"),
    CHECK_ACCEPT_RESERVATIONS("Check the reservations received from other users and decide if want to accept"),
    UPGRADE("Upgrade from a guest account to host account to plan a party."),
    EDIT_USER("Make changes to the User's account"),
    SAVE_DATA("Save the all the data in the system to files."),
    QUIT("Quit");

    private String description;
    private EviteMenu(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }


    //return the String that prints out the menu of the Evite System
    public static String getMenu() {
        String prompt = "\nWelcome to Evite System!\n";
        for(EviteMenu m : EviteMenu.values()){
            prompt += "\n" + (m.ordinal()) + ": " + m.getDescription();
        }
        prompt+="\n**********************************************\n";
        return prompt;
    }


}
