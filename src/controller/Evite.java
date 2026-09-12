package controller;

import controller.setup.FileProcessor;
import model.party.*;
import model.people.*;
import view.BFF;
import java.time.LocalDate;
import java.util.*;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 5/1/23
 */
public class Evite {
    private User currentUser;    // the user who currently logged in the account
    private final Map<User, List<Event>> allParty;     //key is the user who created the party, value is Event
    private Event currentParty;     //the party that the user is currently editing or creating
    private final BFF helper;
    private final Map<String, User> allUsers;    //key is the username, and value is User


    public Evite() {
        this.currentUser = null;
        this.helper = new BFF();
        this.allParty = new HashMap<>();
        this.allUsers = new HashMap<>();
        this.currentParty = null;
        readData();
    }

    //reading data from the fileProcessor
    private void readData() {
        List<User_Party> userData = FileProcessor.readFile_all().get("User");
        for(User_Party u:userData) {
            String username = ((User) u).getName();
            if (u instanceof Host) {
                allUsers.put(username, (Host) u);
            } else {
                allUsers.put(username, (Guest) u);
            }
        }
        List<User_Party> partyData = FileProcessor.readFile_all().get("Party");
        helper.print(partyData.size());
        for(User_Party p:partyData){
            if (p instanceof Event){
                User host = ((Event) p).getHost();
                if (allParty.containsKey(host)){
                    allParty.get(host).add((Event) p);
                }else{
                    List<Event> party_one_host = new ArrayList<>();
                    party_one_host.add((Event) p);
                    allParty.put(host, party_one_host);
                }
            }
        }
        //fulfillPendingGuests();
    }

    /*
    //fill out the list of pending guests for hosts
    private void fulfillPendingGuests() {
        for(User u: allUsers.values()){
            if (u instanceof Host){
                List<Event> partyHosting = ((Host) u).getPartyHosting();
                for(Event e:partyHosting){
                    for(User guest:e.getGuestList()){
                        ((Host) u).getPendingGuests().put(guest, e);
                    }
                }
            }
        }
    }

     */

    //run the system and asks what users want to do
    public void run() {
        boolean quit = false;
        while (!quit) {
            System.out.println(EviteMenu.getMenu());
            int num = helper.inputInt("What would you like to do(Enter a number)?", 0, EviteMenu.values().length-1);
            EviteMenu option = EviteMenu.values()[num];
            switch (option) {
                case LOGIN_CREATE: login_creat();break;
                case LOG_OUT: log_out();break;
                case PLAN_EDIT_A_PARTY: plan_edit_party();break;
                case RESERVE_A_PARTY: reserve();break;
                case INVITE_GUEST: invite_guest();break;
                //case ACCPET_INVITATIONS: accept_invitations();break;
                //case VIEW_EDIT_PARTY: view_edit();break;   combine this to  PLAN_A_PARTY
                // case INVITATIONS_RESERVATIONS: invitations_reservations     split this into two parts
                //case INVITATIONS_INVITED: invitations_invited();break;
                case CHECK_PARTIES_PLANNED_GO: check_parties_going();break;
                case VIEW_ALL_PARTIES: view_all_parties();break;
                case CHECK_ACCEPT_RESERVATIONS: check_accept_reservations();break;
                case UPGRADE: upgrade();break;
                case EDIT_USER: edit_user(); break;
                case SAVE_DATA: save_data(); break;
                case QUIT: quit = true;break;
            }
        }
        System.out.println("Hope to see you again!");
    }

    private void check_parties_going() {
        if((currentUser != null) && (currentUser instanceof Guest)){
           for(var pair:((Guest) currentUser).getPartyPlanned().entrySet()) {
               User host = pair.getKey();
               Event party = pair.getValue();
               helper.print(party.getName() + " hosted by " + host.getName());
           }
        }else{
            helper.print("Please log in to proceed!");

        }


    }


