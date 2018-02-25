package ng.com.eliconcepts.productlist;

/**
 * Created by Somee on 17/12/2017.
 */

public class ListUnit {
    //this would be an object that outlines what an each object should contain
       private String id;



    private String name;
    private String price;
     private String image;
    // int image;
    private String color;
    private String viewColor;


    //getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getViewColor() {
        return viewColor;
    }

    public void setViewColor(String viewColor) {
        this.viewColor = viewColor;
    }
}
