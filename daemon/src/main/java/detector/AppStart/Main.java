package detector.AppStart;

import detector.AppConfig.AppConfig;
import detector.AppData.HarmlessPatternsDB;
import detector.AppData.KnownPatternsDB;
import detector.AppStart.Threads.AnalyzerThread;
import detector.AppStart.Threads.CommunicationThread;
import detector.AppStart.Threads.InterceptorThread;
import detector.AppStart.Threads.ProcessUpdaterThread;
import detector.LogModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {

    private static Thread interceptorThread =
            new InterceptorThread();
    private static Thread analyzerThread =
            new AnalyzerThread();
    private static Thread processInfoUpdaterThread =
            new ProcessUpdaterThread();
    private static Thread communicationModuleThread =
            new CommunicationThread();

    private static Thread.UncaughtExceptionHandler exceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e)
                {
                    LogModule.Err(e);
                }
            };


    public static void main(String[] args)
    {
        // Configure application
        AppConfig.getInstance().configure(args);

        // Load traffic-patterns database
        KnownPatternsDB.getInstance().loadDB();
        HarmlessPatternsDB.getInstance().loadDB();

        // Start interceptor lifecycle monitor
        interceptorThread.setUncaughtExceptionHandler(exceptionHandler);
        interceptorThread.start();

        // Start traffic analyzer
        analyzerThread.setUncaughtExceptionHandler(exceptionHandler);
        analyzerThread.start();

        // Start process info updater
        processInfoUpdaterThread.setUncaughtExceptionHandler(exceptionHandler);
        processInfoUpdaterThread.start();

        // Start server for interaction with daemon
        communicationModuleThread.setUncaughtExceptionHandler(exceptionHandler);
        communicationModuleThread.start();
    }

}
