package ch.christophlinder.statemachine.demo;

public class OrderLine {
    private String item;

    public OrderLine(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
