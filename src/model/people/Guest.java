package model.people;

import model.party.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

import view.BFF;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public class Guest extends User{
    private BFF helper;
    //Parties that user will go, because he or she is invited and gets approved for reservation
    private Map<User, Event>  partyPlanned;      //Key is the host, Event is the party tha plan to go
    private Map<User, Event> reservation;    //Party that the user wants to go, no matter if he or she successfully reserved the party or not
    private String gift;
    //"Available in v2"
    private List<User> fakeAccounts;     //if a guest wants to bring anyone who is not registered in the system, such as his or her children.

    public Guest(String name, String email, String password, Map<User, Event> partyPlanned, Map<User, Event> reservation, String gift, List<User> fakeAccounts) {
        super(name, email, password);
        this.partyPlanned = partyPlanned;
        this.reservation = reservation;
        this.gift = gift;
        this.fakeAccounts = fakeAccounts;
        this.helper = new BFF();
    }

    public Guest(String name, String email, String password) {
        this(name, email, password,new HashMap<>(), new HashMap<>(), "gift", new ArrayList<>());
    }

    //getters

    public Map<User, Event> getPartyPlanned() {
        return partyPlanned;
    }

    public Map<User, Event> getReservation() {
        return reservation;
    }

    public String getGift() {
        return gift;
    }

    public List<User> getFakeAccounts() {
        return fakeAccounts;
    }


    //setters

    public void setPartyPlanned(Map<User, Event> partyPlanned) {this.partyPlanned = partyPlanned;}

    public void setReservation(Map<User, Event> reservation) {
        this.reservation = reservation;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public void setFakeAccounts(List<User> fakeAccounts) {
        this.fakeAccounts = fakeAccounts;
    }


/*    Available in v2
    //accept the invitation sent by the Host and add the event to the reservation
    public String acceptInvitation(Event chose){
        boolean add = false;
            for (var pair : invited.entrySet()){
                Event value = pair.getValue();
                User key = pair.getKey();
                if (chose.equals(value)) {
                    add = true;
                    invited.remove(key);
                    reservation.put(key, value);
                }
            }
        String result = "Sorry. You failed to accept the invitation from " + chose.getName() + ". Please try again.";
        if (add){
            result = "Congrats! You successfully accepted an invitation from " + chose.getName() + ". Looking forward to see you there!";
        }
        return result;
    }



    //check the invitation received
    public void check_invitations() {
        boolean want = false;
        helper.print("Here are the invitations you received!");
            for (var pair : invited.entrySet()) {
                User host = pair.getKey();
                Event party = pair.getValue();
                helper.print(party.getName() + " hosted by " + host.getName());
            }
            want = helper.inputBoolean("Do you want to see more details?");
            //let user see more details about the party
            while (want) {
                String host_name = helper.input("Which party do you want to see more details? (Please enter the name of the host. It is case sensitive)");
                for (var pair : invited.entrySet()) {
                    User host = pair.getKey();
                    Event party = pair.getValue();
                    String target_name = host.getName();
                    if (target_name.equals(host_name)) {
                        helper.print(invited.get(host));
                    }
                    want = helper.inputBoolean("Do you want to see more details about other party?");
                }
            }
        }
 */

    @Override
    public String toString() {
        return "Guest " + super.toString() +
                " prepare gift " + gift;
    }
}
