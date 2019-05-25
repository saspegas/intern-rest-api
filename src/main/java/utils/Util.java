package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Student;

public class Util {

	List<Student> studentList;
	
	public Util() {
		studentList = new ArrayList<>();
	}
	
	public void addUser(ResultSet rs) throws SQLException{
		Student student;
		while(rs.next()) {
			student = new Student();
			student.setId(rs.getInt("id"));
			student.setUniversity_id(rs.getInt("university_id"));
			student.setName(rs.getString("name"));
			student.setStarted_at(rs.getDate("started_at"));
			student.setCreated_at(rs.getDate("created_at"));
			student.setUpdated_at(rs.getDate("updated_at"));
			studentList.add(student);
		}
	}
	
	public List<Student> getStudentList(){
		return studentList;
	}
}