    private void invite_guest() {
        if((currentUser != null) && (currentUser instanceof Host)){
            boolean find = false;
            helper.print("Here is a list of parties you are hosting");
            for(Event party:((Host) currentUser).getPartyHosting()){
                helper.print(party);
            }
            String party_name = helper.input("Which party do you want to invite guest for?(Please enter the name of the party, which is sensitive)");
            String guest_name = helper.input("Who do you want to invite?(Please enter the name of the guest, which is sensitive)");
            for(Event party:((Host) currentUser).getPartyHosting()){
                if(party.getName().equals(party_name)){
                    for(User guest: allUsers.values()){
                        if(guest.getName().equals(guest_name)){
                            party.getGuestList().add(guest);
                            ((Guest)guest).getPartyPlanned().put(currentUser,party);
                            find = true;

                        }
                    }
                }
            }
            if(!find){
                helper.print("Sorry, we did not find the party or the guest in our system, Please try again.");
            }
        }else{
            helper.print("Please either log in or upgrade to host account!");
        }
    }

    //save all User data to userFile, save all Party data to partyFile
    private void save_data() {
        List<Event> parties = new ArrayList<>();
        for(var pair: allParty.entrySet()){
            for(Event e: pair.getValue()){
                parties.add(e);
            }
            System.out.println(pair.getValue());
        }
        FileProcessor.writePartyToFile("TwylaPartyData", parties);

        List<User> users = new ArrayList<>();
        for(var pair: allUsers.entrySet()){
            users.add(pair.getValue());
        }
        FileProcessor.writeUserToFile("TwylaUserData", users);
    }


    //let user decide if they want to check the reservations sent to guests or reservations received
    private void check_accept_reservations() {
        if (currentUser != null) {
            boolean sent = helper.inputBoolean("Do you want to check the reservations sent to hosts?");
            if (sent) {
                reservations_sent();
            }
            boolean received = helper.inputBoolean("Do you want to check the reservations received from other users?");
             if (received) {     //check the reservations received and decide if want to accept
                if  (currentUser instanceof Host){
                    ((Host) currentUser).reservations_received();
                    accept_reservations();
                }
            }
        }else{helper.print("Please log in your account to proceed!");}
    }


    //accept the reservation from other guests
    private void accept_reservations(){
        boolean add = false;
        boolean find = false;
        while(add){
            String accept_guest = helper.input("Which guest do you want to accept? (Please enter the name of the guest. It is case sensitive)");
            // accept the reservation of a guest by removing the guest from needApprovalGuests and add the guest to the party guestMap
            Map<User, Event> need_approval = ((Host) currentUser).getNeedApprovalGuests();
            for(var pair:need_approval.entrySet()){
                if(pair.getKey().getName().equals(accept_guest)){
                    find = true;
                    User guest = pair.getKey();
                    Event party_want = need_approval.get(guest);
                    party_want.getGuestList().add(guest);   //add the guest
                    need_approval.remove(guest);  //remove the user from need approval map
                    add = helper.inputBoolean("Do you want to continue accepting more reservations from guests?");
                }
            }
            if(!find){
                helper.print("Sorry, we can not find the guest in our system. Please try again!");
            }
        }
    }

/*
    //let user decide if they want to check the invitations sent to guests or invitations received
    private void invitations_invited() {
        if (currentUser != null) {
            boolean invitations_sent = helper.inputBoolean("Do you want to check the invitations sent to guests?");
            if (invitations_sent) {
                if (currentUser instanceof Host) {
                    ((Host) currentUser).check_pending();
                }
            }
            boolean invited = helper.inputBoolean("Do you want to check the invitations received?");
            if (invited) {
                if  (currentUser instanceof Guest){
                    ((Guest) currentUser).check_invitations();
                }
            }
        }else{
            helper.print("Please log in your account to proceed!");
        }
    }

 */

/*
    //see the invitations received, and accept the invitation
    private void accept_invitations() {
        if (currentUser != null) {
            if ((currentUser instanceof Host) || (currentUser instanceof Guest)){
                ((Guest) currentUser).check_invitations();
                Map<User,Event> invited =  ((Guest) currentUser).getInvited();
                //the user accepts the invitation
                String host_name = helper.input("Which invitation do you want to accept? (Please enter the name of the host. It is case sensitive)");
                boolean find = false;
                for(var pair: invited.entrySet()) {
                    User host = pair.getKey();
                    Event party = pair.getValue();
                    String target_name = host.getName();
                    if (target_name.equals(host_name)){
                        find = true;
                        //add the party to the reservation and remove the party from invited
                        ((Guest) currentUser).acceptInvitation(party);
                        // remove the user from the pendingGuests and add him or her to the party's guestList
                        if (host instanceof Host){
                            ((Host) host).getPendingGuests().remove(currentUser);
                        }
                        party.getGuestList().add(currentUser);
                        helper.print("Congradulations! You are good to go! Be prepared for your party " + party.getName());

                    }
                }
                if(!find){
                    helper.print("The name you entered is not found in our system. Please try again!");
                }
            }
        }else{helper.print("Please log in your account to proceed!");}
    }

 */


