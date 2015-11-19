package ptr.web.lunch.dto;

import ptr.web.lunch.model.Test;

import java.io.Serializable;


public class TestDTO implements Serializable {

    private long id;
    private String name;

    public TestDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestDTO(Test testObject) {
        if(testObject != null){
            this.id = testObject.getId();
            this.name = testObject.getName();
        }
    }

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




    @Override
    public String toString() {
        return "TestDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
