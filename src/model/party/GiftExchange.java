package model.party;

import model.people.Guest;
import model.people.User;

import java.time.LocalDate;
import java.util.*;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public class GiftExchange extends Event implements Gift{
    private int limit;      //this sets a spending limit for each gift
    private Map<User, String> afterExchange;     //the map that stores the gifts exchanged


    public GiftExchange(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen, int limit) {
        super(name, theme, guestList, location, time, ageLimit, isOpen);
        this.limit = limit;
        afterExchange = new HashMap<>();
    }

    public GiftExchange(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this(name, theme, guestList, location, time, ageLimit, isOpen, 50);
    }

    //dummy constructor
    public GiftExchange() {
        this("name", "theme", new ArrayList<>(), "location", LocalDate.now(), false, false, 50);
    }

    //getter
    public int getLimit() {
        return limit;
    }

    //setter
    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Map<User, String> exchangeGift(){
        //put all the gifts into one list
        Map<User, String> allG = collectGift(getGuestList());
        List<String> giftList = new ArrayList<>();
        for(String s: allG.values()){
            giftList.add(s);
        }

        //shuffle the list and assign them back to the guests, always assign the first item in the list back to the User
        Collections.shuffle(giftList, new Random(3));
        //int length = giftList.size()-1;
        while(!(giftList.isEmpty())){
            for(User u: getGuestList()){
                while (allG.get(u).equalsIgnoreCase(giftList.get(0))){
                    //if the value after shuffled is still equal, shuffle again
                    //if two users bring the same gift, this also makes sure that they get different gifts.
                    Collections.shuffle(giftList, new Random(3));
                }
                allG.replace(u, allG.get(u),giftList.get(0));
                //after assignment, remove this item so that each item is unique and only assigned to different person
                giftList.remove(giftList.get(0));

            }

        }
        return allG;
    }


    //enter a user, after gift exchange, reveal the person who originally brings the gift that this user currently has.
    public User reveal(User u){
        String gift = null;
        User target = null;
        if (u instanceof Guest){
            gift = ((Guest) u).getGift();
        }
        for(var pair: collectGift(getGuestList()).entrySet()){
            if (pair.getValue().equalsIgnoreCase(gift)){
                target = pair.getKey();

            }

        }
        return target;

    }

    //toString
    @Override
    public String fullStatus() {
        return "GiftExchange{" + super.fullStatus() +
                ",limit=" + limit +
                ", afterExchange=" + afterExchange +
                '}';
    }

    @Override
    public String toString() {
        return "On " + getTime() + ", " + " GiftExchange party named " + getName() +
                " with theme " + getTheme() +
                " is at " + getLocation() + '\'' +
                ", status: [ageLimit= " + hasAgeLimit() +
                ", open to everyone=" + isOpen() + " ].";
    }


    //collect and returns the gifts brought by all the guests
    @Override
    public Map<User, String> collectGift(List<User> allGuests) {
        Map<User, String> giftMap = new HashMap<>();
        for (User u: allGuests){
            if (u instanceof Guest){
                giftMap.put(u, ((Guest) u).getGift());
            }

        }
        return giftMap;
    }
}
