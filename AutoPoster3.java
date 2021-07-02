import java.io.BufferedReader;
import java.io.FileReader;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.User;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;


public class AutoPoster3 {
	
	
	public static LocalDateTime convertToLocalDateTime (String date, String time, String am_pm) {
		
		//set date and time input format patterns
		DateTimeFormatter readDatePattern = DateTimeFormatter.ofPattern("M/dd/yyyy");
		DateTimeFormatter readTimePattern = DateTimeFormatter.ofPattern("h:mm a");
		
		//create localDateTime object
		LocalDateTime ldt = LocalDateTime.of(LocalDate.parse(date, readDatePattern), 
				LocalTime.parse(time + " " + am_pm, readTimePattern));
		
		return ldt;
	}
	
	
	public static void schedulePost(FacebookClient fbClient, String id) throws Exception {
		
		//call scheduler class, convertToJSON method to get schedule
		Scheduler.convertToJSON();
		
		//read csv file row by row storing data into variables
		BufferedReader csvReader = new BufferedReader(new FileReader("C:\\Users\\j7b5w2\\eclipse-workspace\\ICS370_AutoPoster\\posting_schedule.csv"));
		String row;
		String inId = "";
		String inDate = "";
		String inTime = "";
		String inAmPm = "";
		String inSlide = "";
		
		//find current date and time set to now object
		LocalDateTime now = LocalDateTime.now();
		
		//read line of csv file as long as it is not null
		while ((row = csvReader.readLine()) != null) {
			//split row on commas and put into array
			String[] data = row.split(",");
			//ignore first line of csv that contains headers
			if(!(data[0].equals("id"))) {
			    inId = data[0];
			    inDate = data[1];
				inTime = data[2];
				inAmPm = data[3];
				inSlide = data[4];
			
				//convert date, time, and ampm to a Date object
				LocalDateTime nextTime = convertToLocalDateTime(inDate, inTime, inAmPm);
				
				//set variable for image to be used in post
				String photopath = "C:\\Users\\j7b5w2\\Desktop\\tree.jpg";
				
				//create byte array for photograph
				byte[] array = Files.readAllBytes(Paths.get(photopath));
				
				//Message to post with id added
				String msg = "Post #" + inId;
  
				long diff = ChronoUnit.MILLIS.between(now, nextTime);
				Thread.sleep(diff);
				
  				//Graph api to publish photo and message
		         GraphResponse publishPhotoResponse = fbClient.publish(id+"/photos", GraphResponse.class,
				 BinaryAttachment.with("tree.jpg", array),
				 Parameter.with("message", msg));
		
		         System.out.println("Published photo ID: " + publishPhotoResponse.getId());
				
		         now = LocalDateTime.now();
			}
		}
		csvReader.close();
		System.out.println("Your messages have been scheduled to post on " + fbClient.fetchObject(id, User.class).getName());
	}
	public static void main(String[] args) throws Exception {
		
		//Copied token of FB Access token (for me I think)
		String accessToken = "EAAHIdfpi6qEBAOs5M0gPLHVlqJ2Wj96034FZCc9fauQyaZBzQkx3jBwWCKavHbQiQsOBADW70WoYcRSAALKTMjLik9var4yP80OE8f3DckpAJgL9ZAkQWCoqu5ZCgCnjFKyEaeRzDZCVYdhZBQCH2y9ASSqZBoisUJZAJbz1U07UZCAZDZD";
		///token expires=Thu Aug 30 2021
		
		String appID = "501884014226081";
		String appSec = "b299273d784215b949ee1e5d33e0bab5";
		
		FacebookClient fbClient = new DefaultFacebookClient(accessToken, appSec, Version.LATEST);
		 
		//Group Page ID
		String id = "845188109729480";
		String myId = "10220337028592875";
		
		//class group page
		String id370 = "540573690634067";
		
		
		schedulePost(fbClient, id370);
	}				
} 


		
		
	

