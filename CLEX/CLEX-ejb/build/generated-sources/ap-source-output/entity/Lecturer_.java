package entity;

import entity.Module;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Lecturer.class)
public class Lecturer_ { 

    public static volatile SingularAttribute<Lecturer, Long> id;
    public static volatile SetAttribute<Lecturer, Module> modules;
    public static volatile SingularAttribute<Lecturer, String> username;
    public static volatile SingularAttribute<Lecturer, String> faculty;

}