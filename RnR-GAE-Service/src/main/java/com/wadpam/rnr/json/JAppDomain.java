package com.wadpam.rnr.json;

import com.google.appengine.api.datastore.Email;
import com.wadpam.open.json.JBaseObject;

import javax.persistence.Basic;
import javax.persistence.Id;

/**
 * Json representation of domain properties.
 * @author mattiaslevin
 */
public class JAppDomain extends JBaseObject {

    /** The domain name */
    private String domain;

    /** Short description of the domain */
    private String description;

    /** The user name used in basic authentication */
    private String user;

    /** The password used in basic authentication */
    private String password;

    /** Analytics tracking code, e.g. Google Analytics */
    private String analyticsTrackingCode;

    /** Contact email that can be used for email send from GAE */
    private String email;

    /** Context specific app property */
    private String appArg1;

    /** Context specific app property */
    private String appArg2;


    // Setters and getters
    public String getAnalyticsTrackingCode() {
        return analyticsTrackingCode;
    }

    public void setAnalyticsTrackingCode(String analyticsTrackingCode) {
        this.analyticsTrackingCode = analyticsTrackingCode;
    }

    public String getAppArg1() {
        return appArg1;
    }

    public void setAppArg1(String appArg1) {
        this.appArg1 = appArg1;
    }

    public String getAppArg2() {
        return appArg2;
    }

    public void setAppArg2(String appArg2) {
        this.appArg2 = appArg2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
