package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.print.attribute.standard.Media;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.Student;
import utils.Util;

@Path("/json")
public class Main {
	
	DBProcess dbPr;
	Statement statement;
	public CachedRowSet resWanted;
	Util myUtil;
	String result;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/students")
	public List<Student> start() throws Exception{
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();
		
		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = queryWork();
		myUtil.addUser(myRs);
		return myUtil.getStudentList();
		
	}

	public ResultSet queryWork() throws SQLException {
		String query;
		query = "select * from students";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}
	
}
