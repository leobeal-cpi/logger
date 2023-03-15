package com.chargepoint.ringbuffer.controller;


import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  private static final Logger logger = LogManager.getLogger();

  @GetMapping("/")
  public String index(@RequestParam("error") Optional<String> error) {

    logger.debug("this is debug");
    logger.info("this is info");
    logger.warn("this is warn");

    if (error.isPresent()) {
      logger.error("this is error");
    }

    //logger.error("this is error");

    return "At least it's working.";
  }
}
