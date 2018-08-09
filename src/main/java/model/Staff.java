package model;

public class Staff {

    private long id;
    private String name;
    private int age;

    public Staff(String name, int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString(){
        return "('"+name+"', "+age+")";
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
