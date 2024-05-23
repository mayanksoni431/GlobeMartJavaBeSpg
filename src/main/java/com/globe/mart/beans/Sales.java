package com.globe.mart.beans;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Document(collection="Sales")
@CompoundIndex(
		unique = true,
		name = "date-storeNo",
		def = "{storeNo:1 , salesDate:1}"
)
public class Sales {
	@Indexed(useGeneratedName = true)
	private String id;

	@NonNull
	private String storeNo;

	@NonNull
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date salesDate;
	
	@NonNull
	@CreatedDate
	private Date dateCreated;
	
	@NonNull
	@LastModifiedDate
	private Date dateLastModified;

	@NonNull
	private String managerId;
	
	private String managerName;
	
	private int noOfPOSTr;//4 
	private double totalPOSAmt;//7834.34 +
	
	private int noOfOnlineTr;//45
	private double totalOnlineOrderAmt;//35643.34 +
	
	private int noOfReturns;//2
	private double totalReturnsAmt;//45 -
	
	private double totalDiscountAmt;//2000 -
	
	private double totalSalesAmt;// = POS + ONLINE - RETURNS - DISCOUNT
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	
	public Date getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(Date date) {
		this.salesDate = date;
	}

	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public int getNoOfPOStr() {
		return noOfPOSTr;
	}
	public void setNoOfPOStr(int totalPOStr) {
		this.noOfPOSTr = totalPOStr;
	}
	public double getTotalPOSAmt() {
		return totalPOSAmt;
	}
	public void setTotalPOSAmt(double totalPOSAmt) {
		this.totalPOSAmt = totalPOSAmt;
	}
	public int getNoOfOnlinetr() {
		return noOfOnlineTr;
	}
	public void setNoOfOnlinetr(int totalOnlineOrder) {
		this.noOfOnlineTr = totalOnlineOrder;
	}
	public double getTotalOnlineOrderAmt() {
		return totalOnlineOrderAmt;
	}
	public void setTotalOnlineOrderAmt(double totalOnlineOrderAmt) {
		this.totalOnlineOrderAmt = totalOnlineOrderAmt;
	}
	public int getNoOfReturns() {
		return noOfReturns;
	}
	public void setNoOfReturns(int totalReturns) {
		this.noOfReturns = totalReturns;
	}
	public double getTotalReturnsAmt() {
		return totalReturnsAmt;
	}
	public void setTotalReturnsAmt(double totalReturnsAmt) {
		this.totalReturnsAmt = totalReturnsAmt;
	}
	public double getTotalDiscountAmt() {
		return totalDiscountAmt;
	}
	public void setTotalDiscountAmt(double totalDiscountAmt) {
		this.totalDiscountAmt = totalDiscountAmt;
	}
	public double getTotalSalesAmt() {
		return totalSalesAmt;
	}
	public void setTotalSalesAmt(double totalSalesAmt) {
		this.totalSalesAmt = totalSalesAmt;
	}

}
