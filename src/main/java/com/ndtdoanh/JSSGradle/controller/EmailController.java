package com.ndtdoanh.JSSGradle.controller;

import com.ndtdoanh.JSSGradle.service.EmailService;
import com.ndtdoanh.JSSGradle.service.SubscriberService;
import com.ndtdoanh.JSSGradle.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

  private final EmailService emailService;
  private final SubscriberService subscriberService;

  public EmailController(EmailService emailService, SubscriberService subscriberService) {

    this.emailService = emailService;
    this.subscriberService = subscriberService;
  }

  @GetMapping("/email")
  @ApiMessage("Send simple email")
  public String sendSimpleEmail() {
    //        this.emailService.sendSimpleEmail();
    //        this.emailService.sendEmailSync("ndtdk2@gmail.com", "test send email",
    //                "<h1> <b> hello </b> </h1>", false, true);
    //    this.emailService.sendEmailFromTemplateSync("ndtdk2@gmail.com", "test send email", "job");
    this.subscriberService.sendSubscribersEmailJobs();
    return "ok";
  }
}
