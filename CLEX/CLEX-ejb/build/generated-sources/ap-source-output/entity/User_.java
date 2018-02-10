package entity;

import entity.Reply;
import entity.Thread;
import entity.Vote;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Long> contactNum;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> school;
    public static volatile CollectionAttribute<User, Reply> replys;
    public static volatile SingularAttribute<User, String> name;
    public static volatile CollectionAttribute<User, Thread> threads;
    public static volatile CollectionAttribute<User, Vote> votes;
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> userType;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}