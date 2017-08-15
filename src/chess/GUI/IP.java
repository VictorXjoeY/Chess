package chess.GUI;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration; 
/**
 * This class is used to return the machine IP.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 * @author Otávio Luis de Aguiar - 9293518
 *
 */
public class IP { 
	/**
	 * This method returns the machine IP.
	 * @return
	 */
   public static String get() { 
      Enumeration<NetworkInterface> e = null;
	try {
		e = NetworkInterface.getNetworkInterfaces();
	} catch (SocketException e1) {
	      return "127.0.0.1";
	} 
      while (e.hasMoreElements()) { 
         NetworkInterface i = e.nextElement(); 
         Enumeration<InetAddress> ds = i.getInetAddresses(); 
         InetAddress myself = ds.nextElement();
         myself = ds.nextElement();
         return myself.getHostAddress() +"";
      }
      return "127.0.0.1";
   } 
} 