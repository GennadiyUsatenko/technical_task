package model;

public class Employee {

    private Long id;
    private Long employee;
    private Long department;

    public Employee(Long employee, Long department){
        this.employee = employee;
        this.department = department;
    }

    @Override
    public String toString(){
        return "("+employee+", "+department+")";
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getEmployee() {
        return employee;
    }

    public void setEmployee(Long employee) {
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
