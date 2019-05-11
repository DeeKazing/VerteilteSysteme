import java.net.UnknownHostException;

public class Main {
  public static void main(String[] args){
    //InetAddress udpAddress = InetAddress.getByName(args[1]);
    //nt udpPort = Integer.parseInt(args[1]);
    //InetAddress tcpAddress;
    //int tcpPort;
    if(args.length!=3){
      System.err.println("Usage <id> <ip> <port>");
      System.exit(0);
    }
    UDPHandler udp = new UDPHandler(args);
    TCPHandler tcp = null;
    try {
      tcp = new TCPHandler(args);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    udp.start();
    tcp.start();
  }
}
