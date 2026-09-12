package model.people;

import model.party.User_Party;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/29/23
 */
public class User implements User_Party {
    private String name;
    private String email;
    private String password;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Constructor for creating a fake account
    public User(String name) {
        this(name, "fakeAccount", "fakeAccount");
    }

    //getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }



    //toString
    @Override
    public String toString() {
        return " name is " + name + " with email " + email;
    }
}
