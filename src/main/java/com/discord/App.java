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
       JDA jda = JDABuilder.createDefault("ODQ2NjY5MzgxODExMTc1NDI0.YKy4Tg.FvF_AZbrdcFYYXECNfuPSMuLwxs").build();
       jda.addEventListener(new bot());
       jda.getPresence().setActivity(Activity.listening("$help in DMs	"));

    }
}
