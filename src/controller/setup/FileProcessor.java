package controller.setup;

import model.party.*;
import model.people.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.io.PrintWriter;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 5/4/23
 */
public class FileProcessor {
    private static final String PARTY_FILE = "/Users/twylazhang/IdeaProjects/codeItp265/Final_Project/src/controller/setup/party.csv";
    private static final String USER_FILE = "/Users/twylazhang/IdeaProjects/codeItp265/Final_Project/src/controller/setup/user.csv";

    private static final List<Event> ALLPARTY = new ArrayList<>();
    private static final List<User> ALLUSER = new ArrayList<>();


    public static void main(String[] args) {
        //testing the map
        readFile_all();
        //System.out.println("Testing the Party: " );
        int size = ALLPARTY.size();
        System.out.println("There are " + size + " entries in the Party");
        for(Event p: ALLPARTY){
            System.out.println(p);
        }
        System.out.println("Testing the User: " );
        int size2 = ALLUSER.size();
        System.out.println("There are " + size2 + " entries in the User");
        for(User e: ALLUSER){
            System.out.println(e);
        }

        writePartyToFile("twylaparty", ALLPARTY);
        writeUserToFile("twylauser", ALLUSER);
    }

    //read the file step by step, first reads file and parse simple data, then parse more
    public static Map<String, List<User_Party>> readFile_all() {
        Map<String, List<User_Party>> allData = new HashMap<>();
        List<User_Party> user = new ArrayList<>();
        List<User_Party> event = new ArrayList<>();
        allData.put("User",  readFile_1_User(USER_FILE, user));
        allData.put("Party",  readFile_1_Party(PARTY_FILE, event));

        //must call this first, fulfill the partyHosting for Host, and set Host for each party
        match_replace_partyHosting();
        //fill out the GuestList for each party and invited map for guest
        match_replace_party();
        //fill out the pendingGuest
        //fill_pendingGuest();
        return allData;
    }

    //find the corresponding user in the ALLUSER and replace the uer holder in GuestList with the real user, and update guest's schedule
    private static void match_replace_party() {
        for(Event party: ALLPARTY) {
            for(String guest_name:party.getGuestList_Holder()){
                for(User u: ALLUSER){
                    if (guest_name.equals(u.getName())){
                        //replace the User holder with real user
                        if (party.getGuestList().contains(u)){

                        }else{
                            party.getGuestList().add(u);
                            ((Guest) u).getPartyPlanned().put(party.getHost(),party);
                        }


                        /*
                        //fulfill the invited map for the guest
                        if (u instanceof Guest){
                            ((Guest) u).getInvited().put(party.getHost(),party);
                        }
                         */
                    }
                }
            }
        }
    }


    //match and replace data for partyHosting. Using the name to find the corresponding party in ALLPARTY and put that into the PartyHosting for Host
    private static void match_replace_partyHosting() {
        for(User u: ALLUSER){
                if (u instanceof Host){
                    for(String party_name:((Host) u).getPartyHosting_holder()){
                        for(Event party: ALLPARTY){
                            if(party.getName().equals(party_name)){
                                if(((Host) u).getPartyHosting().contains(party)){

                                }else{
                                    ((Host) u).getPartyHosting().add(party);   //add the party to Host's PartyHosting
                                    party.setHost(u);    //setting the host for each party
                                }
                            }
                        }
                    }
                }
            }
    }


