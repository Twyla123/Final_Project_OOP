package model.party;

import model.people.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Description
 *
 * @author Twyla Zhang
 * email: twylazha@usc.edu
 * ITP 265, Spring 2023, boba section
 * Date: 4/30/23
 */
public interface Gift {

    Map<User, String> collectGift(List<User> allGuests);   // collects and returns the gifts brought by the guest.

}
