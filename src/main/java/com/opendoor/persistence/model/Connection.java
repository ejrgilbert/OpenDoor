package com.opendoor.persistence.model;

import javax.persistence.*;

@Entity
@Table( name = "connection" )
public class Connection {

    /**
     * The database id of this user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    private ConnectionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public boolean isBlocked() {
      return status == ConnectionStatus.BLOCKED;
    }

    public boolean areFriends() {
      return status == ConnectionStatus.FRIENDS;
    }

    public boolean hasRequested() {
      return status == ConnectionStatus.REQUESTED;
    }

    @Enumerated(EnumType.STRING)
    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public Connection() {

    }

    public Connection(User user1, User user2, ConnectionStatus status) {
      setUser1(user1);
      setUser2(user2);
      setStatus(status);
    }

    public enum ConnectionStatus {
        BLOCKED,
        REQUESTED,
        FRIENDS
    }
}