    //edit the user account information
    private void edit_user() {
        if (currentUser != null) {
            String change = helper.inputWord("Which information do you want to edit?(name, password, email)", "name", "email", "password");
            if (change.equalsIgnoreCase("name")) {    //change name
                String name = helper.input("What is your new name?");
                currentUser.setPassword(name);

            } else if (change.equalsIgnoreCase("email")) {  //change email
                String email = helper.input("What is your new email?");
                currentUser.setPassword(email);

            } else {      //change password
                String password = helper.input("What is your new password?");
                currentUser.setPassword(password);
            }
        }else{
            helper.print("Please log in your account to proceed!");

        }
    }



    //check the reservations that needs approval from the host
    private void reservations_sent() {
            List<Event> can_go = new ArrayList<>();
            List<Event> pending = new ArrayList<>();
            if ((currentUser instanceof Host) || (currentUser instanceof Guest)){
                Map<User, Event> reservation = ((Guest) currentUser).getReservation();
                for(var pair: reservation.entrySet()){
                    User host = pair.getKey();
                    Event party = pair.getValue();
                    if (party.isOpen()){   //the party is open to everyone
                        can_go.add(party);
                    }else{     //the party is not open
                        List<User> guests = party.getGuestList();
                        for(User u: guests){   //check if the user is in the guestList, if not, the host has not approved the reservation yet
                            if (u.equals(currentUser)){can_go.add(party);
                            }else{pending.add(party);}}}
                }
            }
            helper.print("Here is a list of parties that you successfully reserved!");
            for(Event e: can_go){helper.print("  "+ e);}

            helper.print("Here is a list of parties that are still waiting the host to approve");
            for(Event e: can_go){helper.print("  "+ e);}
    }



    //upgrade from a guest account to host account, requires log in
    private void upgrade() {
        if (currentUser != null){     //already logged in
            if (currentUser instanceof Host){
                helper.print("Your account is currently upgraded. You do not need to upgrade!");
            }else{
                boolean pay = helper.inputBoolean("Would you like to pay 25 dollars to upgrade to host account?");
                if(pay){   //upgrade
                    String name = currentUser.getName();
                    String email = currentUser.getEmail();
                    String password = currentUser.getPassword();
                    Host host = new Host(name, email, password);
                    currentUser = host;
                    allUsers.replace(name, allUsers.get(name), host);     //upgrade in the all user map
                    helper.print("You have successfully upgraded to the Premium. Now you can host parties and invite your friends!");
                }
            }

        }else{    //not logged in
            helper.print("Please log in your account to upgrade!");
        }

    }


    //private void invitations_reservations() {}



    //display all parties in the system
    private void view_all_parties() {
        helper.print("Here is the list of all parties");
        for(var pair: allParty.entrySet()){
            for(Event party:  pair.getValue()){
                String party_name = party.getName();
                String host_name = pair.getKey().getName();
                helper.print(party_name + " Hosted by " + host_name);

            }
        }
    }


//let the host view and choose the party they are hosting and want to edit
// it requires the user to be a host and log in
    private boolean view_edit() {
        boolean find = false;
        if (currentUser != null && currentUser instanceof Host) {   //requires log in and being a host
            //display the parties hosting
            List<Event> partyHosting = ((Host) currentUser).getPartyHosting();
            for (Event e : partyHosting) {
                helper.print(e);
            }
            String want_edit = helper.input("Please enter the name of the party that you want to edit. It is case-sensitive.");
            for (Event e : partyHosting) {
                if (want_edit.equals(e.getName())) {  //find the party that user wants to edit in the system
                    find = true;
                    helper.print("Here is the status of the party");
                    helper.print(e.fullStatus());
                    currentParty = e;    //make currentParty the party that user wants to edit
                }
            }
            if (!find){ //can not find the party in the system
                helper.print("The party you entered does not exist in our system, please try again!");

            }
        }else{
            helper.print("You need to log in a host account to view and edit party hosting. If not, please either log in or upgrade to host account.");
        }

        return find;

    }


