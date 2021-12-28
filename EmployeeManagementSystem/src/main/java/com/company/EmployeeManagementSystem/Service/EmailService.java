package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Model.Email;
import org.hibernate.sql.Template;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
//import java.lang.module.Configuration;
import freemarker.template.Configuration;
import java.util.Map;

@Service("emailService")
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    Configuration fmConfiguration;


    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


    public void sendMail(String to, String subject, String text, Email email) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("horvat.diana2000@gmail.com");
            mimeMessageHelper.setTo(to);

            email.setContent(getContentFromTemplate(email.getModel()));
            mimeMessageHelper.setText(email.getContent(), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplate(Map<String, Object> model){
        StringBuffer content = new StringBuffer();
        try{
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("registerEmailTemplate.html"), model));
        }catch (Exception e){
            e.printStackTrace();
        }

        return content.toString();
    }

}
