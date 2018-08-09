package model;

public class Department {

    private Long id;
    private String department;
    private String district;

    public Department(String department, String district){
        this.department = department;
        this.district = district;
    }

    @Override
    public String toString(){
        return "('"+department+"', '"+district+"')";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}
