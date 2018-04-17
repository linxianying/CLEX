/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.context.FacesContext;

/**
 *
 * @author lin
 */
@Named(value = "languageBean")
@SessionScoped
public class LanguageBean implements Serializable {

    /**
     * Creates a new instance of LanguageBean
     */
    
    private FacesContext ctx = FacesContext.getCurrentInstance();
    private String selectedLocale;
    
    
    public LanguageBean() {
        ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
    }
    
    public void selectedLocaleValueChangeListener(){
        if(selectedLocale.equals("en_US")) 
            ctx.getViewRoot().setLocale(new Locale("en", "US"));
        else if(selectedLocale.equals("en_SG")) 
            ctx.getViewRoot().setLocale(new Locale("en", "SG"));
        else if(selectedLocale.equals("zh_CN"))
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        System.out.println(selectedLocale);
    }
    
    public void selectedLocaleValue(String language){
        if(language.equals("English")) 
            ctx.getViewRoot().setLocale(new Locale("en", "US"));
        else if(language.equals("Singapore English")) 
            ctx.getViewRoot().setLocale(new Locale("en", "SG"));
        else if(language.equals("Chinese"))
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        System.out.println(language);
    }
    
    public void setChinese(){
        System.out.println("setChinese");
        ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        
    }
    public String getLocalizedCurrencyFormat(){
        return NumberFormat.getCurrencyInstance(ctx.getViewRoot().getLocale()).format(1000); 
    }
    public String getLocalizedDateFormat(){
        return DateFormat.getDateInstance(DateFormat.LONG, ctx.getViewRoot().getLocale()).format(new Date());
    }
    public String getSelectedLocale() {
        return selectedLocale;
    }
    public void setSelectedLocale(String selectedLocale) {
        this.selectedLocale = selectedLocale;
    }
}
