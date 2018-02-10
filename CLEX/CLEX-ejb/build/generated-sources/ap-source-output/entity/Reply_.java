package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Reply.class)
public class Reply_ { 

    public static volatile SingularAttribute<Reply, Long> threadId;
    public static volatile SingularAttribute<Reply, String> dateTime;
    public static volatile SingularAttribute<Reply, Long> id;
    public static volatile SingularAttribute<Reply, Integer> downVote;
    public static volatile SingularAttribute<Reply, String> content;
    public static volatile SingularAttribute<Reply, String> username;
    public static volatile SingularAttribute<Reply, Integer> upVote;

}