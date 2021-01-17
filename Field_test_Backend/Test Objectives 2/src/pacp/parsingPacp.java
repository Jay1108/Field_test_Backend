package pacp;


import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

import java.io.File;
import java.io.IOException;
 
public class parsingPacp{
    public static void main(String[] args) throws IOException{
    	try{
	    	File file = new File("D:\\test\\");
	    	String[] allFileName = file.list();
	    	for(String fileName : allFileName) {
    			System.out.println("解析檔名:" + fileName);
    			final Pcap pcap = Pcap.openStream("D:\\test\\" + fileName);
    			System.out.println("解析中");
    			pcap.loop(new PacketHandler(){
					public boolean nextPacket(final Packet packet) throws IOException{
					Buffer buffer = packet.getPacket(Protocol.IPv4).getPayload(); 
					if (buffer != null){
						System.out.println(buffer);
					}
					return true;
    				}
    			});
    			System.out.println("解析完成");
    		}
    	}catch(Exception e){
    		System.out.println(e.toString());
    	}
    }
}
