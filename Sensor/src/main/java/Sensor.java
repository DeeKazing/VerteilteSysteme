import com.google.gson.JsonObject;
import java.net.*;

public class Sensor {

  public static void main(String[] args) throws Exception {
    if (args.length != 5) {
      System.err.println("Usage: <id> <type> <ip> <port> <interval>");
      System.exit(0);
    }
    Generator gen = new Generator(args[1]);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", args[0]);
    jsonObject.addProperty("type", args[1]);
    InetAddress IpAddress = InetAddress.getByName(args[2]);
    int port = Integer.parseInt(args[3]);
    DatagramSocket socket = new DatagramSocket();

    while (true) {
      jsonObject.addProperty("time", System.currentTimeMillis());
      jsonObject.addProperty("value", gen.generate());
      byte[] toSend = jsonObject.toString().getBytes();
      try {
        socket.send(new DatagramPacket(toSend, toSend.length, IpAddress, port));
        System.out.println(System.currentTimeMillis() + " sent package: " + new String(toSend));
      } catch (SocketException e) {
        System.err.println("Could not send package!");
      }
      Thread.sleep(Long.parseLong(args[4]));

    }
  }
}