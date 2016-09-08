package MyGetTweets.MyGetTweets;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Hello world!
 *
 */
public class App 
{	
	StatusListener listener = null;
	
	void getTweets(String[] track, String fileName,String hdfsDirectory, int maxCount){		
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(track);
        tweetFilterQuery.language("en");
        listener = new MyStatusListener(fileName,hdfsDirectory, maxCount, twitterStream);
        twitterStream.addListener(listener);        
        twitterStream.filter(tweetFilterQuery);       
	}
	
	
	 public static void main(String[] args) throws Exception{
		 if(args.length < 2){
			 System.out.println("usage: hadoop jar MyGetTweets-1.0-jar-with-dependencies.jar  <tweetsNum> <tweetJsonName> <hdfsDirectory>  <topic>");
			 return ;
		 }
		int maxCount = Integer.parseInt(args[0]);
		String tweetJsonName = args[1];
				
		String hdfsDirectory = args[2];			 
		String track[] = null;	//topic	
		int tracklength = args.length-3;
		track = new String[tracklength];		
		for(int i = 0; i < tracklength; i++){
			track[i] = args[i+3];
		 }
		 	 
		 App app = new App();
		 app.getTweets(track, tweetJsonName,hdfsDirectory, maxCount); 		
    }
}

class MyStatusListener implements StatusListener{	
	FileOutputStream fos = null;
	String newLine = "\n\n";
	int count = 0;
	int maxCount = 0;
	TwitterStream twitterStream = null;
	String hdfsDirectory = "";
	String localDir = ".";
	String fileName = "";
	
	public MyStatusListener(String fileName, String hdfsDirectory, int maxCount, TwitterStream twitterStream){
		this.twitterStream = twitterStream;
		this.hdfsDirectory = hdfsDirectory;
		this.fileName = fileName;
		this.maxCount = maxCount;
		try {
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	public void onException(Exception arg0) {}
	public void onDeletionNotice(StatusDeletionNotice arg0) {}
	public void onScrubGeo(long arg0, long arg1) {}
	public void onStallWarning(StallWarning arg0) { }
	public void onStatus(Status status){
		count++;
		if(count > maxCount){
			twitterStream.removeListener(this);			
			 try {
				fos.close();
				Upload.UploadDirectoryOrFile(fileName, hdfsDirectory);
			} catch (Exception e) {				
				e.printStackTrace();
			}
			System.out.println("finish!!!!");
			twitterStream.shutdown();
			Thread.currentThread().interrupt();
			return;
		}
		System.out.println(count);                
        String json = TwitterObjectFactory.getRawJSON(status);
        //System.out.println(json);
        try {        	
			fos.write(json.getBytes());
			fos.write(newLine.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void onTrackLimitationNotice(int arg0) {	}	
}
