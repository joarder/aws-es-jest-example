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

import com.beust.jcommander.Parameter;

/**
 * This class defines the required and optional command line parameters
 */

public class Args {
    // AWS Elasticsearch Service domain endpoint
    @Parameter(names={"--aws-es-endpoint", "-endpoint"}, required = true, description = "AWS Elasticsearch Service domain endpoint: <URL>")
    public static String domainEndpoint;
    @Parameter(names={"--aws-region", "-region"}, required = false, description = "AWS region: <String>")
    public static String awsRegion = "us-east-1";
    @Parameter(names={"--index-name", "-index"}, required = false, description = "Elasticsearch index name: <String>")
    public static String indexName = "diary";
    @Parameter(names={"--index-type-name", "-type"}, required = false, description = "Elasticsearch index type name: <String>")
    public static String typeName = "notes";

    // Others
    @Parameter(names={"--debug", "-d"}, description = "Enable debug logs: [true, false]")
    public static boolean debug = false;
    @Parameter(names={"--help", "-h"}, help = true, description = "Help")
    public static boolean help = false;

    /**
     * Prints the command line arguments if Logger debug option is enabled
     */
    public static void show() {
        AWSESJestExample.LOG.debug("Listing all of the command line parameters:"
                +" --aws-es-endpoint "+domainEndpoint
                +" --aws-region "+awsRegion
                +" --index-name "+awsRegion
                +" --index-type-name "+awsRegion
                +" --debug "+debug);
    }
}
