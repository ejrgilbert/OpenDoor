package com.opendoor.controller;

import com.google.gson.Gson;
import com.opendoor.dto.user.UserDto;
import com.opendoor.persistence.model.Availability;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AvailabilityService;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController("availabilityController")
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/door")
public class AvailabilityController {
    /**
     * Service for availability-related persistence operations
     */
    @Autowired
    AvailabilityService availabilityService;

    /**
     * Service for user-related persistence operations
     */
    @Autowired
    UserService userService;

    /**
     * Service for group-related persistence operations
     */
    @Autowired
    GroupService groupService;

    /**
     * Convert between java and json objects
     */
    private static Gson gson = new Gson();

    /**
     * Close the door of the logged in user
     */
    @RequestMapping(
      method = PUT,
      value = "/close"
    )
    public void closeDoor() {
        // get the logged in user
        User user = userService.getLoggedInUser();
        availabilityService.closeDoor(user.getId());
    }

    private ResponseEntity verifyLocation(Availability availability) {
        if (availability.getLocation() == null || availability.getLocation() == "") {
            HashMap<String, String> response = new HashMap<>();
            response.put("reject", "location");
            response.put("errorMessage", "Oops!  Looks like you forgot to fill in your location.");

            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    /**
     * Opens the door of the logged in user to all passed in groups
     *
     * Expects RequestBody to be formatted as follows:
     * {
     *     "availability" : <Availability.class>,
     *     "groupNames" : <List of group names (all strings)>
     * }
     * @param params the json RequestBody with mappings for the availability and list of group names
     */
    @RequestMapping(
      method = PUT,
      value = "/open",
      consumes = { "application/json" }
    )
    public ResponseEntity openDoor(@RequestBody OpenDoorParam params) {
        Availability availability = params.getAvailability();
        List<String> groupNames = params.getGroupNames();

        ResponseEntity result = verifyLocation(availability);
        if (result != null) return result;

        // get the logged in user
        User user = userService.getLoggedInUser();

        // Get all the sent groups as group objects
        List<Group> groups = new ArrayList<>();
        if (groupNames == null) {
            // If groups not specified, then assume the 'all friends' list
            groups.add(user.getGroup(GroupService.ALL_FRIENDS_GROUP_NAME));
        } else {
            for( String groupName : groupNames) {
                groups.add(user.getGroup(groupName));
            }
        }

        // Get all the groups
        availabilityService.openDoor(user.getId(), availability, groups);

        return null;
    }

    /**
     * Updates the logged in user's door to be the passed in availability
     * @param availability the availability to update the user to having
     */
    @RequestMapping(
      method = PUT,
      value = "/update",
      consumes = { "application/json" }
    )
    public ResponseEntity updateDoor(@RequestBody Availability availability) {
        ResponseEntity result = verifyLocation(availability);
        if (result != null) return result;

        // get the logged in user
        User user = userService.getLoggedInUser();
        availabilityService.updateDoor(user.getId(), availability);

        return null;
    }

    @RequestMapping(
      method = GET,
      produces = { "application/json" }
    )
    @ResponseBody
    public Availability getDoor() {
      User user = userService.getLoggedInUser();
      return user.getAvailability();
    }

    /**
     * Get all the friends of the logged in user that have set themselves
     * as available to them.
     * @return a list of the users that the logged in user can see the availability of
     */
    @RequestMapping(
      method = GET,
      value = "allAvailable",
      produces = { "application/json" }
    )
    public List<UserDto> getAvailabilities() {
        // Get the logged in user
        User user = userService.getLoggedInUser();
        List<User> allAvailable = availabilityService.getAvailabilities(user.getId());

        List<UserDto> availableUsers = new ArrayList<>();
        for(User available : allAvailable) {
          UserDto availableUser = new UserDto(available.getId(), available.getFirstName(),
                  available.getLastName(), available.getAvailability());
          availableUsers.add(availableUser);
        }

        return availableUsers;
    }

    // ===============================================
    // ============= Data Transfer Objects ===========
    // ===============================================

    public static class OpenDoorParam {
        @Valid
        Availability availability;
        @Valid
        List<String> groupNames;

        public Availability getAvailability() {
            return availability;
        }

        public void setAvailability(Availability availability) {
            this.availability = availability;
        }

        public List<String> getGroupNames() {
            return groupNames;
        }

        public void setGroupNames(List<String> groupNames) {
            this.groupNames = groupNames;
        }

        public OpenDoorParam() {
            // For json mapping
        }
    }
}
