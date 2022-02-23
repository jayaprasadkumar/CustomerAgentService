package com.insurance.customer.agent.file.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
	
	@NotBlank(message="emp id can not be blank")
	private String empID;
	@NotBlank(message="Name prefix can not be blank")
	private String prefix;
	@NotBlank(message="firstName can not be blank")
	private String firstName;
	private String middleInitial;
	@NotBlank(message="lastName can not be blank")
	private String lastName;
	@NotBlank(message="gender can not be blank")
	private String gender;
	@NotBlank(message="eMail can not be blank")
	private String eMail;
	@NotBlank(message="dateofBirth can not be blank")
	private String dateofBirth;
	private String dateofJoining;
	private String ssn;
	
	
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getDateofBirth() {
		return dateofBirth;
	}
	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}
	public String getDateofJoining() {
		return dateofJoining;
	}
	public void setDateofJoining(String dateofJoining) {
		this.dateofJoining = dateofJoining;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public String toString(){//overriding the toString() method  
		
		StringBuilder str = new StringBuilder();
		  return str.append(this.empID).append(this.prefix).append(this.firstName).toString();
		 }  
	
	
}
