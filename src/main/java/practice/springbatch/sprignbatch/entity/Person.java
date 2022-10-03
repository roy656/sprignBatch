package practice.springbatch.sprignbatch.entity;

import lombok.Getter;

@Getter
public class Person {

    private int id;
    private  String name;

    private  String sex;

    private  String age;

    public Person(int id, String name, String sex, String age) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }
}
