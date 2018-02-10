package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Transaction.class)
public class Transaction_ { 

    public static volatile SingularAttribute<Transaction, String> date;
    public static volatile SingularAttribute<Transaction, Double> cost;
    public static volatile SingularAttribute<Transaction, String> activity;
    public static volatile SingularAttribute<Transaction, Long> id;
    public static volatile SingularAttribute<Transaction, Long> projGroupId;

}