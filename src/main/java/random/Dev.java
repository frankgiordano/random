package random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

/*
 * Build a clone of $ cat /dev/random generating your own entropy. 
 * 
 * To build the jar file for this project execute the 
 * following command at project root directory:
 * 
 * 	mvn clean package 
 * 
 * Find dev.random.jar generated within project/target directory
 * 
 * Ways to run the program:
 * 
 *  java -jar dev.random.jar
 *  
 *  java -jar dev.random.jar 100 uint16
 *  
 *  java -jar dev.random.jar 10 uint8
 * 
 */
public class Dev {

    private final static int MAX_LENGTH = 1024;

    private final static String DEFAULT_DATA_TYPE = "uint16";

    private final static String URL = "https://qrng.anu.edu.au/API/jsonI.php?"; 

    private JSONObject data;

    private int length;
    
    private String dataType;
    
    private EntropyPool entropyPool;

    public Dev() {
       this.length = MAX_LENGTH;
       this.dataType = DEFAULT_DATA_TYPE;
       setEntropyPool();
    }

    public Dev(int length) {
       setLength(length);
       this.dataType = DEFAULT_DATA_TYPE;
       setEntropyPool();
    }
    
    public Dev(int length, String dataType) {
    	setLength(length);
        setDataType(dataType);
        setEntropyPool();
    }
    
    private void setLength(int length) {
        if (length > 0 && length <= MAX_LENGTH)
            this.length = length;
        else 
            System.exit(1);
    }

    private void setDataType(String dataType) {
        switch (dataType) {
            case "uint8":
            case "uint16":
            	this.dataType = dataType;
                break;
            default:  
                System.exit(1);
        }
    }

    private JSONObject getData() {
	try {
		return getJsonData();
	} catch (MalformedURLException e) {
		e.printStackTrace();
		System.exit(1);
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(1);
	}
	    
	return null;
     }
	
     private JSONObject getJsonData() throws MalformedURLException, IOException   {
        // full URL example https://qrng.anu.edu.au/API/jsonI.php?length=1024&type=uint16

        String length = String.valueOf(this.length);
        String type = this.dataType;
        String urlString = URL + "length=" + length + "&type=" + type; 
        
        String data = null;
        BufferedReader bufferedReader = null;
        JSONObject jsonObject = null;

	try {
		URL url = new URL(urlString);
		URLConnection urlConnection = url.openConnection();
		InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
		bufferedReader = new BufferedReader(inputStream);
		data = bufferedReader.readLine();
	} finally {
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			}
		}
		
		jsonObject = new JSONObject(data.toString());
		return jsonObject;
     }
	
     private void setEntropyPool() {
	this.data = getData();
	this.entropyPool = new EntropyPool(this.data);
     }
	
     private Integer getNext() {
	Integer value = this.entropyPool.getNext();
	return value;
     }
	
     public int random() {
	Integer value = getNext();
		
	if (value == null) {
	   // if we get here pool is empty mimic a block and refill it
	   setEntropyPool();
	   value = getNext();
	}
		
	return value.intValue();
     }
	
     public static void main(String[] args) {
	int size = args.length;
		
	if (size > 3) 
	   System.exit(1);
		
	Dev dev = null;
	if (size > 0) {
	   dev = new Dev(Integer.parseInt(args[0]), args[1]);
	   System.out.println(dev.random());
	   System.exit(1);
	}
		
	dev = new Dev();
	System.out.println(dev.random());
	System.exit(1);
     }
	
}
