package com.email.master;

import com.email.request.IntentCodeRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class IntentCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="intent_code_seq_id")
	@SequenceGenerator(name ="intent_code_seq_id",allocationSize=1,initialValue = 1)
	private int id;
	
	@Column(unique = true)
	private String code;
	
	private String title;
	
	private String description;
	
	@Column(columnDefinition = "TEXT")
	private String subjectTemplate; 

	@Column(columnDefinition = "TEXT")
	private String bodyTemplate;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private IntentCategory category;
	
	public IntentCode(IntentCodeRequest req) {
		this.code = req.getCode();
		this.title = req.getTitle();
		this.description = req.getDescription();
		this.subjectTemplate = req.getSubjectTemplate();
		this.bodyTemplate = req.getBodyTemplate();
	}

	public IntentCode() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescripton() {
		return description;
	}

	public void setDescripton(String descripton) {
		this.description = descripton;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubjectTemplate() {
		return subjectTemplate;
	}

	public void setSubjectTemplate(String subjectTemplate) {
		this.subjectTemplate = subjectTemplate;
	}

	public String getBodyTemplate() {
		return bodyTemplate;
	}

	public void setBodyTemplate(String bodyTemplate) {
		this.bodyTemplate = bodyTemplate;
	}

	public IntentCategory getCategory() {
		return category;
	}

	public void setCategory(IntentCategory category) {
		this.category = category;
	}
	
	
	
	

}
