package Hilligans.Item;

public class Item {

    public String name;

    public Item(String name) {
        this.name = name;
        Items.ITEMS.add(this);
        Items.HASHED_ITEMS.put(name,this);
    }




}