    //reads the file for both User and Party, parse the simple data, such as lists, String, boolean, then parse map
    //referenced from A11 StoreFactory
    private static List<User_Party> readFile_1_Party(String file, List<User_Party> allParty) {
        try(FileInputStream fis = new FileInputStream(file);
            Scanner scan = new Scanner(fis))
        {
            if (scan.hasNext()) {
                String header = scan.nextLine();  //catch the header section
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                        Event p = parseParty(line);
                        if (p != null) {
                            ALLPARTY.add(p);
                            allParty.add(p);
                        }
                    }
                }
            else {
                System.err.println("File was empty: " + file);
            }

        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace();
        } catch (IOException e1) {e1.printStackTrace();}
        return allParty;
    }





    private static List<User_Party> readFile_1_User(String file, List<User_Party> allUser) {
        try(FileInputStream fis = new FileInputStream(file);
            Scanner scan = new Scanner(fis))
        {
            if (scan.hasNext()) {
                String header = scan.nextLine();  //catch the header section
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    User u = parseUser(line);
                    if(u != null) {
                        allUser.add(u);
                        ALLUSER.add(u);
                    }
                }
            }
            else {System.err.println("File was empty: " + file);}
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return allUser;
    }





    //parse one line of the CSV file into one Event object, referenced from the A11 StoreFactory
    private static Event parseParty(String line) {
        //party_type,name,theme,guestList,location,menu,time,age limit,isopen,
        Event party = null;
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            String party_type = sc.next();
            String name = sc.next();
            String theme = sc.next();
            List<String> guestList = parseGuest(sc.next());
            String location = sc.next();
            LocalDate time = parseTime(sc.next());
            boolean age_limit = sc.nextBoolean();
            boolean isOpen = sc.nextBoolean();
            //Type,Year,MemoryShowcase,Limit,Registry,gradAnnouncement,guestList
            String type = sc.next();
            if(party_type.equalsIgnoreCase("Anniversary")) {
                int year = sc.nextInt();
                boolean MemoryShowcase = sc.nextBoolean();
                party = new Anniversary(name, theme, new ArrayList<>(),location,time,age_limit,isOpen, type, year, MemoryShowcase);
                party.setGuestList_Holder(guestList);}
            else if(party_type.equalsIgnoreCase("Birthday")) {
                int year = Integer.parseInt(sc.next());
                party = new Birthday(name, theme, new ArrayList<>(),location,time,age_limit,isOpen, year);
                party.setGuestList_Holder(guestList);}
            else if(party_type.equalsIgnoreCase("Gift Exchange")) {
                sc.next(); sc.next(); //skip year, MemoryShowcase
                int limit = sc.nextInt();
                party = new GiftExchange(name, theme, new ArrayList<>(),location,time,age_limit,isOpen, limit);
                party.setGuestList_Holder(guestList);}
            else if(party_type.equalsIgnoreCase("Graduation")) {
                sc.next(); sc.next();  sc.next(); sc.next(); //skip year, MemoryShowcase,Limit,Registry
                String gradAnnouncement = sc.next();
                party = new Graduation(name, theme, new ArrayList<>(),location,time,age_limit,isOpen,gradAnnouncement,type);
                party.setGuestList_Holder(guestList);}
            else if(party_type.equalsIgnoreCase("Wedding")) {
                sc.next();  sc.next(); sc.next(); //skip year, MemoryShowcase,Limit
                List<String> registry = parseRegistry(sc.next());
                party = new Wedding(name, theme, new ArrayList<>(),location,time,age_limit,isOpen,registry);
                party.setGuestList_Holder(guestList);}
            else {System.err.println("Unknown category in file: " + party_type);}
        }
        catch(Exception e) {System.err.println("Error reading line of file: " + line + "\nerror; " + e);}

        return party;
    }


    /*
    private static Event parseSpecific(Event party, String rest) {
        // Type,Year,MemoryShowcase,Limit,Registry,gradAnnouncement
        try {
            Scanner sc = new Scanner(rest);
            sc.useDelimiter(",");
            String type = sc.next();
            int year = Integer.parseInt(sc.next());
            boolean MemoryShowcase = sc.nextBoolean();
            int limit = Integer.parseInt(sc.next());
            List<String> registry = parseRegistry(sc.next());
            String gradAnnouncement = sc.next();
            System.out.println("gradAnnouncement " + gradAnnouncement);
            if(party instanceof Anniversary) {
                ((Anniversary) party).setType(type);
                ((Anniversary) party).setYear(year);
                ((Anniversary) party).setShowcase(MemoryShowcase);
            }
            else if(party instanceof Birthday) {
                ((Birthday) party).setAge(year);}
            else if(party instanceof GiftExchange) {
                ((GiftExchange) party).setLimit(limit);}
            else if(party instanceof Graduation) {
                ((Graduation) party).setGradAnnouncement(gradAnnouncement);
                ((Graduation) party).setType(type);
            }
            else if(party instanceof Wedding) {
                ((Wedding) party).setRegistry(registry);}
            else {System.err.println("Unknown party in file: " + party.getName());}
        }
        catch(Exception e) {System.err.println("Error reading line of file: " + rest + "\nerror; " + e);}
        return party;
    }

     */

