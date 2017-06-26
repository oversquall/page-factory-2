package setting;

import cucumber.api.java.Before;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.lang.management.ManagementFactory;

import static java.lang.Runtime.getRuntime;

/**
 * Created by Liza on 07.06.17.
 */
public class JettySettings {
    
    public static final String WAR_PATH = "src/test/resources/test-web-app.war";
    public static final int PORT = 8181;
    
    private static boolean dunit = true;
    
    private Server server;
    
    @Before(order = 999)
    public void startJetty() throws Exception {
    
        if (dunit) {
            // stop jetty after all tests
            getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        
            // start jetty
            server = new Server(PORT);
        
            MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
            server.addBean(mbContainer);
        
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            File warFile = new File(JettySettings.WAR_PATH);
            webapp.setWar(warFile.getAbsolutePath());
        
            server.setHandler(webapp);
            server.start();
            dunit = false;
        }
    }

}
