package com.email.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_sequence")
    @SequenceGenerator(
            name = "person_id_sequence",
            sequenceName = "person_id_sequence",
            allocationSize = 1
    )
    private Integer id;
    @Column(unique = true)
    private String email;

    @OneToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Otp> otps = new ArrayList();
    
    @Column(unique = true)
    private String mobNo;

    @OneToMany(
            mappedBy = "person",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EmailLog> emails = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public List<EmailLog> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailLog> emails) {
        this.emails = emails;
    }

	public List<Otp> getOtps() {
		return otps;
	}

	public void setOtps(List<Otp> otps) {
		this.otps = otps;
	}
	
	public void addOtp(Otp otp) {
	    otps.add(otp);      // inverse side
	    otp.setPerson(this); // owning side ⭐
	}
	
	public void addEmails(EmailLog otp) {
	    emails.add(otp);      // inverse side
	    otp.setPerson(this); // owning side ⭐
	}
    
}
