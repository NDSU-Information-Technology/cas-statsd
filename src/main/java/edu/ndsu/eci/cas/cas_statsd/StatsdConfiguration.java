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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for communication to StatsD.
 *
 */
@ConfigurationProperties(prefix="ndsu.statsd")
public class StatsdConfiguration {

  /** prefix to append to all metrics */
  private String prefix;
  /** statsd udp port */
  private int port = 8125;
  /** statsd hostname or ip */
  private String host = "127.0.0.1";

  /**
   * Get the metrics prefix
   * @return prefix to append
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Set the metric prefix.
   * This is appended to the metric name to help put it in the right place.
   * @param prefix prefix to use
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
    if (!this.prefix.endsWith(".")) {
      this.prefix += ".";
    }
  }

  /**
   * StatsD UDP port
   * @return port
   */
  public int getPort() {
    return port;
  }

  /**
   * StatsD UDP Port
   * @param port port
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * StatsD hostname or IP
   * @return host
   */
  public String getHost() {
    return host;
  }

  /**
   * StatsD hostname or IP
   * @param host valid hostname or IP
   */
  public void setHost(String host) {
    this.host = host;
  }
  
}