    //reserve a party, requires log in.
    //if it is open to everyone, if not, a request will be sent to the host, the guest needs to wait the host to approve.
    private void reserve() {
        if (currentUser != null) {   //checks if the user is logged in
            boolean find = false;
            view_all_parties();
            String party_want = helper.input("Please enter the name of the party that you want to reserve. It is case-sensitive.");
            for (var pair : allParty.entrySet()) {
                User host = pair.getKey();
                for(Event party: pair.getValue()){       //Event party = pair.getValue();
                    if (party_want.equals(party.getName())) {   //find the party_want in the system
                        find = true;
                        if (currentUser instanceof Guest) {
                            ((Guest) currentUser).getReservation().put(host, party);    //add the party to user's reservation map
                        }
                        if (party.isOpen()) {   //if the party is open to everyone, the guest can directly reserve
                            party.getGuestList().add(currentUser);       //add the user to the guestList
                            helper.print("Congrats! You successfully reserved the party: " + party.getName() + ". Looking forward to see you there!");
                        } else {    //the party is private and needs approval from host.
                            //send the request to the host by putting the user and party into the needApprovalGuest map
                            if (host instanceof Host) {
                                ((Host) host).addNeedApprovalGuests(currentUser,party);
                                helper.print("Guest need to be approved ");
                                helper.print(((Host) host).getNeedApprovalGuests());
                                helper.print("Host " + host);
                            }
                            helper.print("This event is private! Please wait for the host " + host.getName() + " to approve your reservation!");
                        }
                    }
                }
            }
            if (!find){//can not find the party in the system
                helper.print("The party you entered does not exist in our system, please try again!");
            }
        }else{
            helper.print("Please log in to reserve a party!");
        }
    }


    //plan a party, requires the account that is logged in is host account, otherwise need to upgrade.
    private void plan_edit_party() {
        if ((currentUser instanceof Host) && (currentUser != null)) {   //make sure that the User is logged in and a host. Guest can not plan a party
            //aks if the user want to continue edit existing party or create new party
            boolean edit = helper.inputBoolean("Do you want to continue edit an existing party?");
            boolean find = false;
            if (edit) {    //want to edit existing party
                find = view_edit();
            }else{find = true;}
            if (find) {
                boolean quit = false;
                while (!quit) {    //ask which party the user wants to create
                    helper.print(EventType.getType());
                    int option = helper.inputInt("What is the type of party that you want to plan or edit(Enter a number)?", 0, EventType.values().length - 1);
                    EventType chosen_event = EventType.values()[option];
                    switch (chosen_event) {
                        case BIRTHDAY: birthday(edit);
                            break;
                        case ANNIVERSARY: anniversary(edit);
                            break;
                        case GRADUATION: graduation(edit);
                            break;
                        case WEDDING: wedding(edit);
                            break;
                        case GIFT_EXCHANGE: gift_exchange(edit);
                            break;
                        case QUIT: quit = true;
                            break;
                    }
                }
            }
        }else{helper.print("Your account is not a host account. You can't plan a party. Please upgrade to host account!");}

    }


    //continue to create the party by setting more details and updating the system after the party is created
    private void continue_create(boolean edit) {
        party_steps();
        if (!edit) {
            //add the party to the database
            if (allParty.containsKey(currentUser)) {
                allParty.get(currentUser).add(currentParty);
            } else {
                List<Event> party_one_host = new ArrayList<>();
                party_one_host.add(currentParty);
                allParty.put(currentUser, party_one_host);
            }
            if (currentUser instanceof Host) {
                ((Host) currentUser).getPartyHosting().add(currentParty);     //add the party to the list of parties that the user is hosting
            }
        }
    }

