package model.party;
import model.people.User;
import java.time.LocalDate;
import java.util.List;

/**
 * Class Event
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public abstract class Event implements User_Party{
    private String name;
    private String theme;
    //private EventType occasion;
    private List<User> guestList;
    private String location;
    //private String menu;
    private LocalDate time;
    private boolean ageLimit;
    private boolean isOpen;
    private User host;
    private List<String> guestList_Holder;


    //Constructors
    public Event(String name, String theme, List<User> guestList, String location, LocalDate time, boolean ageLimit, boolean isOpen) {
        this.name = name;
        this.theme = theme;
        //this.occasion = occasion;
        this.guestList = guestList;
        this.location = location;
        //this.menu = menu;
        this.time = time;
        this.ageLimit = ageLimit;
        this.isOpen = isOpen;
    }

    public Event(String name, String theme, List<User> guestList, String location, LocalDate time){
        this(name, theme, guestList, location, time, false, false);
    }

    //getters
    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isAgeLimit() {
        return ageLimit;
    }

    public User getHost() {return host;}
    //public EventType getOccasion() {return occasion;}

    public List<User> getGuestList() {
        return guestList;
    }

    public String getLocation() {
        return location;
    }

    //public String getMenu() {return menu;}

    public LocalDate getTime() {
        return time;
    }

    public boolean hasAgeLimit() {
        return ageLimit;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<String> getGuestList_Holder() {return guestList_Holder;}

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setHost(User host) {this.host = host;}

//public void setOccasion(EventType occasion) {this.occasion = occasion;}

    public void setGuestList(List<User> guestList) {this.guestList = guestList;}

    public void setLocation(String location) {
        this.location = location;
    }

    //public void setMenu(String menu) {this.menu = menu;}

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public void setAgeLimit(boolean ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setGuestList_Holder(List<String> guestList_Holder) {this.guestList_Holder = guestList_Holder;}

    //equals and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (ageLimit != event.ageLimit) return false;
        if (isOpen != event.isOpen) return false;
        if (!name.equals(event.name)) return false;
        if (!theme.equals(event.theme)) return false;
        //if (occasion != event.occasion) return false;
        if (!guestList.equals(event.guestList)) return false;
        if (!location.equals(event.location)) return false;
        //if (!menu.equals(event.menu)) return false;
        return time.equals(event.time);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + theme.hashCode();
       // result = 31 * result + occasion.hashCode();
        result = 31 * result + guestList.hashCode();
        result = 31 * result + location.hashCode();
        //result = 31 * result + menu.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + (ageLimit ? 1 : 0);
        result = 31 * result + (isOpen ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return   "On " + time + ", this party named " + name +
                " with theme " + theme +
                " is at " + location + '\'' +
                ", status: [ageLimit= " + ageLimit +
                ", open to everyone=" + isOpen + " ].";
    }

    public String fullStatus() {
        String guests = "";
        for(User u:guestList){
            guests += " " + u.getName();
        }
        return "Event{" +
                "name='" + name + '\'' +
                ", theme='" + theme + '\'' +
                ", guestList=" + guests +
                ", location='" + location + '\'' +
                //", menu='" + menu + '\'' +
                ", time=" + time +
                ", ageLimit=" + ageLimit +
                ", isOpen=" + isOpen +
                '}';
    }
}