//parse the Registry
    private static List<String> parseRegistry(String next) {
        List<String> registry = new ArrayList<>();
        Scanner sc = new Scanner(next);  sc.useDelimiter("/");
        while (sc.hasNext()) {
            String item = sc.next();
            registry.add(item);
        }
        return registry;
    }




    //parse a string of guest into a time
    private static LocalDate parseTime(String next) {
        Scanner sc = new Scanner(next);  sc.useDelimiter("-");
        int year = 0; int month = 0; int date = 0;
        while (sc.hasNext()) {
            year = Integer.parseInt(sc.next());
            month = Integer.parseInt(sc.next());
            date = Integer.parseInt(sc.next());
        }
        LocalDate time = LocalDate.of(year, month, date);
        return time;
    }

    private static List<String> parseGuest(String next) {
        List<String> guestList = new ArrayList<>();
        Scanner sc = new Scanner(next); sc.useDelimiter("/");    //System.out.println("parsing guest"+next);
        while (sc.hasNext()) {
            String guest_name = sc.next();          //System.out.println("parsing guest "+guest_name);
            guestList.add(guest_name);}
        return guestList;
    }



    //parse one line of the CSV file into one User object, referenced from the A11 StoreFactory
    private static User parseUser(String line) {
        //Header: Type,Name,Email,Password,Gift,PartyHosting
        User user = null;
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            String type = sc.next();
            String name = sc.next();
            String email = sc.next();
            String password = sc.next();
            String gift = sc.next();
            if(type.equalsIgnoreCase("Host")) {
                List<String> partyHosting_holder = parsePartyHosting(sc.next());
                user = new Host(name, email, password);
                if(user instanceof  Host){
                    ((Host) user).setPartyHosting_holder(partyHosting_holder);
                    ((Host) user).setGift(gift);}
            }
            else if(type.equalsIgnoreCase("Guest")) {
                user = new Guest(name, email, password);
                if(user instanceof  Guest){
                    ((Guest) user).setGift(gift);}
            }else {System.err.println("Unknown user in file: " + type);}
        }
        catch(Exception e) {System.err.println("Error reading line of file in User data: " + line + "\nerror; " + e);}
        return user;
    }





    private static List<String> parsePartyHosting(String partyHosting) {
        List<String> party = new ArrayList<>();
        Scanner sc = new Scanner(partyHosting);
        sc.useDelimiter("/");
        while (sc.hasNext()) {
            String party_name = sc.next();
            party.add(party_name);
        }
        return party;
    }



    //write user date to file, referenced from  BookTeaque, Part 2 BookFileHelper
    public static void writeUserToFile(String file, List<User> list){
        String header = "Type,Name,Email,Password,Gift,PartyHosting\n";
        try(FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos)){
            pw.println(header);  //write the header
            for(User u: list){
                if (u instanceof Host) {
                    String partyHosting = "";
                    int j = ((Host) u).getPartyHosting_holder().size();
                    int number = 0;
                    for(String party_name: ((Host) u).getPartyHosting_holder()){
                        partyHosting += party_name;
                        if (number < (j - 1)) {
                            partyHosting += "/";
                            number++;}
                    }
                    pw.println(u.getClass().getSimpleName() + "," + u.getName() + "," + u.getEmail() + "," + u.getPassword() + "," + ((Host) u).getGift()+ "," + partyHosting);
                }else if(u instanceof Guest){
                    pw.println(u.getClass().getSimpleName() + "," + u.getName() + "," + u.getEmail()
                            + "," + u.getPassword() + "," + ((Guest) u).getGift());
                }
            }
        }
        catch (IOException e){
            System.err.println("Error writing to file");
            e.printStackTrace();
        }
        System.out.println("Wrote the list of users to the file: " + file);
    }




    //write party data to file, referenced from  BookTeaque, Part 2 BookFileHelper
    public static void writePartyToFile(String file, List<Event> list){
        String header = "party_type,name,theme,guestList,location,time,age limit,isopen,Type,Year,MemoryShowcase,Limit,Registry,gradAnnouncement\n";
        try(FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos)){        pw.println(header);  //write the header
            for(Event e: list) {
                String registry = "";
                String guests = "";
                String party_type = e.getClass().getSimpleName();
                int i = e.getGuestList().size();        int num = 0;
                for (User u : e.getGuestList()) {
                    guests += u.getName();
                    if (num < (i - 1)) {
                        guests += "/";
                        num++;}
                }
                String general = party_type + "," + e.getName() + "," + e.getTheme() + "," + guests + "," + e.getLocation() + "," + e.getTime() + "," + e.hasAgeLimit() + "," + e.isOpen();
                //Type,Year,MemoryShowcase,Limit,Registry,gradAnnouncement
                if (party_type.equalsIgnoreCase("Anniversary")) {
                    if (e instanceof Anniversary) {pw.println(general + "," + ((Anniversary) e).getType() + "," + ((Anniversary) e).getYear() + "," + ((Anniversary) e).isMemoryShowcase());}
                } else if (party_type.equalsIgnoreCase("Birthday")) {
                    if (e instanceof Birthday) {pw.println(general + "," + ((Birthday) e).getAge());}
                } else if (party_type.equalsIgnoreCase("GiftExchange")) {
                    if (e instanceof GiftExchange) {pw.println(general + "," + "," + "," + ((GiftExchange) e).getLimit());}
                } else if (party_type.equalsIgnoreCase("Graduation")) {
                    if (e instanceof Graduation) {pw.println(general + ((Graduation) e).getType() + "," + "," + "," + "," + ((Graduation) e).getGradAnnouncement());}
                } else if (party_type.equalsIgnoreCase("Wedding")) {
                    if (e instanceof Wedding) {
                        int j = ((Wedding) e).getRegistry().size();      int number = 0;
                        for (String item : ((Wedding) e).getRegistry()) {
                            registry += item;
                            if (number < (j - 1)) {
                                registry += "/";
                                number++;}
                        }
                        pw.println(general + "," + "," + "," + "," + registry);}
                }
            }
        }
        catch (IOException e){System.err.println("Error writing to file");e.printStackTrace();}
        System.out.println("Wrote the list of parties to the file: " + file);
    }



}
