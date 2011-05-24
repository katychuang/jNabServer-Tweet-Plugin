package jNab.ext.helperPlugins;

import jNab.core.bunny.Bunny;
import jNab.core.events.ClickEventListener;
import jNab.core.events.PingEventListener;
import jNab.core.events.StopEventListener;
import jNab.core.events.RFIDEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


/**
 * in /Tomcat/apache-tomcat-6.0.29/webapps/jNabServer/src/jNab/ext/helperPlugins
 * Plugin for tweeting.
 * 
 * 
 * @author Kat Chuang
 

	Server Command - ADD bunny plugin: 002185ba8277 Tweet_Demo
 */
 
public class TweetDemo extends AbstractPlugin implements RFIDEventListener //ClickEventListener, 
{
	

	// A Hack to test the working of oauth.
	private static boolean test = true;
	
	// Your OAUTH consumer and secret keys
	private final static String OAUTH_CONSUMER = "4YeMHSvzkJr9fCen8fNqw";
	private final static String OAUTH_SECRET = "V0J6uQMPYdYjHCCn08K2qoZHVDS1838oMu4DH7wX2Fo";	
	String tUser = "totoronab";
	String tPasswd = "shoeseum2010";
    private static String PLUGIN_NAME = "Tweet_Demo";	/** Plugin name. */
    private static String[] PARAMETERS = {};			/** Set of parameters supported by plugin; */
    private int level;									/** Level of the monitored process.*/
    /*** Level (0:low, 1:medium, 2:high) of the monitored process.*/
    private long lastSwitchingTime;				/*** The last time a level switch occured.*/
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    /**
     * Creating a new plugin instance.
     */
    public TweetDemo() throws Exception{
		super(PLUGIN_NAME, PARAMETERS);
		
		this.level = 1;
		this.lastSwitchingTime = System.currentTimeMillis();
		
		try {
			// Construct data
			String status = "status=jNabServer plugin started at: " + TweetDemo.now();
			
			// Send data
			URL url = new URL("http://machinetweets.com/t/totoronab/w9pj53rjvsxb0fn2");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(status);
			wr.flush();
	 
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
		}		
			

    }//end public tweetdemo() 

    
    public static String now() {
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
   	 	return sdf.format(cal.getTime());
	}

    private String getChoreography() {
		switch (this.level){
		case 0:
			return "GreenBlinkingWithEars";
		case 1:
			return "OrangeBlinkingWithEars";
		case 2:
			return "RedBlinkingWithEars";
		default:
			return "raisedEars";
		}
    }
// 
//   private static void storeAccessToken(int useId, AccessToken accessToken){
//     //store accessToken.getToken()
//     //store accessToken.getTokenSecret()
//   }

	/**
     * @see jNab.core.events.RFIDEventListener#onRfid(java.lang.String)
     */
    public void onRfid(String rfid) {
    
		try {
			// Construct data
			String status = "status=RFID tag identified at " + TweetDemo.now() + " (tagID=" + rfid + ")";
	 
			// Send data
			URL url = new URL("http://machinetweets.com/t/totoronab/w9pj53rjvsxb0fn2");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(status);
			wr.flush();
	 
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
			System.out.println("~*~*~*~*~*~* Error = " + e + ")\n\n");
		}		
    
    	Packet p = new Packet();
		System.out.println("~*~*~*~*~*~* RFID event received (tagID=" + rfid + ") ~*~*~*~*~*~*  \n\n");
	
		Random rd = new Random();
		this.level = rd.nextInt(4) + 1;
	
		// Playing the associated sound files
		MessageBlock mb = new MessageBlock(333);
		mb.addPlayChoreographyFromLibraryCommand(this.getChoreography());
		mb.addWaitPreviousEndCommand(); 
		p.addBlock(mb);
		p.addBlock(new PingIntervalBlock(1));
		this.bunny.addPacket(p);
	}
	
}//end public class tweetdemo
