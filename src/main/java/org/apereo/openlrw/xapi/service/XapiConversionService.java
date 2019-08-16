package org.apereo.openlrw.xapi.service;

import org.apereo.openlrw.caliper.v1p1.Event;
import org.apereo.openlrw.xapi.Statement;

import java.net.URISyntaxException;


public interface XapiConversionService {

  Event fromXapi(Statement statement);

  Statement toXapi(Event event) throws URISyntaxException;

}