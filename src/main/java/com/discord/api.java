package com.discord;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class api {
  public String apid(String temp, String password, String quota) {
	  try {
		HttpResponse<String> response3 = Unirest.post("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users")
				   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
				   .header("Content-Type", "application/json")
				      .body("{\n    \"username\": \""+temp+"\",\n    \"password\": \""+password+"\",\n    \"traffic\": "+quota+"\n}")
				   .asString();
		String yes = response3.getBody();
		System.out.println(yes);
	     JSONObject jsonObject = new JSONObject(yes);
	     String yes3 =  jsonObject.get("id").toString(); 
		if(response3.getStatus()==200)
		{
			return yes3;
		}
	    } catch (UnirestException e) {
		}
		// TODO Auto-generated catch block
		return null;
	}
  }

