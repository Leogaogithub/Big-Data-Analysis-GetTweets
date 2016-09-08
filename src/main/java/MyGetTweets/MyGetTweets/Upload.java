package MyGetTweets.MyGetTweets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class Upload {
	public static void upload(String localSrc, String dst) throws Exception{		 
		InputStream in;
		in = new BufferedInputStream(new FileInputStream(localSrc));
		Configuration conf = new Configuration();				
		//conf.set("fs.default.name", "hdfs://localhost:9000");
	    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));

	    FileSystem fs = FileSystem.get(URI.create(dst), conf);
	    OutputStream out = fs.create(new Path(dst), new Progressable() {
	      public void progress() {
	        System.out.print(".");
	      }
	    });	    
	    IOUtils.copyBytes(in, out, 4096, true); 
	    System.out.println("we have upload the file to " + dst);
	}
	
	
	public static void UploadDirectoryOrFile(String localDir, String dstDir) throws Exception{		
		File folder = new File(localDir);
		ArrayList<String> fileNames = new ArrayList<String>();
		if(folder.isFile()){			
			upload(localDir, dstDir+"/"+localDir);
		}else{
			File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  fileNames.add(listOfFiles[i].getName());
		      } 
		    }
		    for(String fileName : fileNames){						
				upload(localDir+"/"+fileName, dstDir+"/"+fileName);			
			}
		}				
		
	}
}
