package com.ranbiraeir.attendanceregister;

public class Employee {
    private String id;
    private String name;
    private boolean isPresent;

    public Employee(String id, String name, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.isPresent = isPresent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isPresent=" + isPresent +
                '}';
    }
}
