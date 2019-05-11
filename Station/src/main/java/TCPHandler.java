import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Pattern;

public class TCPHandler extends Thread {

  private InetAddress address;

  static String writeHeader(String status, String length) {
    return ("HTTP/1.1 " + status + "\r\nContent-Length: " + length
        + "\r\nAccess-Control-Allow-Origin: * \r\nContent-Type: application/json\r\n\r\n");
  }

  private static String printForbidden() {
    return writeHeader("403 Forbidden", "0");
  }

  public TCPHandler(String[] args) throws UnknownHostException {
    this.address = InetAddress.getByName(args[1]);
  }

  @Override
  public void run() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(8000,1, address);
      while (true) {
        Socket cliSocket = socket.accept();
        Thread thread = new Thread(() -> {
          BufferedReader fromClient = null;
          try {
            fromClient = new BufferedReader(
                new InputStreamReader(cliSocket.getInputStream()));
          } catch (IOException e) {
            e.printStackTrace();
          }
          DataOutputStream toClient = null;
          try {
            toClient = new DataOutputStream(cliSocket.getOutputStream());
          } catch (IOException e) {
            e.printStackTrace();
          }
          HTTPRequest httpreq = null;
          try {
            httpreq = new HTTPRequest(fromClient);
          } catch (IOException e) {
            e.printStackTrace();
          }
          if (httpreq.method.equals("GET")) {
            if (Pattern.matches("/api/((rain)|(wind)|(humidity)|(temperature))/history",
                httpreq.path)) {
              String answer = getHistory(sensorType(httpreq.path));
              try {
                toClient.writeBytes(
                    writeHeader("200 OK", Integer.toString(answer.length())) + answer);
              } catch (IOException e) {
                e.printStackTrace();
              }
            } else if (Pattern
                .matches("/api/((rain)|(wind)|(humidity)|(temperature))", httpreq.path)) {
              String answer = getLast(sensorType(httpreq.path));
              try {
                toClient.writeBytes(
                    writeHeader("200 OK", Integer.toString(answer.length())) + answer);
              } catch (IOException e) {
                e.printStackTrace();
              }

            } else {
              try {
                toClient.writeBytes(printForbidden());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          } else {
            try {
              toClient.writeBytes(printForbidden());
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          try {
            toClient.close();
            cliSocket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        thread.run();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getLast(String type) {
    Optional<Sensor> s = DataStorage.getInstance().sensors.stream()
        .filter(x -> x.getType().equals(type)).findFirst();
    if (s.isPresent()) {
      SensorData sd = s.get().getMeasurements().get(s.get().measurementCount()-1);
      return String.format("\tid: %s,\ttype: %s\tvalue: %2f\n", sd.getId(),
          sd.getType(), sd.getValue());
    } else {
      return "No Sensor Data found!";
    }
  }

  public static String getHistory(String sensorType) {
    StringBuilder result = new StringBuilder();
    for (Sensor x : DataStorage.getInstance().sensors) {
      if (x.getType().equals(sensorType)) {
        result.append(String.format("Sensor %s%n", x.getId()));
        if (x.measurementCount() != 0) {
          for (SensorData y : x.getMeasurements()) {
            result.append(String
                .format("\tid: %s,\ttype: %s\tvalue: %2f\n", y.getId(),
                    y.getType(), y.getValue()));
          }
        } else {
          result.append("No data yet!\n");
        }
      }
    }
    return result.toString();
  }


  private static String sensorType(String path) {
    String ret;
    if (path.contains("temperature")) {
      ret = "temperature";
    } else if (path.contains("rain")) {
      ret = "rain";
    } else if (path.contains("humidity")) {
      ret = "humidity";
    } else if (path.contains("wind")) {
      ret = "wind";
    } else {
      ret = "Unknown";
    }
    return ret;

  }


}
