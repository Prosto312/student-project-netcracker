package ua.netcrackerteam.configuration;

import org.apache.log4j.*;

/**
 *
 * @author krygin
 */
public class InterviewLoggerSingleton{
    private static InterviewLoggerSingleton instance = null;
    protected static final Logger log = Logger.getLogger(InterviewLoggerSingleton.class);
    private static final String infoMsg = "has successfully executed";

    private InterviewLoggerSingleton() {}

    public Logger getLog(){
        return this.log;
    }

    public static synchronized InterviewLoggerSingleton getInstance() {
        try {
            if(instance  == null){
                instance  = new InterviewLoggerSingleton();
                BasicConfigurator.configure();
                log.setLevel(Level.ALL);
            }
        } catch (Exception e) {
            log.error("Something failed ", e);
        }
        return instance;
    }

    /*public void info() {
        Throwable t = new Throwable();
        StackTraceElement trace[] = t.getStackTrace();
        if (trace.length > 1)
        {
            StackTraceElement element = trace[1];
            String usedClass = element.getClassName();
            String usedMethod = element.getMethodName();
            log.info("[" + usedClass + "],[" + usedMethod + "] " + infoMsg);
        }
    }*/
}