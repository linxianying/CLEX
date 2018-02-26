/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.awt.Component;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javaClass.IcsReader;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import jdk.nashorn.internal.runtime.ParserException;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;

/**
 *
 * @author lin
 */
@Stateless
public class ScheduleBean implements ScheduleBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    

    @Override
    public boolean loadIcsFile() {
        IcsReader ics = new IcsReader();
        ics.loadIcs();
        return false;
    }
}
