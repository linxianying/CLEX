package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Grade.class)
public class Grade_ { 

    public static volatile SingularAttribute<Grade, String> studentId;
    public static volatile SingularAttribute<Grade, String> moduleCode;
    public static volatile SingularAttribute<Grade, String> takenYear;
    public static volatile SingularAttribute<Grade, String> takenSem;
    public static volatile SingularAttribute<Grade, Long> id;
    public static volatile SingularAttribute<Grade, String> moduleGrade;

}