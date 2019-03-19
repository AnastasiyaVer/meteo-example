package meteo.example.meteo.meteo_services;

public class Weather {
    private int temperature;
    private int windSpeed;

    public Weather() {
    }

    public Weather(int temperature, int windSpeed) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
