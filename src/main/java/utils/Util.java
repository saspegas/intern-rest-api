package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Student;
import models.Univesity;

public class Util {

	List<Student> studentList = null;
	List<Univesity> universityList = null;
	
	public Util() {
		studentList = new ArrayList<>();
		universityList = new ArrayList<>();
	}
	
	public void addUser(ResultSet rs) throws SQLException{
		Student student;
		do {
			student = new Student();
			student.setId(rs.getInt("id"));
			student.setUniversity_id(rs.getInt("university_id"));
			student.setName(rs.getString("name"));
			student.setStarted_at(rs.getDate("started_at"));
			student.setCreated_at(rs.getDate("created_at"));
			student.setUpdated_at(rs.getDate("updated_at"));
			studentList.add(student);
		} while (rs.next());
	}
	
	public void addUni(ResultSet rs) throws SQLException{
		Univesity university;
		do {
			university = new Univesity();
			university.setId(rs.getInt("id"));
			university.setApi_id(rs.getInt("api_id"));
			university.setName(rs.getString("name"));
			university.setCity(rs.getString("city"));
			university.setWeb_page(rs.getString("web_page"));
			university.setType(rs.getString("type"));
			university.setFounded_at(rs.getDate("founded_at"));
			university.setCreated_at(rs.getDate("created_at"));
			university.setUpdated_at(rs.getDate("updated_at"));
			universityList.add(university);
		} while (rs.next());
	}
	
	public List<Student> getStudentWithUni() {
		studentList.get(0).setUniversity(universityList.get(0));
		return studentList;
	}
	
	public int getUni_idFromStudent(){
		return studentList.get(0).getUniversity_id();
	}
	
	public List<Student> getStudentList(){
		return studentList;
	}
	
	public List<Univesity> getUniversity(){
		return universityList;
	}
}
