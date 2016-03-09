package com.group4.thermostat;

import android.util.Log;

import java.net.URL;
import java.net.HttpURLConnection;

import java.lang.StringBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Created by Nathan Lam
 */

public class Request {

    private HttpURLConnection getConnection(String endPointURL,
                                            String requestMethod) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(endPointURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            if(requestMethod == "POST") {
                connection.setDoOutput(true);
            }
            connection.connect();
        } catch(IOException e) {
            System.out.println("issue establishing connection with " +
                    endPointURL);
            e.printStackTrace();
            return null;
        }

        return connection;

    }

    // method performs an HTTP GET request, generates the response string
    // and parses it into a JSON object.
    // ********** if the request does not yield a json response, **********
    // ********** the return value will be null.                 **********

    public JSONObject getRequest(String endPointURL) {

        Log.d("GET", "Request handling the get");

        HttpURLConnection connection = getConnection(endPointURL, "GET");

        if(connection == null) {
            Log.d("GET", "Connection is null");
            // check endPointURL
            return null;
        }

        try {
            Log.d("GETINPUTSTREAM", "about to try");
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Log.d("GETINPUTSTREAM", "got here");
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.d("GET", "about to return");
            return getJSONObject(sb.toString());
        } catch(Exception e) {
            Log.d("GET", "IOException");
            e.printStackTrace();
            return null;
        }

    }

    private JSONObject getJSONObject(String jsonString) {

        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try {
            obj = (JSONObject)parser.parse(jsonString);
        } catch(ParseException e) {
        }

        return obj;

    }

    public JSONObject postRequest(String endPointURL, JSONObject obj) {

        HttpURLConnection connection = getConnection(endPointURL,
                "POST");

        if(connection == null) {
            // check endPointURL
            return null;
        }

        try {
            // send request
            DataOutputStream dos =
                    new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(obj.toString());
            dos.flush();
            dos.close();

            // build response
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            return getJSONObject(sb.toString());
        } catch(IOException e) {
            return null;
        }
    }

/*
  // these methods will construct jsonobjects with the attributes and
  // the respective values of the input objects
  //
  // the following is just an example of how these methods will be
  // implemented. it may not reflect the implementation of the input
  // classes

  public JSONObject getJSONObject(Thermostat t) {
    JSONObject obj = new JSONObject();
    obj.put("temperature", t.getTemperature());
    obj.put("hvacON", t.getHVACOn());
    return obj;
  }

  // not sure what class will be passed in here. Application is just a
  // placeholder

  public JSONObject getJSONObject(Application a) {
    JSONObject obj = new JSONObject();
    obj.put("setTemperature", a.getSetTemperature());
    obj.put("schedule", a.getSchedule()); // could even be getSchedules();
    return obj;
  }
*/

    public static void main(String [] args) {

        // here is an example of how you would use the request class

        Request r = new Request();
        // urlString is the server endpoint. i have configured the
        // server to receive both get and post requests at the same
        // url.
        String urlString = "http://52.37.144.142:9000/application";
        // the urlString for the app is "http://52.37.144.142:9000/application"
        JSONObject obj = r.getRequest(urlString);

        if(obj == null) {
            // ERROR
            // something is wrong with the json. this could be that
            // the request was made to an invalid url (check by opening
            // the url in a web browser) or that the json is malformed
            // (my bad)
            return;
        } else {
            System.out.println(obj.toString());
        }

        // following is just an example json object. when complete, you
        // should just be able to call a function to generate a JSONObject
        // from a Java object.
        JSONObject send = new JSONObject();
        send.put("val1", "test");
        send.put("val2", "string");

        JSONObject posted = r.postRequest(urlString, send);
        // you can see what is posted by performing a GET on the opposite
        // endpoint (i.e. if posted to /thermostat, the object can be
        // retrieved via GET request from /application)
        if(posted != null) {
            System.out.println(posted.toString());
        }
    }
}

