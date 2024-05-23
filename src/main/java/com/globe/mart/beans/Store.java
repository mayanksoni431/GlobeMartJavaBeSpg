package com.globe.mart.beans;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection="Store")
public class Store {

	@Indexed(useGeneratedName = true)
	private String id;


	@Indexed(unique = true)
	private String storeNo;//string MM-2134
	private String name;
	
	private String city;
	private String state;
	private String country;
	private String pincode;
	
	@NonNull
	private String manager;
	@NonNull
	private String managerId;
	private String managerEmail;
	private String managerMobNo;
	
	private String noOfEmployees;
	
	@CreatedDate
	private Date dateCreated;
	@LastModifiedDate
	private Date dateModified;
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getManagerMobNo() {
		return managerMobNo;
	}

	public void setManagerMobNo(String managerMobNo) {
		this.managerMobNo = managerMobNo;
	}

	public String getNoOfEmployees() {
		return noOfEmployees;
	}

	public void setNoOfEmployees(String noOfEmployees) {
		this.noOfEmployees = noOfEmployees;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}
		
}
