/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import jdk.nashorn.internal.runtime.ParserException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
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
        //loadIcs();
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
    
    public DefaultScheduleEvent addEvents(String path){
        DefaultScheduleEvent event = null;
        try {
            FileInputStream fin = new FileInputStream("test.ics");
            //FileInputStream fin = new FileInputStream(path);
            //System.out.println(getFileContent(fin));
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);
            
            for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
                Component component = (Component) i.next();
                //new event
                Date start = SDF.parse(component.getProperty("DTSTART").getValue());
                Date end = SDF.parse(component.getProperty("DTEND").getValue());
                String summary = component.getProperty("SUMMARY").getValue();

                event =  new DefaultScheduleEvent(summary, start, end);

                System.out.println("added( start: "+start+"end: "+ end + " summary:" + summary+")");
                

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return event;
    }
    
    public String getFileContent( FileInputStream fis ) throws IOException {
    StringBuilder sb = new StringBuilder();
    Reader r;  
        try {
            r = new InputStreamReader(fis, "UTF-8"); //or whatever encoding
            int ch = r.read();
            while(ch >= 0) {
                sb.append(ch);
                ch = r.read();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IcsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return sb.toString();
}

}
