package com.chatter.flagservice;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.chatter.flagservice.handler.ChatterFlagServiceRequestHandler;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ChatterFlagServiceRequestHandlerTest {

    private static Object input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = null;
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testChatterFlagServiceRequestHandler() {
        ChatterFlagServiceRequestHandler handler = new ChatterFlagServiceRequestHandler();
        Context ctx = createContext();

        Object output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}