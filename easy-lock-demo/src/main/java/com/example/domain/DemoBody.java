package com.example.domain;


/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/3 15:28
 */
public class DemoBody {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DemoBody{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
