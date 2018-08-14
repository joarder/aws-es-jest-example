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

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

class AWSESActions {
    private static final String SERVICE_NAME = "es";
    private static final boolean REQUEST_SENT_RETRY_ENABLED = true;
    private static final int RETRY_COUNT = 3;

    static JestClient jestClientBuilder() {
        AWS4Signer signer = new AWS4Signer();

        signer.setServiceName(SERVICE_NAME);
        signer.setRegionName(Args.awsRegion);

        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
        HttpRequestInterceptor requestInterceptor = new AWSSignerInterceptor(SERVICE_NAME, signer, credentialsProvider);

        final JestClientFactory factory = new JestClientFactory() {
            @Override
            protected HttpClientBuilder configureHttpClient(final HttpClientBuilder builder) {
                builder.addInterceptorLast(requestInterceptor);
                builder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, REQUEST_SENT_RETRY_ENABLED));
                return builder;
            }

            @Override
            protected HttpAsyncClientBuilder configureHttpClient(final HttpAsyncClientBuilder builder) {
                builder.addInterceptorLast(requestInterceptor);
                return builder;
            }

        };

        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(Args.domainEndpoint)
                .multiThreaded(true)
                .build());

        JestClient jestClient = null;

        try {
            jestClient = factory.getObject();
        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while initializaing the JEST client.\n");
            e.printStackTrace();
        }

        return jestClient;
    }

    /**
     * Creates an index
     * @param jestClient
     * @throws Exception
     */
    public static void createIndex(final JestClient jestClient) throws Exception {
        AWSESJestExample.LOG.info("Creating index \""+Args.indexName+"\" ...");

        JestResult result;

        try {
            result = jestClient.execute(new CreateIndex.Builder(Args.indexName).build());

            if(!isValidResult(result))
                System.exit(result.getResponseCode());

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while creating the index.\n");
            e.printStackTrace();
        }
    }

    /**
     * Index some documents into Elasticsearch
     * @param jestClient
     * @throws Exception
     */
    public static void indexData(JestClient jestClient) throws Exception {
        // Blocking indexing
        final Note note1 = new Note("User1", "Note1: do u see this - "
                + System.currentTimeMillis());
        Index index = new Index.Builder(note1).index(Args.indexName).type(Args.typeName).build();

        AWSESJestExample.LOG.info("Inserting a single document ...\n" + note1);

        JestResult result;

        try {
            result = jestClient.execute(index);

            if(!isValidResult(result))
                System.exit(result.getResponseCode());

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while indexing a single document.\n");
            e.printStackTrace();
        }

        // Asynch indexing
        final Note note2 = new Note("User2", "Note2: do u see this - "
                + System.currentTimeMillis());
        index = new Index.Builder(note2).index(Args.indexName).type(Args.typeName).build();

        AWSESJestExample.LOG.info("Inserting a single document asynchronously ...\n" + note2);


        try {
            jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
                public void failed(Exception ex) { }

                public void completed(JestResult result) {
                    note2.setId((String) result.getValue("_id"));
                }
            });

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while indexing a single document asynchronously.\n");
            e.printStackTrace();
        }


        // Bulk indexing
        final Note note3 = new Note("User3", "Note3: do u see this - "
                + System.currentTimeMillis());
        final Note note4 = new Note("User4", "Note4: do u see this - "
                + System.currentTimeMillis());

        Bulk bulk = new Bulk.Builder()
                .addAction(new Index.Builder(note3).index(Args.indexName)
                        .type(Args.typeName).build())
                .addAction(new Index.Builder(note4).index(Args.indexName)
                        .type(Args.typeName).build()).build();

        AWSESJestExample.LOG.info("Inserting two documents using bulk API ...\n" + note3 +"\n"+ note4);

        try {
            result = jestClient.execute(bulk);
            Thread.sleep(2000);

            if(!isValidResult(result))
                System.exit(result.getResponseCode());

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while bulk indexing two documents.\n");
            e.printStackTrace();
        }
    }

    /**
     * Query or Search within an index
     * @param jestClient
     * @throws Exception
     */
    public static void queryIndex(JestClient jestClient) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("note", "see"));

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(Args.indexName).addType(Args.typeName).build();

        AWSESJestExample.LOG.info("Querying index \""+Args.indexName+"\" for 'note' and 'see' ...");
        AWSESJestExample.LOG.info(searchSourceBuilder.toString());

        JestResult result;

        try {
            result = jestClient.execute(search);

            if(!isValidResult(result))
                System.exit(result.getResponseCode());
            else {
                List<Note> notes = result.getSourceAsObjectList(Note.class);
                for (Note note : notes)
                    System.out.println(note);
            }

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while searching the indexed documents.\n");
            e.printStackTrace();
        }
    }

    /**
     * Deletes an index
     * @param jestClient
     * @throws Exception
     */
    public static void deleteIndex(final JestClient jestClient) throws Exception {
        AWSESJestExample.LOG.info("Deleting index \""+Args.indexName+"\" ...");

        DeleteIndex deleteIndex = new DeleteIndex.Builder(Args.indexName).build();
        JestResult result;

        try {
            result = jestClient.execute(deleteIndex);

            if(!isValidResult(result))
                System.exit(result.getResponseCode());

        } catch (Exception e) {
            AWSESJestExample.LOG.error("Caught an exception while deleting the index.\n");
            e.printStackTrace();
        }
    }

    /**
     * Validates the HTTP response coming back from Elasticsearch
     * @param result
     * @return
     * @throws Exception
     */
    private static boolean isValidResult(JestResult result) throws Exception {
        AWSESJestExample.LOG.debug("Response from Elasticsearch ...");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if(!result.isSucceeded()) {
            AWSESJestExample.LOG.debug(gson.toJson(result.getErrorMessage()));
            return false;
        } else {
            AWSESJestExample.LOG.debug(gson.toJson(result.getJsonObject()));
            return true;
        }
    }
}
