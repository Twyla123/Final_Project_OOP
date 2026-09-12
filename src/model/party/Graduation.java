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
public class Graduation extends Event{
    private String gradAnnouncement;
    private String type;


    public Graduation(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen, String gradAnnouncement, String type) {
        super(name, theme, guestList, location, time, ageLimit, isOpen);
        this.gradAnnouncement = gradAnnouncement;
        this.type = type;
    }

    public Graduation(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this(name, theme, guestList, location, time, ageLimit, isOpen, "gradAnnouncement", "type");
    }

    //dummy constructor
    public Graduation() {
        this("name", "theme", new ArrayList<>(), "location", LocalDate.now(), false, false, "gradAnnouncement", "type");
    }



    //getters
    public String getGradAnnouncement() {
        return gradAnnouncement;
    }

    public String getType() {
        return type;
    }


    //setters
    public void setGradAnnouncement(String gradAnnouncement) {
        this.gradAnnouncement = gradAnnouncement;
    }

    public void setType(String type) {
        this.type = type;
    }

    //toString


    @Override
    public String toString() {
        return "On " + getTime() + ", " +  type + " Graduation party named " + getName() +
                " with theme " + getTheme() +
                " is at " + getLocation() + '\'' +
                ", status: [ageLimit= " + hasAgeLimit() +
                ", open to everyone=" + isOpen() + " ].";
    }

    @Override
    public String fullStatus() {
        return "Graduation{" + super.fullStatus() +
                "gradAnnouncement='" + gradAnnouncement + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
