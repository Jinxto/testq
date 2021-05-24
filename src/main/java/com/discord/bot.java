package com.discord;



import java.io.File;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import jdk.internal.org.jline.utils.Status;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class bot extends ListenerAdapter {
@Override
 public void onMessageReceived(MessageReceivedEvent event) {
	
	if(event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
     String messa = event.getMessage().getContentRaw();
     String id = Long.toString(event.getAuthor().getIdLong());
     if(messa.equals("$help")) {
	 MessageChannel channel = event.getChannel();
     EmbedBuilder eb = new EmbedBuilder();
     eb.setTitle("User Commands");
     eb.setDescription("The bot's prefix is `$.` Examples below are just a general guideline for use. Do not use `< >` when invoking commands.");
     eb.addField("View current regions", "`$regions`\n"
     		+ "Display supported regions for Residential Proxies.", true);
     eb.addField("Generate Proxies","`$gen <amount> <region>\n`"
     		+ "Generate proxies for a specific region in the quantities under 10000. If country contains spaces, wrap it with `< >`",true);
     eb.addField("View your data", "`$data`\n"
     		+ "View how much data you currently have.", true);
     eb.addField("Claim your data","`$claim <order_number> <email>\n`"
     		+ "After paying on our store, you can then claim your data by invoking this command using the order number of the paid order.",true);
     //eb.setfooter(Stringtext, string icon url);
	 channel.sendMessage(eb.build()).queue();
     }
     if(messa.equals("$data")) {
    	 MessageChannel channel = event.getChannel();
    	 csv cas = new csv();
    	 String yes = cas.getId(id);
    	 if(!yes.isEmpty()) {
    		 Unirest.setTimeouts(0, 0);
    		 try {
				HttpResponse<JsonNode> response = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users/"+yes)
				   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
				   .asJson();
				JSONObject json = (JSONObject) response.getBody().getObject();
				String pl = json.get("availableTraffic").toString(); //changeto availableTraffic on v2
				if(response.getStatus()==200) {
				
				channel.sendMessage(pl).queue();
				return;
				}
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				channel.sendMessage("Data Not Found!");
			}
    		
    	 }
    	 channel.sendMessage("Data Not Found!");
    		
    		
    			
     }
     if(messa.contains("$claim")) {
    	 MessageChannel channel = event.getChannel();
    	 String temp = messa.replace("$claim ", "");
    	 String email = temp.substring(5);
    	 String orderId= temp.replace(" "+email, "");
    	 if(email!="" && orderId!="") {
    		try {
    			
    			Unirest.setTimeouts(0, 0);
    			HttpResponse<JsonNode> response = Unirest.get("https://a201bc7a9475d96ffe85d436af7fc56e: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@mamak-proxies.myshopify.com/admin/api/2021-04/orders.json?name=#"+orderId)
    			  .header("Content-Type", "application/json")
    			  .asJson();
    		            System.out.println(response.getStatus());
    			        JSONArray json = (JSONArray) response.getBody().getObject().get("orders");
    					JSONObject json2 = (JSONObject) json.get(0);
    					String userid = json2.get("id").toString();
    					System.out.println(userid);
    					String contact_email = json2.get("contact_email").toString();
    					JSONArray wifi = (JSONArray) json2.get("line_items");
    					JSONObject wifi2 = (JSONObject) wifi.get(0);	
    					String wifi3 = wifi2.get("name").toString().replace("Residential Proxies ", "");
    					wifi3 = wifi3.replace(" GB", "");
    					System.out.println(userid+contact_email+wifi3);
				       if(email.equals(contact_email)) {
				    	   csv cas = new csv();
				    	   String discordId = cas.getId(id);
				    	   if(discordId!=null) {
				    		   HttpResponse<JsonNode> response4 = Unirest.post("https://a201bc7a9475d96ffe85d436af7fc56e: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@mamak-proxies.myshopify.com/admin/api/2021-04/orders/"+userid+"/close.json")
				    				   .header("Content-Type", "application/json")
				    				   .asJson();
				    		   
                             if(response4.getStatus()==200) {
                               Unirest.setTimeouts(0, 0);
  				    		   HttpResponse<String> response2 = Unirest.post("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users/"+discordId+"/give-traffic")
  				    				   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
  				    				   .header("Content-Type", "application/json")
  				    				   .body("{\n    \"amount\": "+wifi3+"\n}")
  				    				   .asString();
  				    		   System.out.println(response2.getStatus());
                            	 
                            	 if(response2.getStatus()==200) {
                            		 System.out.println("existing user");
                      				channel.sendMessage("Data claimed for existing user!").queue();
                                    return;
                            	 }
                            	 channel.sendMessage("Error occured try again!").queue();
                 				return;
                             }
                             channel.sendMessage("Data already claimed!").queue();
                             return;
				    	   }
				    	   System.out.println("here");
				    	   String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
				           StringBuilder salt = new StringBuilder();
				           Random rnd = new Random();
				           while (salt.length() < 8) { // length of the random string.
				               int index = (int) (rnd.nextFloat() * SALTCHARS.length());
				               salt.append(SALTCHARS.charAt(index));
				           }
				        
				           if(discordId==null) {
				        	   HttpResponse<JsonNode> response4 = Unirest.post("https://a201bc7a9475d96ffe85d436af7fc56e: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@mamak-proxies.myshopify.com/admin/api/2021-04/orders/"+userid+"/close.json")
				    				   .header("Content-Type", "application/json")
				    				   .asJson();
				    		   if(response4.getStatus()==200) {
				    			   String saltStr = salt.toString();
						           String username = "10248_"+id;
					        	   Unirest.setTimeouts(0, 0);
						           HttpResponse<JsonNode> response3 = Unirest.post("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users")
						             .header("X-Access-Token", "Bearer API-ACCESS-TOKEN")
						             .header("Content-Type", "application/json")
							          .body("{\n    \"username\": \""+username+"\",\n    \"password\": \""+saltStr+"\",\n    \"traffic\": "+wifi3+"\n}")
						             .asJson();
						           JSONObject ovke = response3.getBody().getObject();
						           String oke = ovke.get("id").toString();
						           System.out.println("{\n    \"username\": \""+username+"\",\n    \"password\": \""+saltStr+"\",\n    \"traffic\": "+wifi3+"\n}");
						           if(response3.getStatus()==200) {
						        	   csv casio = new csv();
						        	   casio.writeSpecific2(id+","+oke, "test.txt");
						        	   channel.sendMessage("congratz new user").queue();
						        	   return;
				    		   }
				        	  
					        	   channel.sendMessage("cant create account").queue();
                                   return;
				    	   }
							   EmbedBuilder eb = new EmbedBuilder();
							   eb.setTitle("An error occured");
							   eb.setDescription("Something happend, please try again!");
					    	   channel.sendMessage(eb.build()).queue();
					    	   return;
						   }
				           
						   EmbedBuilder eb = new EmbedBuilder();
						   eb.setTitle("An error occured");
						   eb.setDescription("Data has already been claimed or incorrect information entered!");
				    	   channel.sendMessage(eb.build()).queue();
				    	   return;
				       }
				       EmbedBuilder eb = new EmbedBuilder();
					   eb.setTitle("Data not found!");
					   eb.setDescription("Either your id is claimed or you input the wrong details!");
			    	   channel.sendMessage(eb.build()).queue();
			    	   return;
			} catch (UnirestException e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				channel.sendMessage("Error occured try again!").queue();
			}
    	 }
    	
    	 
     }
     if(messa.equals("$regions")) {
    	 MessageChannel channel = event.getChannel();
    	  EmbedBuilder eb = new EmbedBuilder();
    	  eb.setTitle("Available regions");
          eb.setDescription("`Albania` `Algeria` `Andorra` `Angola Antigua and Barbuda` `Argentina` `Armenia` `Australia` `Austria` `Azerbaijan` `Bahrain` `Bangladesh` `Barbados` `Belarus` `Belgium` `Bolivia Bosnia and Herzegovina` `Botswana` `Brazil` `Bulgaria` `Burkina Faso` `Cambodia` `Cameroon` `Canada` `Cape Verde` `Chile` `China` `Colombia` `Congo` `Costa Rica` `Cote D'Ivoire` `Croatia` `Cuba` `Cyprus` `Czech Republic` `Denmark` `Dominican Republic` `Ecuador` `Egypt` `El Salvador` `Equatorial` `Guinea` `Estonia` `Ethiopia` `Finland` `France` `French` `Guiana` `Gabon` `Georgia` `Germany` `Ghana` `Greece` `Grenada` `Guatemala` `Haiti` `Honduras` `Hong Kong` `Hungary` `India` `Indonesia` `Iran`, `Islamic Republic of Iraq` `Ireland` `Israel` `Italy` `Jamaica` `Japan` `Jordan` `Kazakhstan` `Kenya` `Korea`, `Republic of Kuwait Kyrgyzstan Lao` `People's Democratic Republic Latvia Lebanon Libyan Arab` `Jamahiriya` `Lithuania` `Luxembourg` `Macao` `Macedonia`, `the Former Yugoslav` `Republic of Madagascar` `Malaysia` `Malta` `Mauritius` `Mexico` `Moldova`, `Republic of Mongolia` `Montenegro` `Morocco` `Mozambique` `Myanmar` `Namibia` `Nepal` `Netherlands` `New Caledonia` `New Zealand` `Nicaragua` `Nigeria` `Norway` `Oman` `Pakistan` `Palestine`, `State of Panama` `Paraguay` `Peru` `Philippines` `Poland` `Portugal` `Qatar` `Romania` `Russian Federation` `Saint Kitts and Nevis` `Saint Vincent and the Grenadines` `Saudi Arabia` `Senegal` `Serbia` `Singapore` `Slovakia` `Slovenia` `South Africa` `Spain` `Sri Lanka` `Sudan` `Suriname` `Sweden` `Switzerland` `Taiwan`,`China` `Tanzania`,`Thailand` `Togo` `Trinidad and Tobago` `Tunisia` `Turkey` `Ukraine` `United Arab Emirates` `United Kingdom` `United States` `Uruguay` `Uzbekistan` `Venezuela` `Viet Nam` `Yemen` `Zambia` `Zimbabwe`");
          channel.sendMessage(eb.build()).queue();
     }
     if(messa.contains("$gen")) {
    	 MessageChannel channel = event.getChannel();
    	 String tempeor = messa.replace("$gen ", "");
         Pattern p = Pattern.compile("\\d+");
         Matcher m = p.matcher(tempeor);
         String generator="";
         while(m.find()) {
             generator = m.group();
         }
         int numbers = Integer.parseInt(generator);
         tempeor = tempeor.replace(generator+" ", "");
         if(numbers<10000) {
        	 csv cos = new csv();
        	 String veri = cos.getId(id);
        	 if(veri!=null) {
        		HttpResponse<JsonNode> response;
				try {
					response = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users/"+veri)
					.header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
					.asJson();
			  		JSONObject json = (JSONObject) response.getBody().getObject();
	        		String nama = json.get("username").toString();
	        		String kataLaluan = json.get("password").toString();
	        		System.out.println(nama+" "+kataLaluan);
	        		if(response.getStatus()==200) {
	        			channel.sendMessage(nama+" "+kataLaluan+" "+generator+" "+tempeor).queue();
	        			utilities util = new utilities();
	        			String attachment = "";
	        			Unirest.setTimeouts(0, 0);
	        			HttpResponse<JsonNode> response3 = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/access/countries")	
	        			  .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
	        			  .asJson();
	        			JSONArray array = (JSONArray) response3.getBody().getObject().get("countries");
	        			for(int l = 0; l<array.length(); l++) {
	        				JSONObject jeck = (JSONObject) array.get(l);
	        				String countryname = jeck.get("name").toString();
	        				if(countryname.equals(tempeor)) {
	        				   String countrycode = jeck.get("code").toString();
	        				   if(response3.getStatus()==200) {
	        					  if(numbers>=22) {
	        		        	     for(int i = 0; i<numbers; i++) {
	        			        		 attachment="data.mamakproxies.com:12323:"+nama+":"+kataLaluan+"_session-"+util.generateRandomString(8)+"_country-"+countrycode;
	        			        		 cos.writeSpecific2(attachment, "temp.txt");
	        			    
	        			        	    }
	        			        		  channel.sendFile(new File("temp.txt"),"Proxies generated").queue();
	        			        		  cos.deleteFile("temp.txt");
	        			        		   return;
	        		        			}
	        		        			if(numbers<22) {
	        		        			for(int i = 0; i<numbers; i++) {
	        		        				attachment+="data.mamakproxies.com:12323:"+nama+":"+kataLaluan+"_session-"+util.generateRandomString(8)+"_country-my\n";
	        		    
	        		        			}
	        		        			attachment = "```"+attachment+"```";
	        		        			channel.sendMessage(attachment).queue();
	        		        			return;
	        		        			}
	        					}
	        				}	
	        				
	        			}
	        			
	        		
	        			
	        			//connect api
	        			//if api ==200
	        			//while loop send Message message = new MessageBuilder().append("My message").build();
	        			//textChannel.sendFile(new File("my-file.txt"), message).queue();
	        		}
	        		channel.sendMessage("Error Occured");
				} catch (UnirestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		 
        		 
        	 }
         }
    	 
     }
     
 }
}
}
