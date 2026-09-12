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
public class Birthday extends Event{
    private int age;

    public Birthday(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen, int age) {
        super(name, theme, guestList, location, time, ageLimit, isOpen);
        this.age = age;
    }

    public Birthday(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this(name, theme, guestList, location, time, ageLimit, isOpen, 21);
    }

    //dummy constructor
    public Birthday() {
        this("name", "theme", new ArrayList<>(), "location", LocalDate.now(), false, false, 10);
    }
    //getters
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //additional methods
    public String prepareCake(){
        System.out.println("You cake is prepared! Check it out!");

        return "🍰";
    }

    //toString
    @Override
    public String toString() {
        return "On " + getTime() + ", " + " Birthday party named " + getName() +
                " with theme " + getTheme() +
                " is at " + getLocation() + '\'' +
                ", status: [ageLimit= " + hasAgeLimit() +
                ", open to everyone=" + isOpen() + " ].";

    }


    @Override
    public String fullStatus() {
        return "Birthday{" + super.fullStatus() +
                "age=" + age +
                '}';
    }

}
