package ca.bcit.comp3717.trailchum;

import java.util.List;

public class UserAccount {

    String uid;
    String email;
    String name;
    String gender;
    String dateOfBirth;
    List<String> trailsToBeDone;

    public UserAccount() {

    }

    public UserAccount(String uid, String email, String name, String gender,
                       String dateOfBirth,
                       List<String> trailsToBeDone) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.trailsToBeDone = trailsToBeDone;
    }

    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getTrailsToBeDone() {
        return trailsToBeDone;
    }

    public void setTrailsToBeDone(List<String> trailsToBeDone) {
        this.trailsToBeDone = trailsToBeDone;
    }

}
