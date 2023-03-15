package com.chargepoint.ringbuffer.log;

import java.io.Serializable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
    name = "RingBufferAppender",
    category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE)
public class RingBufferAppender extends AbstractAppender {


  protected RingBufferAppender(
      String name,
      Filter filter,
      Layout<? extends Serializable> layout,
      boolean ignoreExceptions,
      Property[] properties
  ) {
    super(name, filter, layout, ignoreExceptions, properties);
  }

  @Override
  public void append(LogEvent event) {
    if (Level.ERROR.isLessSpecificThan(event.getLevel())) {
      LogEventCollector.markError();
    }

    if (LogEventCollector.hadError()) {
      flush();
    }

    if (Level.DEBUG.isMoreSpecificThan(event.getLevel())) {
      LogEventCollector.collect(event);
    } else {
      write(event);
    }
  }

  private void flush() {
    for (LogEvent e : LogEventCollector.events()) {
      write(e);
    }
  }

  private void write(LogEvent event) {
    System.out.println("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage());
  }

  @PluginFactory
  public static RingBufferAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
      @PluginElement("Layout") Layout<? extends Serializable> layout,
      @PluginElement("Filter") final Filter filter,
      @PluginElement("Properties") Property[] properties
  ) {
    if (name == null) {
      LOGGER.error("No name provided for RingBufferAppender");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new RingBufferAppender(name, filter, layout, ignoreExceptions, properties);
  }

}
