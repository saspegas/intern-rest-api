package database;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;


public class UniversityJson {

  private String streamToString(InputStream inputStream) {
    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    return text;
  }

  public JSONObject getJsonUniversity(int universityId) {
    String json = null;
    try {
      URL url = new URL("https://gitlab.com/kodiasoft/intern/2019/snippets/1859421/raw");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("charset", "utf-8");
      connection.connect();
      InputStream inStream = connection.getInputStream();
      json = streamToString(inStream); // input stream to string
      JSONArray jsonArray = new JSONArray(json);
      return jsonArray.getJSONObject(universityId);
      
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}