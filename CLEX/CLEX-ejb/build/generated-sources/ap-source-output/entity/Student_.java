package entity;

import entity.Grade;
import entity.Module;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Student.class)
public class Student_ { 

    public static volatile SingularAttribute<Student, String> matricYear;
    public static volatile SingularAttribute<Student, Double> cap;
    public static volatile SingularAttribute<Student, String> major;
    public static volatile SingularAttribute<Student, String> matricSem;
    public static volatile SingularAttribute<Student, Long> id;
    public static volatile CollectionAttribute<Student, Grade> grades;
    public static volatile SingularAttribute<Student, String> currentYear;
    public static volatile SetAttribute<Student, Module> modules;
    public static volatile SingularAttribute<Student, String> username;
    public static volatile SingularAttribute<Student, String> faculty;

}