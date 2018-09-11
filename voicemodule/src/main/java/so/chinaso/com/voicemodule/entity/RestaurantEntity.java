package so.chinaso.com.voicemodule.entity;

/**
 * Created by yf on 2018/9/7.
 */
public class RestaurantEntity {
    private String name;
    private String address;
    private String phone;
    private String distance;
    private String img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "RestaurantEntity{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", distance='" + distance + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
