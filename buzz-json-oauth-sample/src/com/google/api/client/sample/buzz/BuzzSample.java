/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.sample.buzz;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.sample.buzz.model.BuzzActivity;
import com.google.api.client.sample.buzz.model.BuzzActivityFeed;
import com.google.api.client.sample.buzz.model.BuzzObject;
import com.google.api.client.sample.buzz.model.Debug;

import java.io.IOException;

/**
 * @author Yaniv Inbar
 */
public class BuzzSample {

  static final String APP_DESCRIPTION = "Buzz API Java Client Sample";

  public static void main(String[] args) {
    Debug.enableLogging();
    try {
      try {
        HttpTransport transport = setUpTransport();
        authorize(transport);
        showActivities(transport);
        BuzzActivity activity = insertActivity(transport);
        activity = updateActivity(transport, activity);
        deleteActivity(transport, activity);
        Auth.revoke();
      } catch (HttpResponseException e) {
        System.err.println(e.response.parseAsString());
        throw e;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      Auth.revoke();
      System.exit(1);
    }
  }

  private static HttpTransport setUpTransport() {
    HttpTransport transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
    headers.setApplicationName("Google-BuzzSample/1.0");
    transport.addParser(new JsonCParser());
    return transport;
  }

  private static void authorize(HttpTransport transport) throws Exception {
    Auth.authorize(transport);
  }

  private static void showActivities(HttpTransport transport)
      throws IOException {
    View.header("Show Buzz Activities");
    BuzzActivityFeed feed = BuzzActivityFeed.list(transport);
    View.display(feed);
  }

  private static BuzzActivity insertActivity(HttpTransport transport)
      throws IOException {
    View.header("Insert Buzz Activity");
    BuzzActivity activity = new BuzzActivity();
    activity.object = new BuzzObject();
    activity.object.content = "Posting using " + BuzzSample.APP_DESCRIPTION;
    BuzzActivity result = activity.post(transport);
    View.display(result);
    return result;
  }

  private static BuzzActivity updateActivity(
      HttpTransport transport, BuzzActivity activity) throws IOException {
    View.header("Update Buzz Activity");
    activity.object.content += " (http://goo.gl/rOyC)";
    BuzzActivity result = activity.update(transport);
    View.display(result);
    return result;
  }

  private static void deleteActivity(
      HttpTransport transport, BuzzActivity activity) throws IOException {
    View.header("Delete Buzz Activity");
    activity.delete(transport);
    System.out.println("Deleted.");
  }
}
