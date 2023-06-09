package com.chargepoint.ringbuffer.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
    name = "FingersCrossedAppender",
    category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE)
public class FingersCrossedAppender extends AbstractAppender {

  private final String[] appenderRefs;
  private final List<AppenderControl> appenders;
  private final Configuration config;

  protected FingersCrossedAppender(
      String[] appenderRefs,
      Configuration config,
      String name,
      Filter filter,
      boolean ignoreExceptions,
      Property[] properties) {
    super(name, filter, null, ignoreExceptions, properties);
    this.appenderRefs = appenderRefs;
    this.config = config;
    this.appenders = new ArrayList<>();
  }

  @Override
  public void start() {
    parseAppenders();
    super.start();
  }

  private void parseAppenders() {
    final Map<String, Appender> map = config.getAppenders();
    for (final String appenderString : appenderRefs) {
      AppenderRef appenderRef = AppenderRef.createAppenderRef(appenderString, null, null);
      final Appender appender = map.get(appenderRef.getRef());
      if (appender == null) {
        LOGGER.error("No appender named {} was configured", appenderRef);
        continue;
      }

      appenders.add(
          new AppenderControl(appender, appenderRef.getLevel(), appenderRef.getFilter())
      );
    }
  }

  @Override
  public void append(LogEvent event) {
    //If log level is error or higher
    if (Level.ERROR.isLessSpecificThan(event.getLevel())) {
      LogEventCollector.markError();
    }

    if (LogEventCollector.hadError()) {
      flush();
    }

    if (Level.DEBUG.isMoreSpecificThan(event.getLevel())) {
      LogEventCollector.collect(event);
      return;
    }

    write(event);
  }

  /**
   * Flushes the events collected
   */
  private void flush() {
    for (LogEvent e : LogEventCollector.events()) {
      write(e);
    }
  }

  /**
   * Writes the event to all appenders.
   */
  private void write(LogEvent event) {
    for (final AppenderControl appender : appenders) {
      appender.callAppender(event);
    }
  }

  @PluginFactory
  public static FingersCrossedAppender createAppender(
      @PluginAttribute("AppenderRef") final String appenderRefs,
      @PluginConfiguration final Configuration config,
      @PluginAttribute("name") String name,
      @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
      @PluginElement("Filter") final Filter filter,
      @PluginElement("Properties") Property[] properties
  ) {
    if (name == null) {
      LOGGER.error("No name provided for FingersCrossedAppender");
      return null;
    }

    return new FingersCrossedAppender(
        appenderRefs.split(","),
        config,
        name,
        filter,
        ignoreExceptions,
        properties);
  }
}

