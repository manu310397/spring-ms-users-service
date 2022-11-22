package com.manoj.users.entity;

import com.manoj.users.dto.UserDTO;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -2591552627963833757L;

    // db generated value not public:
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String userId;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

    @Column(nullable=false, length=120, unique = true)
    private String email;

    @Column(nullable=false)
    private String encryptedPassword;

    private String emailVerificationToken;

    @Column(nullable=false)
    private Boolean emailVerificationStatus = false;

//    @OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
//    private List<AddressEntity> addresses;
//
//    //Cascade.PERSIST - If User is deleted the ROLE remains
//    @ManyToMany(cascade= {CascadeType.PERSIST}, fetch = FetchType.EAGER)
//    @JoinTable(name="users_roles",
//            joinColumns=@JoinColumn(name="users_id", referencedColumnName="id"),
//            inverseJoinColumns=@JoinColumn(name="roles_id", referencedColumnName="id"))
//    private Collection<RoleEntity>roles;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public Boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public UserDTO toUserDTO() {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(this, userDTO);

        return userDTO;
    }
}

