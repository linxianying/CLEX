/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.util.AccessGrant;
import org.brickred.socialauth.util.SocialAuthUtil;

/**
 *
 * @author lin
 */
@Named(value = "userSession")
@RequestScoped
public class UserSessionBean implements Serializable {
    private SocialAuthManager manager;
    private String originalURL="http://localhost:8080/CLEX-war/landing.xhtml";
    private String providerID = "facebook";
    private Profile profile;
    private String FACEBOOK_APP_ID = "1000619583418683";
    private String FACEBOOK_APP_SECRET = "085e4cc470994fd7d86fe0c4609ddac3";
    
    public UserSessionBean() {
    }
    
    public void socialConnect() throws Exception {
        // Put your keys and secrets from the providers here 
        Properties props = System.getProperties();
        props.put("graph.facebook.com.consumer_key", FACEBOOK_APP_ID);
        props.put("graph.facebook.com.consumer_secret", FACEBOOK_APP_SECRET);
        // Define your custom permission if needed
        props.put("graph.facebook.com.custom_permissions", "email,public_profile,user_friends");
        
        // Initiate required components
        SocialAuthConfig config = SocialAuthConfig.getDefault();
        config.load(props);
        manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);
        
        // 'successURL' is the page you'll be redirected to on successful login
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String successURL = "https://localhost:8080"+externalContext.getRequestContextPath() + "/landing.xhtml"; 
        //System.out.println(successURL);
        //String authenticationURL = manager.getAuthenticationUrl(providerID, successURL);
        //System.out.println(authenticationURL);
    // Store in session
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("authManager", manager);                
        //redirect to the successful login page
        //FacesContext.getCurrentInstance().responseComplete();     
        //FacesContext.getCurrentInstance().getExternalContext().redirect(authenticationURL);
        
        //ExternalContext externalContext   = FacesContext.getCurrentInstance().getExternalContext();
        //String          successURL        = externalContext.getRequestContextPath() + "/landing.xhtml"; 
        String          authenticationURL = manager.getAuthenticationUrl(providerID, successURL);
        //VaadinSession.getCurrentRequest().getWrappedSession(); 
        FacesContext.getCurrentInstance().getExternalContext().redirect(authenticationURL);
    
        pullUserInfo();
    }   
    
    
    public void pullUserInfo() throws SocialAuthException {
        try {
            //HttpSession session = (HttpSession) request.getAttribute("jsessionid");
            ExternalContext    externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request         = (HttpServletRequest) externalContext.getRequest();
            Map                map             = SocialAuthUtil.getRequestParametersMap(request);
            
            
            //ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            //HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            
            // (String) request.getContentType());
            //Map map = SocialAuthUtil.getRequestParametersMap(request);
            //Map<String, String> paramsMap = SocialAuthUtil.getRequestParametersMap(request);
            //manager = (SocialAuthManager) request.getSession().getAttribute("authManager");
            //Enumeration<String> e = request.getParameterNames();
            //for (e = e; e.hasMoreElements();)
            //    System.out.println(e.nextElement());
            //AccessGrant a =  createAccessGrant(paramsMap);
            //System.out.println(request.getAttribute("Code"));
            if (this.manager != null) {
                System.out.println("map size"+map.size());
                //paramsMap.put("code", "400");
                AuthProvider provider = manager.connect(SocialAuthUtil.getRequestParametersMap(request));
                //manager.getProvider(providerID);
                //manager.getCurrentAuthProvider();
                //manager.connect(a);
                
                
                this.profile = provider.getUserProfile();
                System.out.println(profile.getFullName());
                FacesContext.getCurrentInstance().getExternalContext().redirect(originalURL);
            
            } else {
                System.out.println("this.manager=null");
                FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/landing.xhtml/");
        
            }
        } catch (Exception ex) {
            
            System.out.println("pullUserInfo: ");
            System.err.println(ex);
        } 
    }
    
    private AccessGrant createAccessGrant(Map<String, String> params){
	AccessGrant accessGrant= new AccessGrant();
	if(params.get("access_token") != null)
	{
		String accessToken = params.get("access_token");
		Integer expires = null;
		if (params.get("expires") != null) {
			expires = new Integer(params.get("expires"));
		}
		accessGrant.setKey(accessToken);
		accessGrant.setAttribute("expires", expires);
	}
	accessGrant.setProviderId(providerID);
	return accessGrant;
    }
    
    public void logOut() {
        try {
            // Disconnect from the provider
            manager.disconnectProvider(providerID);
            
            // Invalidate session
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            //this.invalidateSession(request);
            
            // Redirect to home page
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/landing.xhtml/");
        } catch (IOException ex) {
            System.out.println("logOut: " + ex.toString());
        }
    }
    
    // Getters and Setters

    public SocialAuthManager getManager() {
        return manager;
    }

    public void setManager(SocialAuthManager manager) {
        this.manager = manager;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getFACEBOOK_APP_ID() {
        return FACEBOOK_APP_ID;
    }

    public void setFACEBOOK_APP_ID(String FACEBOOK_APP_ID) {
        this.FACEBOOK_APP_ID = FACEBOOK_APP_ID;
    }

    public String getFACEBOOK_APP_SECRET() {
        return FACEBOOK_APP_SECRET;
    }

    public void setFACEBOOK_APP_SECRET(String FACEBOOK_APP_SECRET) {
        this.FACEBOOK_APP_SECRET = FACEBOOK_APP_SECRET;
    }
    
}