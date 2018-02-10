package entity;

import entity.Module;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Lesson.class)
public class Lesson_ { 

    public static volatile SingularAttribute<Lesson, String> venue;
    public static volatile SingularAttribute<Lesson, String> moduleCode;
    public static volatile SingularAttribute<Lesson, Module> module;
    public static volatile SingularAttribute<Lesson, Long> id;
    public static volatile SingularAttribute<Lesson, String> time;
    public static volatile SingularAttribute<Lesson, String> type;
    public static volatile SingularAttribute<Lesson, String> day;

}