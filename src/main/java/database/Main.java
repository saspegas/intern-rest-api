package database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Student;
import models.Univesity;
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
	public List<Student> start() throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = queryWork();
		myRs.next();
		myUtil.addUser(myRs);
		return myUtil.getStudentList();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/students/{id}")
	public Response start(@PathParam("id") int studentId) throws Exception {
		ResultSet myRs,myRs2;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = queryWorkById(studentId);
		if (!myRs.next()) {
			return Response.status(Response.Status.NOT_FOUND).entity("Student not found for ID: " + studentId).build();
		}
		myUtil.addUser(myRs);
		myRs2 = universityById(myUtil.getUni_idFromStudent());
		myRs2.next();
		myUtil.addUni(myRs2);
		return Response.ok(myUtil.getStudentWithUni(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/universities")
	public List<Univesity> universities() throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = getUniversities();
		myRs.next();
		myUtil.addUni(myRs);
		return myUtil.getUniversity();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/universities/{id}")
	public Response university(@PathParam("id") int universityId) throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = universityById(universityId);
		if (!myRs.next()) {
			return Response.status(Response.Status.NOT_FOUND).entity("University not found for ID: " + universityId)
					.build();
		}
		myUtil.addUni(myRs);
		return Response.ok(myUtil.getUniversity(), MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/students/")
	public Response createUserProfile(Student student) throws Exception {

		String name = student.getName();
		int university_id = student.getUniversity_id();
		Date started_at = student.getStarted_at();
		ResultSet myRs;
		dbPr = new DBProcess();
		dbPr.openConnection();

		if (name == null || name == "") {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity("Name cannot be empty.").build();
		}

		Date currentDate = new Date(System.currentTimeMillis());
		if (started_at == null || started_at.equals("") || !currentDate.after(started_at)) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity("Wrong date field.").build();
		}

		myRs = universityByApiId(university_id);
		if (myRs.next()) {
			System.out.println("üni var");
		} else {
			System.out.println("üni yok");
		}

		return Response.ok("Kayýt Baþarýlý", MediaType.APPLICATION_JSON).build();
	}

	public ResultSet universityByApiId(int universityId) throws SQLException {
		String query;
		query = "select * from universities where api_id = " + universityId;
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	public ResultSet universityById(int universityId) throws SQLException {
		String query;
		query = "select * from universities where id = " + universityId;
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	public ResultSet getUniversities() throws SQLException {
		String query;
		query = "select * from universities";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	public ResultSet queryWorkById(int studentId) throws SQLException {
		String query;
		query = "select * from students where id = " + studentId;
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	public ResultSet queryWork() throws SQLException {
		String query;
		query = "select * from students";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

}
