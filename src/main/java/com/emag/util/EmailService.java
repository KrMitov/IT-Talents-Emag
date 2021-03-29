package com.emag.util;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmailService{

    private static final String EMAIL_SUBJECT = "One of your favourite items on Emag has been discounted! Check it out!";

    @Autowired
    private JavaMailSender emailSender;

    public void sendMessage(Product product) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("emagservicenew@gmail.com");
        message.setTo(getRecipients(product));
        message.setSubject(EMAIL_SUBJECT);
        message.setText(createMailText(product));
        emailSender.send(message);
        System.out.println("Message sent successfully");
    }

    private String[] getRecipients(Product product){
        List<User> users = product.getUsersLikedThisProduct();
        List<String> emails = new ArrayList<>();
        users.forEach(user -> emails.add(user.getEmail()));
        return emails.toArray(new String[0]);
    }

    private String createMailText(Product product){
        double price = product.getDiscountedPrice() != null ? product.getDiscountedPrice() : product.getRegularPrice();
        String text = product.getFullName() + "\nThe product has been discounted!\n" +
                "Don't waste more time! Go and grab this awesome offer right now!\n" +
                "New discounted price: " + price + "\nProduct description:\n" + product.getDescription();
        return text;
    }
}
