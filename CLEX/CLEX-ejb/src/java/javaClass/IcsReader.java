/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import jdk.nashorn.internal.runtime.ParserException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;

/**
 *
 * @author lin
 */
public class IcsReader {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    public void main(String[] args){
        loadIcs();
    }
    
    @PostConstruct
    public void loadIcs() {
        DefaultScheduleModel eventModel = new DefaultScheduleModel();
        CalendarBuilder builder = new CalendarBuilder();

        try {
            net.fortuna.ical4j.model.Calendar calendar = builder.build(this.getClass().getResourceAsStream("test.ics"));

            for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
                Component component = (Component) i.next();
                //new event
                Date start = SDF.parse(component.getProperty("DTSTART").getValue());
                Date end = SDF.parse(component.getProperty("DTEND").getValue());
                String summary = component.getProperty("SUMMARY").getValue();

                eventModel.addEvent(new DefaultScheduleEvent(summary, start, end));

                System.out.println("added( start: "+start+"end: "+ end + " summary:" + summary+")");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    

}
