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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
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
	 eb.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");

	 channel.sendMessage(eb.build()).queue();
     }
     if(messa.equals("$data")) {
    	 MessageChannel channel = event.getChannel();
    	 csv cas = new csv();
    	 String yes = cas.getId(id);
    	 if(yes!=null) {
    		 Unirest.setTimeouts(0, 0);
    		 try {
				HttpResponse<JsonNode> response = Unirest.get("https://dashboard.iproyal.com/api/residential/royal/reseller/sub-users/"+yes)
				   .header("X-Access-Token", "Bearer F0D4SA5SmEKh4Q7eLxP2OoZ00JX6S1Oc4A3HayZzsSDeU72wDKlqCppmxIT2")
				   .asJson();
				JSONObject json = (JSONObject) response.getBody().getObject();
				String pl = json.get("availableTraffic").toString(); //changeto availableTraffic on v2
				if(response.getStatus()==200) {
					EmbedBuilder eb1 = new EmbedBuilder();
					   eb1.setTitle("Data: "+pl);
					   eb1.setDescription("<@"+id+"> You can top up your data at `$claim`");
					   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
			    	   channel.sendMessage(eb1.build()).queue();
				
				return;
				}
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				EmbedBuilder eb1 = new EmbedBuilder();
				   eb1.setTitle("User not found");
				   eb1.setDescription("Are you new? Go ahead and purchase our subscription and claim it at `$claim`!");
				   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
		    	  channel.sendMessage(eb1.build()).queue();
			}
    		
    	 }
    	 EmbedBuilder eb1 = new EmbedBuilder();
		   eb1.setTitle("User not found");
		   eb1.setDescription("Are you new? Go ahead and purchase our subscription and claim it at `$claim`!");
		   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
  	       channel.sendMessage(eb1.build()).queue();
    		
    		
    			
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
                            		  EmbedBuilder eb1 = new EmbedBuilder();
									   eb1.setTitle("Data deposited!");
									   eb1.setDescription("Data is deposited! Thanks for using us <@"+id+">! ");
									   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
							    	   channel.sendMessage(eb1.build()).queue();
						        	   return;
                            	 }
                            	  EmbedBuilder eb1 = new EmbedBuilder();
								   eb1.setTitle("An error occured!");
								   eb1.setDescription("Please try again!");
								   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
						    	   channel.sendMessage(eb1.build()).queue();
					        	   return;
                             }
                       	       EmbedBuilder eb1 = new EmbedBuilder();
							   eb1.setTitle("An error occured!");
							   eb1.setDescription("Data already been claimed or wrong details inputted!");
							   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
					    	   channel.sendMessage(eb1.build()).queue();
				        	   return;
				    	   }
				    	   System.out.println("here");
				    	   String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
				           StringBuilder salt = new StringBuilder();
				           Random rnd = new Random();
				           while (salt.length() < 10) { // length of the random string.
				               int index = (int) (rnd.nextFloat() * SALTCHARS.length());
				               salt.append(SALTCHARS.charAt(index));
				           }
				        
				           
				        	   System.out.println("new user");
				        	   HttpResponse<JsonNode> response4 = Unirest.post("https://a201bc7a9475d96ffe85d436af7fc56e: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@mamak-proxies.myshopify.com/admin/api/2021-04/orders/"+userid+"/close.json")
				    				   .header("Content-Type", "application/json")
				    				   .asJson();
				    		   if(response4.getStatus()==200) {
				    			   System.out.println("creating user");
				    			   String saltStr = salt.toString();
						           String username = id;
						    	   api ae = new api();
						    	   String bot = ae.apid(username, saltStr, wifi3);
						           if(bot!=null) {
						        	   csv casio = new csv();
						        	   casio.writeSpecific(id, bot);
						        	   EmbedBuilder eb1 = new EmbedBuilder();
									   eb1.setTitle("Welcome");
									   eb1.setDescription("Data is deposited! Welcome <@"+id+"> ! `$help` for more commands!");
									   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
							    	   channel.sendMessage(eb1.build()).queue();
						        	   return;
				    		   }
				        	  
						           EmbedBuilder eb1 = new EmbedBuilder();
								   eb1.setTitle("An error occured");
								   eb1.setDescription("Account creation failed");
								   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
						    	   channel.sendMessage(eb1.build()).queue();
						    	   return;
				    	   }
							 
						   
				           
						   EmbedBuilder eb1 = new EmbedBuilder();
						   eb1.setTitle("An error occured");
						   eb1.setDescription("Data has already been claimed or incorrect information entered!");
						   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
				    	   channel.sendMessage(eb1.build()).queue();
				    	   return;
				       }
				       EmbedBuilder eb = new EmbedBuilder();
					   eb.setTitle("Data not found!");
					   eb.setDescription("Either your id is claimed or you input the wrong details!");
					   eb.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
			    	   channel.sendMessage(eb.build()).queue();
			    	   return;
			} catch (UnirestException e) {
				EmbedBuilder eb1 = new EmbedBuilder();
				   eb1.setTitle("An error occured!");
				   eb1.setDescription("Error occured please try again!");
				   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
		    	   channel.sendMessage(eb1.build()).queue();
			}
    	 }
    	
    	 
     }
     if(messa.equals("$regions")) {
    	 MessageChannel channel = event.getChannel();
    	  EmbedBuilder eb = new EmbedBuilder();
    	  eb.setTitle("Available regions");
          eb.setDescription("`Albania` `Algeria` `Andorra` `Angola Antigua and Barbuda` `Argentina` `Armenia` `Australia` `Austria` `Azerbaijan` `Bahrain` `Bangladesh` `Barbados` `Belarus` `Belgium` `Bolivia Bosnia and Herzegovina` `Botswana` `Brazil` `Bulgaria` `Burkina Faso` `Cambodia` `Cameroon` `Canada` `Cape Verde` `Chile` `China` `Colombia` `Congo` `Costa Rica` `Cote D'Ivoire` `Croatia` `Cuba` `Cyprus` `Czech Republic` `Denmark` `Dominican Republic` `Ecuador` `Egypt` `El Salvador` `Equatorial` `Guinea` `Estonia` `Ethiopia` `Finland` `France` `French` `Guiana` `Gabon` `Georgia` `Germany` `Ghana` `Greece` `Grenada` `Guatemala` `Haiti` `Honduras` `Hong Kong` `Hungary` `India` `Indonesia` `Iran`, `Islamic Republic of Iraq` `Ireland` `Israel` `Italy` `Jamaica` `Japan` `Jordan` `Kazakhstan` `Kenya` `Korea`, `Republic of Kuwait Kyrgyzstan Lao` `People's Democratic Republic Latvia Lebanon Libyan Arab` `Jamahiriya` `Lithuania` `Luxembourg` `Macao` `Macedonia`, `the Former Yugoslav` `Republic of Madagascar` `Malaysia` `Malta` `Mauritius` `Mexico` `Moldova`, `Republic of Mongolia` `Montenegro` `Morocco` `Mozambique` `Myanmar` `Namibia` `Nepal` `Netherlands` `New Caledonia` `New Zealand` `Nicaragua` `Nigeria` `Norway` `Oman` `Pakistan` `Palestine`, `State of Panama` `Paraguay` `Peru` `Philippines` `Poland` `Portugal` `Qatar` `Romania` `Russian Federation` `Saint Kitts and Nevis` `Saint Vincent and the Grenadines` `Saudi Arabia` `Senegal` `Serbia` `Singapore` `Slovakia` `Slovenia` `South Africa` `Spain` `Sri Lanka` `Sudan` `Suriname` `Sweden` `Switzerland` `Taiwan`,`China` `Tanzania`,`Thailand` `Togo` `Trinidad and Tobago` `Tunisia` `Turkey` `Ukraine` `United Arab Emirates` `United Kingdom` `United States` `Uruguay` `Uzbekistan` `Venezuela` `Viet Nam` `Yemen` `Zambia` `Zimbabwe`");
		  eb.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
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
         System.out.println(numbers);
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
	        		String pl = json.get("availableTraffic").toString(); 
	        		System.out.println(nama+" "+kataLaluan);
	        		System.out.println(pl);
	        		if(response.getStatus()==200) {
	        			if(pl.contains("-") || pl.equals("0")) {
	        				EmbedBuilder eb1 = new EmbedBuilder();
							   eb1.setTitle("Data quota exceeded!");
							   eb1.setDescription(" <@"+id+">, you have currently "+pl+" please $claim!");
							   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
							   channel.sendMessage(eb1.build()).queue();
							   return;
	        			}
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
	        		        	     
	        			        		 attachment="data.mamakproxies.com:12323:"+nama+":"+kataLaluan+"_session-";
	        			        		 cos.writeSpecific2(attachment, "temp.txt", numbers,countrycode);
	        			        		  channel.sendFile(new File("temp.txt"),"`Proxies generated`").queue();
	        			        		  cos.deleteFile("temp.txt");
	        			        		   return;
	        		        			}
	        		        			if(numbers<22) {
	        		        			for(int i = 0; i<numbers; i++) {
	        		        				attachment+="data.mamakproxies.com:12323:"+nama+":"+kataLaluan+"_session-"+util.generateRandomString(8)+"_country-"+countrycode+"\n";
	        		    
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
	        			
	        			
	        		}
	        		EmbedBuilder eb1 = new EmbedBuilder();
					   eb1.setTitle("An error occured!");
					   eb1.setDescription("Please specify the correct country name including uppercase letter and spaces");
					   eb1.setFooter("Mamak Bot", "https://media.discordapp.net/attachments/785375543146184714/825679793957371934/Logo-1.jpg?width=300&height=300");
					   channel.sendMessage(eb1.build()).queue();
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

