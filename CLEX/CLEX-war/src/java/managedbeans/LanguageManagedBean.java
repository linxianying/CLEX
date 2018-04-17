/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;


@ManagedBean(name = "languageManagedBean")
@SessionScoped
public class LanguageManagedBean implements Serializable{
    
    private FacesContext ctx = FacesContext.getCurrentInstance();
    private String selectedLocale = "zh_CN";
    
    public LanguageManagedBean(){
        ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
    }
    public void selectedLocaleValueChangeListener(){
        switch (selectedLocale) {
            case "en_US":
                ctx.getViewRoot().setLocale(new Locale("en", "US"));
                break;
            case "en_SG":
                ctx.getViewRoot().setLocale(new Locale("en", "SG"));
                break;
            case "zh_CN":
                ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
                break;
        }
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
    
    public void setEnglish(){
        System.out.println("setEnglish");
        ctx.getViewRoot().setLocale(new Locale("en", "SG"));
        
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
    