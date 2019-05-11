public class SensorData {
  private String type;
  private String id;
  private long time;
  private double value;

  public SensorData(String type, String id, long time, String value) {
    this.type = type;
    this.id = id;
    this.time = time;
    this.value = Double.parseDouble(value);
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public long getTime() {
    return time;
  }

  public double getValue() {
    return value;
  }
}
