package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Service.EmployeeService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            employeeService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot_password_form";

    }

    public void sendEmail(String email, String resetPasswordLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Employee emp = employeeService.findByEmail(email);
        String username = emp.getUsername();

        helper.setFrom("gramabmalina@gmail.com");
        helper.setTo(email);

        String subject = "Reset Password";

        String content = "<h1>Hello!</h1>" +
                        "<p style=\"font-size: medium\">" +
                        " You are receiving this email because you requested your password to be changed. <br>" +
                        "Your username is: " + username +". <br>" +
                        "To change your password, please click on the following link." +
                        "</p>" +
                        "<div class=\"container text-center\">" +
                            "<a href=\"" + resetPasswordLink + "\" style=\"font-size: medium\">" +
                                "Change Password" +
                            "</a>" +
                        "</div>" +
                        "<p style=\"font-size: medium\">" +
                           "If you have not made this request, please ignore this e-mail." +
                        "</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Employee employee = employeeService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (employee == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Employee employee = employeeService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (employee == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            employeeService.updatePassword(employee, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }
}
