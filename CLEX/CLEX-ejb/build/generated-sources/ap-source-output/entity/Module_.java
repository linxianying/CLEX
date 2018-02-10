package entity;

import entity.Course;
import entity.Lecturer;
import entity.Lesson;
import entity.SuperGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Module.class)
public class Module_ { 

    public static volatile SingularAttribute<Module, SuperGroup> supergroup;
    public static volatile SingularAttribute<Module, String> moduleCode;
    public static volatile SingularAttribute<Module, String> takenYear;
    public static volatile SingularAttribute<Module, String> prerequisite;
    public static volatile SingularAttribute<Module, String> takenSem;
    public static volatile SingularAttribute<Module, Integer> workload;
    public static volatile SetAttribute<Module, Lecturer> lecturers;
    public static volatile SingularAttribute<Module, Long> superGroupId;
    public static volatile SingularAttribute<Module, String> preclusions;
    public static volatile SingularAttribute<Module, Integer> modularCredits;
    public static volatile SingularAttribute<Module, Course> course;
    public static volatile SingularAttribute<Module, Long> id;
    public static volatile CollectionAttribute<Module, Lesson> lessons;

}