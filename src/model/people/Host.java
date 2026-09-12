package model.people;

import model.party.*;

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
public class Host extends Guest implements Gift{
    private List<Event> partyHosting;
    private List<String> partyHosting_holder;


    //map of guests that are  invited but have not accepted the invitation from host
    //private Map<User, Event> pendingGuests;


    //map of guests who need approval from the host to attend the party
    private Map<User, Event> needApprovalGuests;



    public Host(String name, String email, String password, Map<User, Event> invited, Map<User, Event> reservation, String gift, List<User> fakeAccounts, List<Event> partyHosting, Map<User, Event> needApprovalGuests) {
        super(name, email, password, invited, reservation, gift, fakeAccounts);
        this.partyHosting = partyHosting;
        //this.pendingGuests = pendingGuests;
        this.needApprovalGuests = needApprovalGuests;
    }


    public Host(String name, String email, String password) {
        this(name, email, password, new HashMap<>(), new HashMap<>(), "gift", new ArrayList<>(),new ArrayList<>(),new HashMap<>());
    }


    //getters
    public List<Event> getPartyHosting() {
        return partyHosting;
    }

    //public Map<User, Event>  getPendingGuests() {return pendingGuests;}

    public Map<User, Event> getNeedApprovalGuests() {
        return needApprovalGuests;
    }

    public List<String> getPartyHosting_holder() {
        return partyHosting_holder;
    }

    //setters
    public void setPartyHosting(List<Event> partyHosting) {
        this.partyHosting = partyHosting;
    }

    //public void setPendingGuests(Map<User, Event>  pendingGuests) {this.pendingGuests = pendingGuests;}

    public void setNeedApprovalGuests(Map<User, Event> needApprovalGuests) {
        this.needApprovalGuests = needApprovalGuests;
    }

    public void setPartyHosting_holder(List<String> partyHosting_holder) {
        this.partyHosting_holder = partyHosting_holder;
    }

    //remove the guest from the guest list
    public boolean removeGuest(User u, Event e){
        boolean removed = false;
        if (e.getGuestList().remove(u)){
            removed = true;
        }

        return removed;
    }
/*
    //check the pending guest list and whether the guests accept the invitation
    public void check_pending() {
        System.out.println("Here are the guests who have not accepted the invitation");
            for(var pair: getPendingGuests().entrySet()){
                User guest = pair.getKey();
                Event party = pair.getValue();
                System.out.println("You invited " + guest.getName() + " to " + party.getName());
            }
    }

 */

    public void addNeedApprovalGuests(User u, Event p){
        needApprovalGuests.put(u,p);
    }
    //let the host check the reservations received
    public void reservations_received() {
        System.out.println("Here are the guests who want to reserve a party that you are hosting");
        System.out.println(getNeedApprovalGuests());
        for(var pair: getNeedApprovalGuests().entrySet()){
            User guest = pair.getKey();
            Event party = pair.getValue();
            System.out.println("Guest " + guest.getName() + " wants to join " + party.getName());
        }
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

    @Override
    public String toString() {
        return "Host name is " + super.getName() +
                " who is hosting " + partyHosting.size() + " parties.";
    }
}
