# intern-rest-api - java

Conditions:
 - A person's university start date cannot be greater than today.
 - The name of the person and the start date of the university cannot be empty.
 - Date formats should be year-month-day. (Example: 2019-05-20)
 - If the submitted university is not in the database, it is created first. The student is then created to include university knowledge.
 - We're getting university information from another json data.

**GET METHODS LINKS**

http://localhost:8080/webservice/json/students
> Response: Returns all students from database.

	[
		{
		    "id":23,
		    "university_id":10,
		    "name":"Kadir Bay",
		    "started_at":"2016-05-30",
		    "created_at":"2019-06-07",
		    "updated_at":"2019-06-07",
		    "university":null
		},
			{
		    	"id":24,
		    	"university_id":10,
		    	"name":"Ahmet Yılmaz",
		    	"started_at":"2017-05-30",
		    	"created_at":"2019-06-07",
		    	"updated_at":"2019-06-07",
		    	"university":null
			}
	]

http://localhost:8080/webservice/json/students/13 
>Response: Returns the student with id 13 from database.

    [
	    {
		    "id":23,"university_id":10,
		    "name":"İsim Soyisim",
		    "started_at":"2016-05-30",
		    "created_at":"2019-06-07",
		    "updated_at":"2019-06-07",
		    "university":
			    {
				    "id":10,
				    "api_id":10,
				    "name":"Alanya Alaaddin Keykubat Üniversitesi",
					"city":"Antalya",
					"web_page":"alanyaaku.edu.tr",
					"type":"Devlet",
					"founded_at":"2015-01-01",
					"created_at":"2019-06-07",
					"updated_at":"2019-06-07"
				}
		}
	]

http://localhost:8080/webservice/json/universities
>Response: Returns all universities from database.

http://localhost:8080/webservice/json/universities/15
>Response: Returns the university with id 15 from database.

**POST METHOD**

http://localhost:8080/webservice/json/students

For example, json data we send;

    {
	"name":"Kadir Bay",
	"university_id":10,
	"started_at":"2016-05-30"
	}

![students](https://raw.githubusercontent.com/saspegas/intern-rest-api/master/images/mysql-students.png)

![universities
](https://raw.githubusercontent.com/saspegas/intern-rest-api/master/images/mysql-universities.png)
