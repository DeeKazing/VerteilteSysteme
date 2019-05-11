import java.util.ArrayList;

public class DataStorage
{
  // static variable single_instance of type Singleton
  private static DataStorage single_instance = null;


  public ArrayList<Sensor> sensors;

  private DataStorage()
  {
    sensors = new ArrayList<>();
  }



  public static DataStorage getInstance()
  {
    if (single_instance == null)
      single_instance = new DataStorage();

    return single_instance;
  }
}

