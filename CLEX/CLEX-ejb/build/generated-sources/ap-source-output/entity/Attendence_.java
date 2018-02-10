package entity;

import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Attendence.class)
public class Attendence_ { 

    public static volatile SingularAttribute<Attendence, Long> studentId;
    public static volatile SingularAttribute<Attendence, String> moduleCode;
    public static volatile SingularAttribute<Attendence, List> attendStatus;
    public static volatile SingularAttribute<Attendence, Long> id;

}