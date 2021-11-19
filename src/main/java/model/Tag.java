package model;

import model.interfaces.Entity;

public class Tag implements Entity {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        setId(id);
        setName(name);
    }

    private Tag(){}

    @Override
    public String toString() {
        return "Tag [id: " + getId() +", name: " + getName() + "]";
    }

}
