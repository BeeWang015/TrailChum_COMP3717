package ca.bcit.comp3717.trailchum;

import java.util.List;

public class UserAccount {

    String email;
    String name;

    String gender;
    String dateOfBirth;
    List<String> trailsToBeDone;
    List<String> trailsDone;

    public UserAccount() {

    }

    public UserAccount(String email, String name, String gender,
                       String dateOfBirth,
                       List<String> trailsToBeDone) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.trailsToBeDone = trailsToBeDone;
        this.trailsDone = trailsDone;
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

    public List<String> getTrailsDone() {
        return trailsDone;
    }

    public void setTrailsDone(List<String> trailsDone) {
        this.trailsDone = trailsDone;
    }
}
