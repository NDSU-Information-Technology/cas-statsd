/*  Copyright 2016 North Dakota State University 
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.ndsu.eci.cas.cas_statsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apereo.cas.support.events.audit.CasAuditActionContextRecordedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Service for sending audit actions as StatsD counters
 *
 */
@Service
@EnableConfigurationProperties(StatsdConfiguration.class)
public class StatsdService {

  /** configuration */
  private StatsdConfiguration config;

  /** pattern to find mfa actions */
  private Pattern mfaPattern = Pattern.compile("signedDuoResponse|mfa-duo");
  
  /** statsd host */
  private InetAddress host;
  
  /** udp socket to statsd */
  private DatagramSocket sock;
  
  /**
   * Constructs the service
   * @param config configuration to use
   * @throws UnknownHostException upon bad hostname
   * @throws SocketException upon bad socket
   */
  @Autowired
  public StatsdService(StatsdConfiguration config) throws UnknownHostException, SocketException {
    this.config = config;
    host = InetAddress.getByName(config.getHost());
    sock = new DatagramSocket();
  }
  
  /**
   * Listener to audit events to trigger counter
   * @param event audit event
   */
  @EventListener
  public void handleCasAuditActionContextRecordedEvent(final CasAuditActionContextRecordedEvent event) {    
    String message = config.getPrefix() + event.getAuditActionContext().getActionPerformed().toLowerCase();
    
    Matcher matcher = mfaPattern.matcher(event.getAuditActionContext().getResourceOperatedUpon());

    if (matcher.find()) {
      message += "_" + "duo";      
    }
    
    message += ":1|c";

    byte[] bdata = message.getBytes();
    try {
      sock.send(new DatagramPacket(bdata, bdata.length, host, config.getPort()));
    } catch (IOException ignored) {
      // Ignore
    }
    
  }

}
