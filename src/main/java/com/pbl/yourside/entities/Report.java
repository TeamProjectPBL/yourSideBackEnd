package com.pbl.yourside.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    @ManyToOne
    private User teacher;

    @ManyToOne
    private User student;

    private String role;

    private String perp;

    private String victim;

    private String type;

    private String description;

    private boolean anonymous;

    private Status status;

    private int commit;

    private int resolution;

    private int contact;

    private int speed;

    private String comments;

    private String opened;

    private String closed;

    private boolean reviewed = false;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public boolean getAnonymous() { return anonymous;}

    public void setAnonymous(boolean anonymous) {this.anonymous = anonymous; }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPerp() {
        return perp;
    }

    public void setPerp(String perp) {
        this.perp = perp;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean wasReviewed) {
        this.reviewed = wasReviewed;
    }
}

