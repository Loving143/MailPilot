package com.email.master;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class IntentCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="intent_cat_seq_id")
	@SequenceGenerator(name ="intent_email_seq_id",allocationSize = 1, initialValue=1)
	private int id;
	
	@Column(unique = true)
	private String categoryCode;
	
	private String name;
	
	private String description;
	
	 @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private List<IntentCode>intentCodes = new ArrayList();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IntentCode> getIntentCodes() {
		return intentCodes;
	}

	public void setIntentCodes(List<IntentCode> intentCodes) {
		this.intentCodes = intentCodes;
	}
	
	

}
