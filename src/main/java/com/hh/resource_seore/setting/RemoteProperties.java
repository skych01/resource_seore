package com.hh.resource_seore.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration
@ConfigurationProperties(prefix = "environment", ignoreUnknownFields = false)
@PropertySource("classpath:config/fileConfig.properties")
//@Component
public class RemoteProperties {
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  public static void main(String[] args) {
    Calendar calendar = new GregorianCalendar();
    System.out.println(    calendar.get(Calendar.YEAR));
    System.out.println(    calendar.get(Calendar.MONTH)+1);
    System.out.println(    calendar.get(Calendar.DATE));


  }
}