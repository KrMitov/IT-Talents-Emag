package com.emag.controller;

import com.emag.exceptions.AuthenticationException;
import com.emag.exceptions.BadRequestException;
import com.emag.model.pojo.User;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";
    private static final int ADMIN_ID = 2;
    @Autowired
    private UserRepository userRepository;

    public User getLoggedUser(HttpSession session) {
        if (session.getAttribute(LOGGED_USER_ID) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        int userId = (int) session.getAttribute(LOGGED_USER_ID);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new BadRequestException("The user you are trying to log in with does not exist!");
        }
        return user;

    }

    public void adminVerification(HttpSession session){
        if (getLoggedUser(session).getRole().getId() != ADMIN_ID){
            throw new AuthenticationException("Action forbidden! Admin rights required!");
        }
    }

    public void loggedInVerification(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) != null){
            throw new BadRequestException("You are already logged in!");
        }
    }

    public void userVerification(HttpSession session, int id){
        User user = getLoggedUser(session);
        if (user.getRole().getId() != ADMIN_ID && id != user.getId()){
            throw new AuthenticationException("Cannot access or edit the profile information of someone else");
        }
    }

    public void loginUser(HttpSession session,int id){
        session.setAttribute(LOGGED_USER_ID,id);
    }

    public void logoutUser(HttpSession session){
        session.invalidate();
    }

}
