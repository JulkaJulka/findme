package com.findme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USER_FM")
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    //TODO from existed data
    private String country;
    private String city;

    private Integer age;
    private Date dateRegistrated;
    private Date lastDateActivited;
    //TODO enum
    private RelationShip relationshipStatus;
    private Religion religion;
    //TODO from existed data
    private String school;
    private String university;

    private List<Message> messagesSent;
    private List<Message> messagesReceived;

    private String password;
    private String email;
   // private UserType userType;
    private LoginStatus loginStatus;

    // private String[] interests;

    public User() {
    }

    @Id
    @SequenceGenerator(name = "SEQ_US", sequenceName = "SEQ_US", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_US", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_USER")
    public Long getId() {
        return id;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    @Column(name = "AGE")
    public Integer getAge() {
        return age;
    }

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "DATE_REGISTRATED")
    public Date getDateRegistrated() {
        return dateRegistrated;
    }

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "LAST_DATE_ACTIVATED")
    public Date getLastDateActivited() {
        return lastDateActivited;
    }

    /*@Enumerated(EnumType.STRING)
    @Column(name = "RELATIONSHIP_STATUS")
    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "RELIGION")
    public String getReligion() {
        return religion;
    }*/

    @Enumerated(EnumType.STRING)
    @Column(name = "RELATIONSHIP_STATUS")
    public RelationShip getRelationshipStatus() {
        return relationshipStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "RELIGION")
    public Religion getReligion() {
        return religion;
    }

    @Column(name = "SCHOOL")
    public String getSchool() {
        return school;
    }

    @Column(name = "UNIVERSITY")
    public String getUniversity() {
        return university;
    }

   /* @Enumerated(EnumType.STRING)
   @Column(name = "LOGIN_STATUS")
    public LoginStatus getLoginStatus() {
        return loginStatus;
    }*/

    @JsonIgnore
    @OneToMany(targetEntity = Message.class, fetch = FetchType.LAZY, mappedBy = "userFrom",cascade = CascadeType.ALL)
    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    @JsonIgnore
    @OneToMany(targetEntity = Message.class, fetch = FetchType.LAZY, mappedBy = "userTo", cascade = CascadeType.ALL)
    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDateRegistrated(Date dateRegistrated) {
        this.dateRegistrated = dateRegistrated;
    }

    public void setLastDateActivited(Date lastDateActivited) {
        this.lastDateActivited = lastDateActivited;
    }

    /*public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }*/

   /*public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }*/

    public void setRelationshipStatus(RelationShip relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(phone, user.phone) ||
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(phone, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", age=" + age +
                ", dateRegistrated=" + dateRegistrated +
                ", lastDateActivited=" + lastDateActivited +
                ", relationshipStatus=" + relationshipStatus +
                ", religion=" + religion +
                ", school='" + school + '\'' +
                ", university='" + university + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
