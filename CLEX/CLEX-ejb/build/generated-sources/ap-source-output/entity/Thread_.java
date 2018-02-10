package entity;

import entity.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Thread.class)
public class Thread_ { 

    public static volatile SingularAttribute<Thread, String> dateTime;
    public static volatile SingularAttribute<Thread, Long> id;
    public static volatile SingularAttribute<Thread, String> title;
    public static volatile SingularAttribute<Thread, Integer> downVote;
    public static volatile SingularAttribute<Thread, User> user;
    public static volatile SingularAttribute<Thread, String> content;
    public static volatile SingularAttribute<Thread, String> username;
    public static volatile SingularAttribute<Thread, Integer> upVote;

}