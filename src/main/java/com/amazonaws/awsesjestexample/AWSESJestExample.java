/*
 * Copyright 2018 Joarder Kamal. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may
 * not use this file except in compliance with the License. A copy of the
 * License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.awsesjestexample;

import com.beust.jcommander.JCommander;
import io.searchbox.client.JestClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class AWSESJestExample {
    // Logger initialization
    public final static Logger LOG = LogManager.getLogger(AWSESJestExample.class);

    /**
     * Initializes the logger settings if debugging is enabled
     */
    static void init() {
        if(Args.debug) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            LoggerConfig lc = ctx.getConfiguration().getRootLogger();
            lc.setLevel(Level.DEBUG);
            ctx.updateLoggers();

            AWSESJestExample.LOG.debug(ctx.getRootLogger().getLevel());
        }
    }

    /**
     * Main function
     * @param argv
     */
    public static void main(String ... argv) {
        Args args = new Args();

        JCommander jCommander = JCommander.newBuilder().addObject(args).build();
        jCommander.parse(argv);

        // Show help
        if (Args.help) {
            jCommander.usage();
            return;

        } else {
            // Read the command line arguments
            JCommander.newBuilder().addObject(args).build().parse(argv);

            // Initialization
            init();

            // Show command line arguments if debug option is enabled
            Args.show();

            // Initialises and starts the example
            JestClient jestClient;

            try {
                jestClient = AWSESActions.jestClientBuilder();

                if(jestClient != null) {
                    AWSESActions.createIndex(jestClient);
                    AWSESActions.indexData(jestClient);
                    AWSESActions.queryIndex(jestClient);
                    AWSESActions.deleteIndex(jestClient);
                }
            } catch (Exception e) {
                AWSESJestExample.LOG.error("Caught an exception while initializaing the JEST client.\n");
                e.printStackTrace();
            }
        }

        AWSESJestExample.LOG.info("AWS ES JEST example testing is now completed.");
        System.exit(0);
    }
}
