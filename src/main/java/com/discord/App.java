package com.discord;



import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws LoginException
    {
    	csv start = new csv();
    	start.deleteFile("test.txt");
    	start.csvdataInitialize();
       JDA jda = JDABuilder.createDefault("NjM1Mzc2MzIyOTAxOTY2ODQ4.XawKUA.9pe8OM9ORjMujM3dKqdPmL2I8kc").build();
       jda.addEventListener(new bot());
       jda.getPresence().setActivity(Activity.listening("$help in DMs	"));

    }
}
