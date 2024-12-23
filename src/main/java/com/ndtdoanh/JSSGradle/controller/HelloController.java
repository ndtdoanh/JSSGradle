package com.ndtdoanh.JSSGradle.controller;

import com.ndtdoanh.JSSGradle.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public String getHelloWorld() throws IdInvalidException {
    return "Hello World (Hỏi Dân IT & Eric)";
  }
}
