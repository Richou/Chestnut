package net.heanoria.droid.chestnut.domains;

public class Address {

    private String address;
    private String type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Address emptyAddress() {
        return new Address();
    }
}
