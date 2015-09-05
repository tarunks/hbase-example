package com.tarun.myapp;

public class Employee{

	private String id;
	private String name;
	private String address;

	public Employee(){}
	public Employee(String id,String name, String address){
		this.id=id;
		this.name=name;
		this.address=address;
	}
	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getAddress(){

		return this.address;
	}
	@Override
	public String toString(){
		return String.format("ID: %s \n Name: %s \n Address: %s \n", id,name,address);
	}
}