    //uses the methods implemented in the fancy interface
    private void fancy(Event event) {
        boolean photographer = helper.inputBoolean("Would you like to hire photographers for you party?");
        boolean champagneFountain = helper.inputBoolean("Would you like to have Champagne Fountain for your party?");
        if (event instanceof Wedding){
            ((Wedding) event).champagneFountain(champagneFountain);
            ((Wedding) event).hirePhotographer(photographer);
        }
        if (event instanceof Anniversary){
            ((Anniversary) event).champagneFountain(champagneFountain);
            ((Anniversary) event).hirePhotographer(photographer);
        }

    }

    //create the wedding object based on User's choice, ask about registry, then call continue_create to add more details
    private void wedding(boolean edit) {
        if (!edit) {   //the user wants to create a new party
            Wedding event = new Wedding();
            currentParty = event;
        }
        if (currentParty instanceof Wedding) {
            boolean add = true;
            List<String> registry_planning = new ArrayList<>();
            while (add) {
                String item = helper.input("What is the registry item that you want to provide to the guests?");
                registry_planning.add(item);
                helper.print("Here is the registry list.");
                for (String s : registry_planning) {
                    helper.print(s);
                }
                add = helper.inputBoolean("Do you want to continue to add more items?");
                ((Wedding) currentParty).setRegistry(registry_planning);
            }


            boolean first_dance = helper.inputBoolean("Do you want to prepare first dance?");
            boolean tossBouquet = helper.inputBoolean("Do you plan to toss bouquet?");
            ((Wedding) currentParty).setRegistry(registry_planning);
            ((Wedding) currentParty).firstDance(first_dance);
            ((Wedding) currentParty).tossBouquet(tossBouquet);

            fancy(currentParty);    //ask user if he or she wants to implement methods in Fancy interface.
            continue_create(edit);
        }
    }

    //create the anniversary object based on User's choice, set specific details different occasion, then call continue_create to add more details
    private void anniversary(boolean edit) {
        if (!edit) {   //the user wants to create a new party
            Anniversary event = new Anniversary();
            currentParty = event;
        }
        if (currentParty instanceof Anniversary) {
            String type = helper.input("Which type of anniversary are you planning?");
            int year = helper.inputInt("Which year of anniversary are you planning?", 0, 100);
            boolean memoryShowcase = helper.inputBoolean("Do you plan to give memory showcase?");
            ((Anniversary) currentParty).setType(type);
            ((Anniversary) currentParty).setYear(year);
            ((Anniversary) currentParty).setShowcase(memoryShowcase);
            fancy(currentParty);    //ask user if he or she wants to implement methods in Fancy interface.
            continue_create(edit);
        }
    }

    //create the graduation object based on User's choice, set specific details different occasion, then call continue_create to add more details
    private void graduation(boolean edit) {
        if (!edit) {   //the user wants to create a new party
            Graduation event = new Graduation();
            currentParty = event;
        }
        if (currentParty instanceof Graduation) {
            String type = helper.input("Which type of graduation are you planning?");
            String announcement = helper.input("Which is your graduation announcement?");
            ((Graduation) currentParty).setType(type);
            ((Graduation) currentParty).setGradAnnouncement(announcement);
            continue_create(edit);
        }
    }


    //create the birthday object based on User's choice, set specific details different occasion, then call continue_create to add more details
    private void birthday(boolean edit) {
        if (!edit) {   //the user wants to create a new party
            Birthday event = new Birthday();
            currentParty = event;
        }
        if (currentParty instanceof Birthday) {
            int age = helper.inputInt("Which year of the birthday are you planning?", 0, 100);
            ((Birthday) currentParty).setAge(age);
            helper.print(((Birthday) currentParty).prepareCake());
            continue_create(edit);
        }
    }

    //create the gift_exchange object based on User's choice, set specific details different occasion, then call continue_create to add more details
    private void gift_exchange(boolean edit) {
        if (!edit) {   //the user wants to create a new party
            GiftExchange event = new GiftExchange();
            currentParty = event;
        }
        if (currentParty instanceof GiftExchange) {
            int limit = helper.inputInt("What is the max amount of money each guest can spend on the gift?");
            ((GiftExchange) currentParty).setLimit(limit);
            continue_create(edit);
        }
    }


