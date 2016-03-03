/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 27/10/15.
 */
public class StaticUrlData {
    String terms;
    String privacypolicy;
    String disclaimer;
    String aboutus;
    String contactus;
    String app_terms;
    String app_privacypolicy;
    String androidAppStoreUrl;
    String helloEditorEmail;
    String feedbackEmail;

    public String getFeedbackEmail() {
        return feedbackEmail;
    }

    public String getHelloEditorEmail() {
        return helloEditorEmail;
    }

    public String getAndroidAppStoreUrl() {
        return androidAppStoreUrl;
    }

    public String getContactusUrl() {
        return contactus;
    }

    public String getTermsAndConditionsUrl() {
        return app_terms;
    }

    public String getPrivacyPolicyUrl() {
        return app_privacypolicy;
    }

    public String getAboutUsUrl() {
        return aboutus;
    }

    public String getDisclaimerUrl() {
        return disclaimer;
    }
}
