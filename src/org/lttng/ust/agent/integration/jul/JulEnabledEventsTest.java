package org.lttng.ust.agent.integration.jul;

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.lttng.ust.agent.integration.common.EnabledEventsTest;
import org.lttng.ust.agent.jul.LttngLogHandler;
import org.lttng.ust.agent.utils.LttngSessionControl;
import org.lttng.ust.agent.utils.LttngSessionControl.Domain;

public class JulEnabledEventsTest extends EnabledEventsTest {

    private static final Domain DOMAIN = Domain.JUL;

    private Logger loggerA;
    private Logger loggerB;
    private Logger loggerC;
    private Logger loggerD;

    @BeforeClass
    public static void julClassSetup() {
        /* Skip tests if we can't find the JNI library or lttng-tools */
        try {
            LttngLogHandler testHandler = new LttngLogHandler();
            testHandler.close();
        } catch (SecurityException | IOException e) {
            assumeTrue(false);
        }

        boolean ret1 = LttngSessionControl.setupSession(null, DOMAIN);
        boolean ret2 = LttngSessionControl.stopSession(null);
        /* "lttng view" also tests that Babeltrace is installed and working */
        List<String> contents = LttngSessionControl.viewSession(null);
        boolean ret3 = LttngSessionControl.destroySession(null);
        assumeTrue(ret1 && ret2 && ret3);
        assumeTrue(contents.isEmpty());
    }

    @AfterClass
    public static void julClassCleanup() {
        LttngSessionControl.deleteAllTracee();
    }

    @Before
    public void julSetup() throws SecurityException, IOException {
        // TODO Wipe all existing LTTng sessions?

        loggerA = Logger.getLogger(EVENT_NAME_A);
        loggerB = Logger.getLogger(EVENT_NAME_B);
        loggerC = Logger.getLogger(EVENT_NAME_C);
        loggerD = Logger.getLogger(EVENT_NAME_D);

        loggerA.setLevel(Level.ALL);
        loggerB.setLevel(Level.ALL);
        loggerC.setLevel(Level.ALL);
        loggerD.setLevel(Level.ALL);

        handlerA = new LttngLogHandler();
        handlerB = new LttngLogHandler();
        handlerC = new LttngLogHandler();

        loggerA.addHandler((Handler) handlerA);
        loggerB.addHandler((Handler) handlerB);
        loggerC.addHandler((Handler) handlerC);
    }

    @After
    public void julTeardown() {
        loggerA.removeHandler((Handler) handlerA);
        loggerB.removeHandler((Handler) handlerB);
        loggerC.removeHandler((Handler) handlerC);

        loggerA = null;
        loggerB = null;
        loggerC = null;
        loggerD = null;
    }

    @Override
    protected Domain getDomain() {
        return DOMAIN;
    }

    @Override
    protected void sendEventsToLoggers() {
        send10Events(loggerA);
        send10Events(loggerB);
        send10Events(loggerC);
        send10Events(loggerD);
    }

    static void send10Events(Logger logger) {
        String a = new String("a");
        Object[] params = { a, new String("b"), new Object() };

        // Levels are FINE, FINER, FINEST, INFO, SEVERE, WARNING
        logger.fine("A fine level message");
        logger.finer("A finer level message");
        logger.finest("A finest level message");
        logger.info("A info level message");
        logger.severe("A severe level message");
        logger.warning("A warning level message");
        logger.warning("Another warning level message");
        logger.log(Level.WARNING, "A warning message using Logger.log()");
        logger.log(Level.INFO, "A message with one parameter", a);
        logger.log(Level.INFO, "A message with parameters", params);
    }
}
