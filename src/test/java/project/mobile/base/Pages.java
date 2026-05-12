package project.mobile.base;

import project.mobile.pages.welcome.AccessSendNotificationsPage;
import project.mobile.pages.welcome.WelcomePage;
import project.mobile.pages.dashboard.DashboardPage;
import project.mobile.pages.login.*;
import project.mobile.pages.welcome.*;

public class Pages {
    public LoginPage loginPage() {
        return new LoginPage();
    }

    public WelcomePage welcomePage() {
        return new WelcomePage();
    }

    public AccessSendNotificationsPage accessSendNotificationsPage() {
        return new AccessSendNotificationsPage();
    }

    public TermsAndConditionsPage termsAndConditionsPage() {
        return new TermsAndConditionsPage();
    }

    public DashboardPage dashboardPage() {return new DashboardPage();}
}
