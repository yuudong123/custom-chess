package com.yuudong123.chessgalltnmt.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Helper implements StringValues{

  public boolean isValidString(String input) {
    return input.matches(INPUT_REGEX);
  }

  public String getClientIp(HttpServletRequest request) {
    String clientIp = null;
    boolean isIpInHeader = false;

    ArrayList<String> headerList = new ArrayList<>();
    headerList.add("X-Forwarded-For");
    headerList.add("HTTP_CLIENT_IP");
    headerList.add("HTTP_X_FORWARDED_FOR");
    headerList.add("HTTP_X_FORWARDED");
    headerList.add("HTTP_FORWARDED_FOR");
    headerList.add("HTTP_FORWARDED");
    headerList.add("Proxy-Client-IP");
    headerList.add("WL-Proxy-Client-IP");
    headerList.add("HTTP_VIA");
    headerList.add("IPV6_ADR");

    for (String header : headerList) {
      clientIp = request.getHeader(header);
      if (StringUtils.hasText(clientIp) && !clientIp.equals("unknown")) {
        isIpInHeader = true;
        break;
      }
    }

    if (!isIpInHeader) {
      clientIp = request.getRemoteAddr();
    }
    return clientIp;
  }

  public <T> ArrayList<T> readJsonFile(String filePath, Type type) {
    ArrayList<T> dataList = new ArrayList<>();
    try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "UTF-8")) {
      Gson gson = new Gson();
      dataList = gson.fromJson(reader, type);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataList;
  }

  public <T> void writeJsonFile(String filePath, ArrayList<T> dataList) {
    try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(dataList, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendEmail(String to, String subject, String body) {
    // Gmail SMTP 서버 설정
    String host = "smtp.gmail.com";
    String port = "587";
    String username = "chessgalltnmt@gmail.com";
    String password = "pgvm coje nixq ykbk";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);

    Authenticator auth = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };

    Session session = Session.getInstance(props, auth);

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);
      System.out.println("메일 전송 성공");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
