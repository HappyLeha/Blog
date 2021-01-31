package com.example.demo.service;

import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.ResetCode;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.ResourceAlreadyExistException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnknownServerException;
import com.example.demo.repository.CodeRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final CodeRepository codeRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${emailAddress}")
    private String from;

    @Value("${emailPassword}")
    private String password;

    @Value("${confirmSubject}")
    private String confirmSubject;

    @Value("${confirmMessage}")
    private String confirmMessage;

    @Value("${successConfirmSubject}")
    private String successConfirmSubject;

    @Value("${successConfirmMessage}")
    private String successConfirmMessage;

    @Value("${codeSubject}")
    private String codeSubject;

    @Value("${codeMessage}")
    private String codeMessage;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TokenRepository tokenRepository,
                           CodeRepository codeRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.codeRepository = codeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
        if (userRepository.existsByEmailAndEnabled(username,true)) {
            return userRepository.findByEmail(username);
        }
        else {
            log.info("User with email " + username + " doesn't exist.");
            throw new ResourceNotFoundException("User with this email doesn't exist.");
        }
    }

    @Override
    public void createUser(User user)  {
        VerificationToken token;

        deleteExpiryTokensAndCodes();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        token = new VerificationToken(user);
        tokenRepository.save(token);
        confirmMessage += token.getToken();
        sendEmail(confirmSubject, confirmMessage, user.getEmail());
        log.info("User " + user + " has been created.");
    }

    @Override
    public void confirmUser(String token) {
        Optional<VerificationToken> optionalToken =
                tokenRepository.findFirstByToken(token);

        deleteExpiryTokensAndCodes();
        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();

            verificationToken.getUser().setEnabled(true);
            tokenRepository.delete(verificationToken);
            sendEmail(successConfirmSubject, successConfirmMessage,
                    verificationToken.getUser().getEmail());
            log.info("User " + verificationToken.getUser() +
                    " has been confirmed.");
        }
        else {
            log.info("Token "+token+" wasn't found.");
            throw new ResourceNotFoundException("This token doesn't exist");
        }
    }

    @Override
    public void createCode(String email) {
        User user;
        ResetCode code;

        deleteExpiryTokensAndCodes();
        if (!userRepository.existsByEmailAndEnabled(email,true)) {
            log.info("User with email " + email + " doesn't exist.");
            throw new ResourceNotFoundException(
                    "User with this email doesn't exist.");
        }
        user = userRepository.findByEmail(email);
        code = user.getCode();
        if (code == null || !codeRepository.existsByCode(code.getCode())) {
            code = new ResetCode(user);
            codeRepository.save(code);
            user.setCode(code);
            System.out.println("1");
        }
        codeMessage+=" "+user.getCode().getCode();
        sendEmail(codeSubject, codeMessage, email);
        log.info("Code " + user.getCode() + " has been created.");
    }

    @Override
    public void confirmCode(NewPasswordDTO newPasswordDTO) {
        ResetCode code;

        deleteExpiryTokensAndCodes();
        if (!codeRepository.existsByCode(newPasswordDTO.getCode())) {
            log.info("Code "+newPasswordDTO.getCode()+" is wrong.");
            throw new ResourceNotFoundException("This code is wrong");
        }
        code = codeRepository.findByCode(newPasswordDTO.getCode());
        code.getUser().setPassword(bCryptPasswordEncoder.encode(
                                   newPasswordDTO.getNewPassword()));
        userRepository.save(code.getUser());
        codeRepository.delete(code);
        log.info("Password for user " + code.getUser() +
                " has been successed changed.");
    }

    public void deleteExpiryTokensAndCodes() {
        List<VerificationToken> tokens = tokenRepository.
                findAllByExpiryDateLessThan(new Date());
        List<User> users = new ArrayList<>();

        tokens.forEach(i->users.add(i.getUser()));
        tokenRepository.removeByExpiryDateLessThan(new Date());
        userRepository.deleteAll(users);
        codeRepository.removeByExpiryDateLessThan(new Date());
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        if (!userRepository.existsByEmailAndEnabled(email, true)) {
            log.info("User with email " + email + " doesn't exist.");
            throw new ResourceNotFoundException("User with this email doesn't exist.");
        }
        User user = userRepository.findByEmail(email);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        else {
            log.info("This password is wrong");
            throw new InvalidPasswordException();
        }
    }

    @Override
    public User getProfile(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public void updateProfile(Principal principal, UserDTO userDTO) {
        User user = userRepository.findByEmail(principal.getName());

        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistException();
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        userRepository.save(user);
        log.info("User " + user + " was updated.");
    }

    public void deleteProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        if (user.getCode() != null) {
            codeRepository.delete(user.getCode());
        }
        userRepository.delete(user);
        log.info("User "+user+" was deleted.");
    }

    private void sendEmail(String subject, String message, String to)  {
        Properties props;
        Session session;
        MimeMessage mimeMessage;

        try {
            props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            log.info("Email with text " + message + " was sent to address "+ to
                    + ".");
        }
        catch (MessagingException e) {
            log.error("Email with text " + message + "wasn't sent");
            throw new UnknownServerException();
        }
    }
}
