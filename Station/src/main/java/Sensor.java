import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Sensor {

  private String id;
  private String type;
  private ArrayList<SensorData> measurements;

  public Sensor(String id, String type) {
    this.id = id;
    this.type = type;
    measurements = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public void addSensorData(String type, String id, long timestamp, String value) {
    measurements.add(new SensorData(type, id, timestamp, value));
  }

  public void addSensorData(SensorData measurement) {
    measurements.add(measurement);
   /* try {
      FileWriter fw = new FileWriter(measurement.getId() + ".json", true);
      fw.write(measurement.getTime() + ": " + measurement.getValue() + "\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }

  public int measurementCount() {
    return measurements.size();
  }

  public ArrayList<SensorData> getMeasurements() {
    return measurements;
  }


}
