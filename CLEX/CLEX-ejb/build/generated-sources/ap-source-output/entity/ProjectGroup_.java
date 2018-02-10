package entity;

import entity.SuperGroup;
import entity.Transaction;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(ProjectGroup.class)
public class ProjectGroup_ { 

    public static volatile SingularAttribute<ProjectGroup, SuperGroup> superGroup;
    public static volatile SingularAttribute<ProjectGroup, List> listOfStudentId;
    public static volatile SingularAttribute<ProjectGroup, Double> cost;
    public static volatile SingularAttribute<ProjectGroup, Long> id;
    public static volatile CollectionAttribute<ProjectGroup, Transaction> transactions;
    public static volatile SingularAttribute<ProjectGroup, Long> superGroupId;

}