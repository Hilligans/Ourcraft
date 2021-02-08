package Hilligans.Item;

public class Item {

    public String name;
    public int id;

    public Item(String name) {
        this.name = name;
        Items.ITEMS.add(this);
        Items.HASHED_ITEMS.put(name,this);
        id = Items.getNextId();
    }


    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }
}
