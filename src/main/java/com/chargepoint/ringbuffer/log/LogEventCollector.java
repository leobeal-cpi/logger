package com.chargepoint.ringbuffer.log;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;

public class LogEventCollector {
    static ThreadLocal<Context> LOG_COLLECTOR = ThreadLocal.withInitial(Context::new);

    public static void clean() {
      LOG_COLLECTOR.get().clean();
    }

    static class Context {
      boolean had_error = false;
      List<LogEvent> events = new ArrayList<>();

      public void clean() {
        had_error = false;
        events = new ArrayList<>();
      }
    }

    public static void markError() {
      LOG_COLLECTOR.get().had_error = true;
    }

    public static boolean hadError() {
      return LOG_COLLECTOR.get().had_error;
    }

    public static void collect(LogEvent event) {
      LOG_COLLECTOR.get().events.add(event);
    }

    public static List<LogEvent> events() {
      List<LogEvent> ret = LOG_COLLECTOR.get().events;
      LOG_COLLECTOR.get().events = new ArrayList<LogEvent>();
      return ret;
    }

  }