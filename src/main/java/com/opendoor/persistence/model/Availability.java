package com.opendoor.persistence.model;

import sun.security.x509.AVA;

import javax.persistence.*;

/**
 * The availability of the user
 */
@Entity
@Table( name = "availability" )
public class Availability {
  /**
   * The DB unique ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * Where the person is
   * TODO Do we need to have a Coordinate object too?
   *
   * TODO future version should have PostalAddress object
   * https://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/#entity-mapping-property
   * This embedded object annotation syntax will have to be used for the Address of the Availability object. (section 2.2.2.4)
   */
  private String location;

  /**
   * What the person is doing
   * TODO In the future, maybe this can be an enum?
   * - This would only be if we have a finite options for activity
   * - maybe there could be an enum option for "other"?
   */
  private String activity;

  // --------------------------------------------------
  // ------------- Getters and Setters ----------------
  // --------------------------------------------------

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  // --------------------------------------------------
  // ------------- Functional Methods -----------------
  // --------------------------------------------------

  public boolean equals(Availability other) {
    return this.activity.equals(other.activity) && this.location.equals(other.location);
  }

  // --------------------------------------------------
  // ---------------- Constructors --------------------
  // --------------------------------------------------

  public Availability() {
    // Empty for Hibernate
  }

  public Availability(String location, String activity) {
    setLocation(location);
    setActivity(activity);
  }
}
