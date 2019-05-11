import java.util.Random;

public class Generator {

  String type;
  double value;
  Random rand;
  boolean windflag = false;
  boolean temperaturflag = false;
  boolean humidityflag = false;
  boolean rainflag = false;

  public Generator(String t) {
    this.rand = new Random();
    this.type = t;
    value = 999;
  }

  public double generate() {
    switch (this.type) {
      case "wind":
        if (windflag == false) {
          windflag = true;
          value = rand.nextInt(250);
        }else{
          value = value + rand.nextInt(20) - 10;
        }
        break;
      case "temperature":
        if (temperaturflag == false) {
          temperaturflag = true;
          value = rand.nextInt(40);
        }else{
          value = value + rand.nextInt(4) - 2;
        }
        break;
      case "humidity":
        if (humidityflag == false) {
          humidityflag = true;
        value = rand.nextInt(50);
        }else{
          value = value + rand.nextInt(6) - 3;
        }
        break;
      case "rain":
        if (rainflag == false) {
          rainflag = true;
          value = rand.nextInt(10);
        }else{
          value = value + rand.nextInt(2) - 1;
        }
        break;
      default:
        break;
    }
    return value;
  }
}
