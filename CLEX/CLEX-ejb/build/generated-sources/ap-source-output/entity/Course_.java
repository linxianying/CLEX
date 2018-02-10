package entity;

import entity.Module;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Course.class)
public class Course_ { 

    public static volatile SingularAttribute<Course, String> moduleCode;
    public static volatile SingularAttribute<Course, String> school;
    public static volatile SingularAttribute<Course, Boolean> discontinuedBool;
    public static volatile SingularAttribute<Course, String> moduleInfo;
    public static volatile SingularAttribute<Course, String> moduleName;
    public static volatile SingularAttribute<Course, String> discountinuedYear;
    public static volatile SingularAttribute<Course, Long> id;
    public static volatile SingularAttribute<Course, String> discountinuedSem;
    public static volatile CollectionAttribute<Course, Module> modules;
    public static volatile SingularAttribute<Course, String> offeredSem;

}