    //ask the time to carry out the party
    private LocalDate ask_time() {
        LocalDate today = LocalDate.now();
        int year = helper.inputInt("Which year do you plan to carry out the party?", today.getYear(), 2300);
        int month = helper.inputInt("Which month do you plan to carry out the party?", 1,12);
        int date = helper.inputInt("Which day do you plan to carry out the party?", 1,31);
        LocalDate planned_day = LocalDate.of(year, month, date);
    return planned_day;
    }


    //plan the specific details about each party
    private void party_steps() {
        //currentParty = event;
        System.out.println("Now, let's plan more details about your party!");
        boolean quit = false;
        while (!quit) {
            System.out.println(PartySteps.getType());
            int option = helper.inputInt("Which step do you want to start(Enter a number)?", 0, PartySteps.values().length - 1);
            PartySteps chosen = PartySteps.values()[option];
            switch (chosen) {
                case NAME: String name = helper.input("What the name for your party?");
                    currentParty.setName(name);break;
                case TIME: LocalDate time_party = ask_time();
                    currentParty.setTime(time_party);break;
                case THEME: String theme = helper.input("What the theme for your party?");
                    currentParty.setTheme(theme);break;
                case GUEST_LIST: Map<User, Event> guestMap = edit_guestMap();
                /*
                    // Since the guests also need to approve the invitation, so they are added to the pending guests, once they accept, they will be added to the guestList.
                    if(currentUser instanceof Host){
                        ((Host) currentUser).setPendingGuests(guestMap);
                    }
                 */
                    break;
                case LOCATION: String location = helper.input("What the location for your party?");
                    currentParty.setLocation(location);break;
                case AGE_LIMIT: boolean age_limit = helper.inputBoolean("Does this party only allow guests who are above 21?");
                    currentParty.setAgeLimit(age_limit);break;
                case OPEN: boolean open = helper.inputBoolean("Is this party open to everyone");
                    currentParty.setOpen(open);break;
                case QUIT: quit = true;break;
            }
        }
    }

    //ask user the guest that he or she wants to invite, it requires the guest is in the system or one of our Users.
    private Map<User, Event> edit_guestMap() {
        Map<User, Event> guestMap = new HashMap<>();
        boolean invite = true;
        while(invite){
            String name = helper.input("Which guest would you like to invite? Please enter the name of the guest.");
            if (allUsers.containsKey(name)){     //the guest is in your system, invite him or her
                User guest = allUsers.get(name);
                guestMap.put(guest, currentParty);      //add the person to the guestList
                if (guest instanceof Guest){
                    ((Guest) guest).getPartyPlanned().put(currentUser, currentParty);    //add the party to guest's schedule
                }
                invite = helper.inputBoolean("Would you like to continue inviting guest?");

            }else{
                helper.print("The guest you try to invite does not exist in your system. Please try again!");
                invite = false;
            }
        }
        return guestMap;
    }


    //log out the account
    private void log_out() {
        currentUser = null;
        helper.print("You are Logged out!" );
    }


    //check if the user exists in the system, if so, sign in, otherwise create a new account
    private void login_creat() {
        String name = helper.input("Please enter your name: ");
        if (allUsers.containsKey(name)){      //the user exists in the system, sign in. password is case-sensitive
            String password = helper.input("Please enter your password: ");
            if ((password.equals(allUsers.get(name).getPassword()))) {
                currentUser = allUsers.get(name);
                helper.print("You are logged in!");
            }else{
                helper.print("Your password is not correct. Please try again");
            }
        }else{     //the user does not exist in the system, create an account
            boolean create = helper.inputBoolean("You do not have an account in the system. Do you want to create an account?");
            if(create){
                String password_create = helper.input("Please enter your password: ");
                String email = helper.input("Please enter your email: ");
                User user_create = new Guest(name,email,password_create, new HashMap<>(), new HashMap<>(), "", new ArrayList<>());
                allUsers.put(name, user_create);    //add the user to the userMap
                currentUser = user_create;    //log in the user
                helper.print("Congratulations! You already created an account. You are logged in!");

            }
        }
    }

    public static void main(String[] args) {
        Evite haveFun = new Evite();
        haveFun.run();

    }



}
