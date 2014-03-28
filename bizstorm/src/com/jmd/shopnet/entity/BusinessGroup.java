package com.jmd.shopnet.entity;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class BusinessGroup {
	@Id
	private long id;
	
	private @Load Ref<Business>  business;	
	@Parent @Load 
	private Ref<Customer>  owner;	
	private List<Ref<Customer>>  members;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ref<Business> getBusiness() {
		return business;
	}

	public void setBusiness(Ref<Business> business) {
		this.business = business;
	}

	public Ref<Customer> getOwner() {
		return owner;
	}

	public void setOwner(Ref<Customer> owner) {
		this.owner = owner;
	}

	public List<Ref<Customer>> getMembers() {
		return members;
	}

	public void setMembers(List<Ref<Customer>> members) {
		this.members = members;
	}

	
	public List<Customer> getAllGroupMembers() {
		List<Customer> rmembers = new ArrayList<>();
		if(members != null){
			for (Ref<Customer> member : members) {
				rmembers.add(member.get());
			}
		}
		return rmembers;
	}
	
	public void addGroupMember(Customer customer) {
		if(customer != null && members != null){
			members.add(Ref.create(customer));
		}
	}
	
	public void removeGroupMember(Customer customer) {
		if(customer != null && members != null){
			members.remove(Ref.create(customer));
		}
	}
}
