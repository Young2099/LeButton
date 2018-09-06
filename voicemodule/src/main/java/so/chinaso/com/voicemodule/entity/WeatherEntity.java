package so.chinaso.com.voicemodule.entity;

/**
 * Created by yf on 2018/8/31.
 */
public class WeatherEntity {
    private String city;
    private String date;
    private String temp;
    private String weather;
    private String airQuality;
    private String wind;
    private String windLevel;
    private String tempRange;
    /**
     * 0 晴天
     * 1 云
     * 2 阴
     * 4 雷阵雨
     * 7 小雨
     * 8 中雨
     */
    private String weatherType;
    private String date_for_voice;
    //天气图片
    private String img;
    private String week;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getAir() {
        return airQuality;
    }

    public void setAir(String air) {
        this.airQuality = air;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getDate_for_voice() {
        return date_for_voice;
    }

    public void setDate_for_voice(String date_for_voice) {
        this.date_for_voice = date_for_voice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", temp='" + temp + '\'' +
                ", weather='" + weather + '\'' +
                ", airQuality='" + airQuality + '\'' +
                ", wind='" + wind + '\'' +
                ", windLevel='" + windLevel + '\'' +
                ", tempRange='" + tempRange + '\'' +
                ", weatherType='" + weatherType + '\'' +
                ", date_for_voice='" + date_for_voice + '\'' +
                ", img='" + img + '\'' +
                ", week='" + week + '\'' +
                '}';
    }
}
