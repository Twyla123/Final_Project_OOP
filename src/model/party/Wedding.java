package model.party;

import model.people.Guest;
import model.people.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public class Wedding extends Event implements Fancy{

    private List<String> registry;
    //private String speech;

    public Wedding(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen, List<String> registry) {
        super(name, theme, guestList, location, time, ageLimit, isOpen);
        this.registry = registry;
    }


    //dummy constructor
    public Wedding() {
        this("name", "theme", new ArrayList<>(), "location", LocalDate.now(), false, false, new ArrayList<>());
    }

    public Wedding(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this(name, theme, guestList, location, time, ageLimit, isOpen, new ArrayList<>());
    }

    //getters
    //public String getSpeech() {return speech;}

    public List<String> getRegistry() {
        return registry;
    }

    //setters
    //public void setSpeech(String speech) {this.speech = speech;}
    public void setRegistry(List<String> registry) {
        this.registry = registry;
    }


    public void tossBouquet(boolean choice){
        if (choice){
            System.out.println("Bouquet is prepared! Be ready to toss it!");
        }

    }

    public void firstDance(boolean choice){
        if (choice){
            System.out.println("First Dance is prepared!");
        }
    }


    //compare the registry given by the host and gifts collected from guests and return filtered Registry that include items not provided by the guests
    public List<String> compareRegistry(){
        List<String> giftList = new ArrayList<>();
        List<String> filteredRegistry = new ArrayList<>();
        List<User> allGuests = getGuestList();

        //put all gifts into one list
        for (User u: allGuests){
            if (u instanceof Guest){
                giftList.add(((Guest) u).getGift());
            }
        }
        //compare, if the giftList does not contain the gift in registry, then add that gift to Registry.
        for(String gift: registry){
            if (!(giftList.contains(gift))){
                filteredRegistry.add(gift);
            }
        }

        return filteredRegistry;
    }


    //Fancy methods
    @Override
    public boolean hirePhotographer(boolean choice) {
        return choice;
    }

    @Override
    public boolean champagneFountain(boolean choice) {
        return choice;
    }

    //toString
    @Override
    public String toString() {
        return "On " + getTime() + ", " + " Wedding party named " + getName() +
                " with theme " + getTheme() +
                " is at " + getLocation() + '\'' +
                ", status: [ageLimit= " + hasAgeLimit() +
                ", open to everyone=" + isOpen() + " ].";
    }

    @Override
    public String fullStatus() {
        return "Wedding{" + super.fullStatus() +
                "registry=" + registry +
                '}';
    }

}
