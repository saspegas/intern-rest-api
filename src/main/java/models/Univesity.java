package models;

import java.sql.Date;

public class Univesity {

	private int id;
	private int api_id;
	private String name;
	private String city;
	private String web_page;
	private String type;
	private Date founded_at;
	private Date created_at;
	private Date updated_at;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getApi_id() {
		return api_id;
	}
	public void setApi_id(int api_id) {
		this.api_id = api_id;
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
	public String getWeb_page() {
		return web_page;
	}
	public void setWeb_page(String web_page) {
		this.web_page = web_page;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getFounded_at() {
		return founded_at;
	}
	public void setFounded_at(Date founded_at) {
		this.founded_at = founded_at;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
}
