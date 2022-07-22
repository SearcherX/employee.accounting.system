package system;

import system.console.CompanyConsoleApp;
import system.structure.Organization;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        OrganizationSynchronizer organizationSynchronizer = new OrganizationSynchronizer("org.json");
        organizationSynchronizer.setDaemon(true);
        Organization organization = organizationSynchronizer.getOrganization();
        organizationSynchronizer.start();
        CompanyConsoleApp app = new CompanyConsoleApp(organization);

        app.start();

        organizationSynchronizer.interrupt();
        organizationSynchronizer.join();

    }

}
