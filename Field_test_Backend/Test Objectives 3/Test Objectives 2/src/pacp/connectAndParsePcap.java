package pacp;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class connectAndParsePcap {
    public static void main(String args[]) {
        try {
        	//載入MYSQL JDBC驅動程式
            Class.forName("com.mysql.jdbc.Driver");     
	    }catch (Exception e) {
	        System.out.print("Error loading Mysql Driver!");
	        e.printStackTrace();
	    }
	    try {
	        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/testdb?serverTimezone=CST", "root", "123456");
	        //取得pcap內的資料map
	        Map<String, String> pcapMap = connectAndParsePcap.prasePcap();
	        Statement stmt = connect.createStatement();
	        //將map的值取出使用insert SQL寫入DB
	        ResultSet rs = stmt.executeQuery("insert into testTable ");
	    }catch (Exception e) {
	        System.out.print("get data error!");
	        e.printStackTrace();
	    }
	}
    
    public static Map<String, String> prasePcap() throws IOException{
    	final Map<String, String> pcapValueMap = null;
    	try{
	    	File file = new File("//root//tcpdump");
	    	String[] allFileName = file.list();
	    	for(String fileName : allFileName) {
    			final Pcap pcap = Pcap.openStream("//root//tcpdump" + fileName);
    			pcap.loop(new PacketHandler(){
					public boolean nextPacket(final Packet packet) throws IOException{
					Buffer buffer = packet.getPacket(Protocol.PCAP).getPayload(); 
					if (buffer != null){
						//解析的結果資料不足，無法取得所有資訊
						pcapValueMap.put(buffer.toString(), buffer.toString());
					}
					return true;
    				}
    			});
    		}
    	}catch(Exception e){
    		System.out.println(e.toString());
    	}
    	return pcapValueMap;
    }
}
