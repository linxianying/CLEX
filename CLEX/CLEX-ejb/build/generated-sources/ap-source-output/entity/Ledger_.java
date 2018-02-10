package entity;

import entity.Student;
import entity.Transaction;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(Ledger.class)
public class Ledger_ { 

    public static volatile SingularAttribute<Ledger, Long> studentId;
    public static volatile SingularAttribute<Ledger, Double> ascCost;
    public static volatile SingularAttribute<Ledger, Student> student;
    public static volatile SingularAttribute<Ledger, Long> id;
    public static volatile SingularAttribute<Ledger, Long> projGroupId;
    public static volatile SingularAttribute<Ledger, Long> transactionId;
    public static volatile SingularAttribute<Ledger, Transaction> transaction;

}