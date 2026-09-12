package model.party;

import model.people.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public class Anniversary extends Event implements Fancy{
    private String type;
    private int year;
    private boolean memoryShowcase;


    public Anniversary(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen, String type, int year, boolean memoryShowcase) {
        super(name, theme, guestList, location, time, ageLimit, isOpen);
        this.memoryShowcase = memoryShowcase;
        this.type = type;
        this.year = year;
    }

    public Anniversary(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this(name, theme, guestList, location, time, ageLimit, isOpen, "type", 10, true);
    }

    //dummy constructor
    public Anniversary() {
        this("name", "theme", new ArrayList<>(), "location", LocalDate.now(), false, false,"type", 10, true);
    }

    //getters
    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public boolean isMemoryShowcase() {
        return memoryShowcase;
    }

    //setters
    public void setType(String type) {
        this.type = type;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setShowcase(boolean memoryShowcase) {
        this.memoryShowcase = memoryShowcase;
    }


    @Override
    public String fullStatus() {
        return "Anniversary{"+ super.fullStatus() +
                "type='" + type + '\'' +
                ", year=" + year +
                ", memoryShowcase=" + memoryShowcase +
                '}';
    }

    @Override
    public String toString() {
        return "On " + getTime() + ", " + year + " " +  type + " Anniversary party named " + getName() +
                " with theme " + getTheme() +
                " is at " + getLocation() + '\'' +
                ", status: [ageLimit= " + hasAgeLimit() +
                ", open to everyone=" + isOpen() + " ].";

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




}
