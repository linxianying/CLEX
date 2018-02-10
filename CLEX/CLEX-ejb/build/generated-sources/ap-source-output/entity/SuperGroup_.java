package entity;

import entity.Module;
import entity.ProjectGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-10T15:14:48")
@StaticMetamodel(SuperGroup.class)
public class SuperGroup_ { 

    public static volatile SingularAttribute<SuperGroup, String> moduleCode;
    public static volatile SingularAttribute<SuperGroup, Integer> numOfGroups;
    public static volatile SingularAttribute<SuperGroup, Module> module;
    public static volatile CollectionAttribute<SuperGroup, ProjectGroup> projectGroups;
    public static volatile SingularAttribute<SuperGroup, Integer> maxStudentNum;
    public static volatile SingularAttribute<SuperGroup, Long> id;
    public static volatile SingularAttribute<SuperGroup, Integer> minStudentNum;

}