package connectMySql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class connectMySQL {
    public static void main(String args[]) throws Exception {
    	String configStr = "";
    	String response = "";
        try {
        	FileReader file = new FileReader("//usr//local//nginx//html//config.ini");
        	if(file != null){
        		BufferedReader br = new BufferedReader(file);
        		while (br.ready()) {
        			configStr += br.readLine();
        		}
        	}
    		String[] config = configStr.split("@@@")[1].split(",");
    		Map<String, String> configMap = new HashMap<String, String>();
    		for(String tempStr : config){
    			String[] tempArray = tempStr.split("=");
    			configMap.put(tempArray[0], tempArray[1]);
    		}
    		if(configMap.get("logger").equals("1")){
    			try{
    				response = connectMySQL.httpRequest(configMap.get("URL"));
    			}catch (Exception e) {
    				response = "Can’t connect to " + configMap.get("URL");
    			}
    		}
	    }catch (Exception e) {
	    	throw new Exception(e.toString());
	    }
	    connectMySQL.outPutLog(response);
	}
    
    public static String httpRequest(String req_url) throws Exception {
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(req_url);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  

            httpUrlConn.setDoOutput(false);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  

            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  

            // 將返回的輸入流轉換成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  

            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            //釋放資源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  

        } catch (Exception e) {  
            System.out.println(e.getStackTrace()); 
            throw new Exception(e.toString());
        }  
        return buffer.toString();  
    } 
    
    public static void outPutLog(String response){
    	String returnLog = "";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	String nowStr = sdf.format(new Date());
    	returnLog = "[" + nowStr + "]";
    	if(response.contains("OK")){
    		returnLog += "OK:PID:32471, response: " + response;
    	}else if(response.contains("ERROR")){
    		returnLog += "ERROR:PID:32472 ,response: " + response;
    	}else{
    		returnLog += " ERROR:PID:32473 ,ERROR-" + response;
    	}
    	connectMySQL.logtest(returnLog);
    }
    
    public static void logtest(String returnLog) {
    	String path = "//home//logs//test.log";
    	String logStr = "";
    	try {
    		File files = new File(path);
    		if(files.exists()){
	    		FileReader file = new FileReader(path);
	        	if(file != null){
	        		BufferedReader br = new BufferedReader(file);
	        		while (br.ready()) {
	        			logStr += br.readLine() + "\n";
	        		}
	        	}
	        	file.close();
    		}
    		FileWriter fw = new FileWriter(path);
    		fw.write(logStr + returnLog);
    		fw.flush();
    		fw.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	 }
}
