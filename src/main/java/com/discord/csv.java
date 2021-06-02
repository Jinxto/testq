package com.discord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class csv {
  public void csvdataInitialize() {
	  try {
			HttpResponse<JsonNode> response = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users")
			   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
			   .asJson();
			   
			JSONObject myObj = response.getBody().getObject();
			String pagesize = myObj.get("pageSize").toString();
			int pagesize3= Integer.parseInt(pagesize);
			for(int k = 1; k<= pagesize3; k++) {
				System.out.println(k);
				HttpResponse<JsonNode> response2 = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users?page="+k)
						   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
						   .asJson();
				JSONObject myObj2 = response2.getBody().getObject();
				JSONArray arr = (JSONArray) myObj2.get("data");
				for(int i = 0; i<arr.length(); i++) {
					JSONObject test = (JSONObject) arr.get(i);
					if(!test.get("id").toString().isEmpty()) {
						String username = test.get("username").toString();
						String id = test.get("id").toString();
						if(username.contains("mamak_") || username.contains("10248_")) {
							username = username.substring(6);
						}
						FileWriter fw = new FileWriter("test.txt",true);
						BufferedWriter bw = new BufferedWriter(fw); // extension of filewriter make sure words are efficient written on the file
						PrintWriter pw = new PrintWriter(bw);
						System.out.println(username+","+id);
						pw.println(username+","+id);
						pw.flush();
						pw.close();
					}
		
	  }
			
	}
			
	
} catch (UnirestException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}finally{
	
}
}
  public String getId(String discord) {
	  String delimiter = ",";
	  try {
	         File file = new File("test.txt");
	         FileReader fr = new FileReader(file);
	         BufferedReader br = new BufferedReader(fr);
	         String line = "";
	         String[] tempArr;
	         while((line = br.readLine()) != null) {
	            tempArr = line.split(delimiter);
	            
	            for(int i = 0; i<tempArr.length;i++) {
	               if(tempArr[i].equals(discord)) {
	            	   return tempArr[i+1];
	               }
	            }
	           
	         }
	         br.close();
	         } catch(IOException ioe) {
	            ioe.printStackTrace();
	         }
	return null;
  }
  public void deleteFile(String filename) {
	  File file = new File(filename);
	  file.setWritable(true);
	  file.delete();
  }
  public void writeSpecific(String discordId, String apiId) {
		FileWriter fw;
		try {
			fw = new FileWriter("test.txt",true);
			BufferedWriter bw = new BufferedWriter(fw); // extension of filewriter make sure words are efficient written on the file
			PrintWriter pw = new PrintWriter(bw);
			pw.println(discordId+","+apiId);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
  }
  public void writeSpecific2(String text, String Filename, int index, String countrycode) {
		FileWriter fw;
		
		try {
			utilities util = new utilities();
			fw = new FileWriter(Filename,true);
			BufferedWriter bw = new BufferedWriter(fw); // extension of filewriter make sure words are efficient written on the file
			PrintWriter pw = new PrintWriter(bw);
			System.out.println("generating data");
			for(int i = 0; i<index; i++) {
			String temp = text+util.generateRandomString(8)+"_country-"+countrycode;
			if(countrycode.equals("nikeas")) {
			temp=text+util.generateRandomString(8)+"_set-"+countrycode;
			}
			pw.println(temp);
			}
		
			pw.flush();
			pw.close();
			fw.flush();
			fw.close();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
} 
