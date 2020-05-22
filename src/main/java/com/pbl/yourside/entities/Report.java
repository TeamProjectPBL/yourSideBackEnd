package com.pbl.yourside.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(name = "Reports")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    //@Column(name = "Teacher ID")
    private long tId;

    //@Column(name = "Student ID")
    private long sId;

    //@Column(name = "Status")
    private enum status {
        UNREAD, READ, PENDING, RESOLVED, UNRESOLVED
    }

    //@Column(name = "Commitment")
    private int commit;

    //@Column(name = "Resolution")
    private int resolution;

    //@Column(name = "Contact")
    private int contact;

    //@Column(name = "Speed")
    private int speed;

    //@Column(name = "Date Opened")
    private String opened;

    //@Column(name = "Date Closed")
    private String closed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long gettId() {
        return tId;
    }

    public void settId(long tId) {
        this.tId = tId;
    }

    public long getsId() {
        return sId;
    }

    public void setsId(long sId) {
        this.sId = sId;
    }

    public int getCommit() {
        return commit;
    }

    public void setCommit(int commit) {
        this.commit = commit;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }
}
