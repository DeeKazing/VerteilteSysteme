import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;

public class UDPHandler extends Thread {

  private Gson gson = new Gson();
  private String address;
  private int port;
  private String id;

  UDPHandler(String[] args) {
    this.id = args[0];
    this.address = args[1];
    this.port = Integer.parseInt(args[2]);
  }


  @Override
  public void run() {
    int dataLength = 65535;
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket(this.port, InetAddress.getByName(this.address));
      //socket.bind(new InetSocketAddress(this.address,this.port));
    } catch (SocketException | UnknownHostException e) {
      e.printStackTrace();
    }
    DatagramPacket inPacket;

    System.out.println("UDP Echo Server started....");
    while (true) {
      byte[] data = new byte[dataLength];
      inPacket = new DatagramPacket(data, data.length);
      try {
        socket.receive(inPacket);
      } catch (IOException e) {
        e.printStackTrace();
      }

      final String line = new String(inPacket.getData());

      SensorData m = gson.fromJson(line.trim(), SensorData.class);

      Optional<Sensor> s = DataStorage.getInstance().sensors.stream()
          .filter(x -> x.getId().equals(m.getId())).findFirst();

      if (s.isPresent()) {
        s.get().addSensorData(m);
      } else {
        Sensor newSens = new Sensor(m.getId(), m.getType());
        newSens.addSensorData(m);
        DataStorage.getInstance().sensors.add(newSens);
      }

      System.out.println(inPacket.getAddress() + " " + inPacket.getPort());
      System.out.println(String
          .format("ID: %s%nTYPE: %s%nTIME: %d%nVALUE: %s%n", m.getId(), m.getType(), m.getTime(),
              m.getValue()));
    }
  }
}
