package database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import org.json.JSONObject;

import models.Student;
import utils.Util;

@Path("/json")
public class Main {

	DBProcess dbPr;
	Statement statement;
	public CachedRowSet resWanted;
	Util myUtil;
	String result;
	UniversityJson getUniversity = new UniversityJson();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/students")
	public Response students() throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = getStudents();
		if (myRs.next()) {
			myUtil.addUser(myRs);
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Kayýtlý öðrenci bulanmamaktadýr.").build();
		}
		return Response.ok(myUtil.getStudentList(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/students/{id}")
	public Response start(@PathParam("id") int studentId) throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = getStudentsById(studentId);
		if (!myRs.next()) {
			return Response.status(Response.Status.NOT_FOUND).entity("Student not found for ID: " + studentId).build();
		}
		myUtil.addUser(myRs);
		myRs = universityById(myUtil.getUni_idFromStudent());
		myRs.next();
		myUtil.addUni(myRs);
		return Response.ok(myUtil.getStudentWithUni(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/universities")
	public Response universities() throws Exception {
		ResultSet myRs;
		dbPr = new DBProcess();
		myUtil = new Util();

		statement = dbPr.openConnection();
		resWanted = dbPr.createStatement();
		myRs = getUniversities();
		if (myRs.next()) {
			myUtil.addUni(myRs);
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Kayýtlý üniversite bulanmamaktadýr.").build();
		}
		return Response.ok(myUtil.getUniversity(), MediaType.APPLICATION_JSON).build();
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
		int insertResult;
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
		
		if (myRs.next()) { //üniversite daha önceden kaydedilmiþ
			insertResult = addStudent(name, university_id, started_at);
			if (insertResult != -1) {
				return Response.ok(name + " isimli öðrenci " +insertResult + " id numarasý ile kaydedildi.", MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Öðrenci eklenirken hata oluþtu.").build();
			}
		} else { //üniversite veritabanýnda bulunamadý
			insertResult = addUniversity(getUniversity.getJsonUniversity(university_id));
			if (insertResult != -1) {
				insertResult = addStudent(name, university_id, started_at);
				if (insertResult != -1) {
					return Response.ok(name + " isimli öðrenci " +insertResult + " id numarasý ile kaydedildi.", MediaType.APPLICATION_JSON).build();
				}
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Öðrenci eklenirken hata oluþtu.").build();
			}
		}

		return Response.ok("Kayýt Baþarýlý", MediaType.APPLICATION_JSON).build();
	}

	private int addUniversity(JSONObject jsonUni) throws SQLException {
		String query;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String created_at = LocalDateTime.now().format(formatter);
		String founded_at = jsonUni.getString("founded_at") + "-01-01";
		query = "insert into universities (id, api_id, name, city, web_page, type, founded_at, created_at, updated_at) values ("
				+ jsonUni.getInt("id")
				+ "," + jsonUni.getInt("id")
				+ ",'" + jsonUni.getString("name") + "'"
				+ ",'" + jsonUni.getString("city") + "'"
				+ ",'" + jsonUni.getString("web_page") + "'"
				+ ",'" + jsonUni.getString("type") + "'"
				+ ",'" + founded_at + "'"
				+ ",'" + created_at + "'"
				+ ",'" + created_at + "')";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		int rs = pst.executeUpdate();
		if (rs == 1)
			return jsonUni.getInt("id");
		return -1;
	}

	private int addStudent(String name, int university_id, Date started_at) throws SQLException {
		String query;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String created_at = LocalDateTime.now().format(formatter);
		query = "insert into students (university_id, name, started_at, created_at, updated_at) values ("
		+ university_id + "," 
		+ "'" + name + "',"
		+ "'" + started_at + "',"
		+ "'" + created_at + "',"
		+ "'" + created_at + "')";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int rs = pst.executeUpdate();
		if(rs == 1) {
			ResultSet rSet = pst.getGeneratedKeys();
			rSet.next();
			rs = rSet.getInt(1);
		} else {
			return -1;
		}
		return rs;
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

	public ResultSet getStudentsById(int studentId) throws SQLException {
		String query;
		query = "select * from students where id = " + studentId;
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

	public ResultSet getStudents() throws SQLException {
		String query;
		query = "select * from students";
		PreparedStatement pst = dbPr.getConnection().prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs;
	}

}