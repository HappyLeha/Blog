package com.example.demo.service;

import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.entity.ResetCode;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.exception.ResourceAlreadyExistException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnknownServerException;
import com.example.demo.repository.CodeRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
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

    @PostConstruct
    public void init() {
        tokenRepository.deleteAllByExpiryDateIsLessThanEqual(new Date());
        codeRepository.deleteAllByExpiryDateIsLessThanEqual(new Date());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public void createUser(User user)  {
        VerificationToken token;

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        token = new VerificationToken(user);
        tokenRepository.save(token);
        confirmMessage += token.getToken();
        sendEmail(confirmSubject, confirmMessage, user.getEmail());
        //deleteToken(token);
        log.info("User "+user+" was created.");
    }

    @Override
    public void confirmUser(String token) {
        Optional<VerificationToken> optionalToken =
                tokenRepository.findFirstByToken(token);

        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();

            verificationToken.getUser().setEnabled(true);
            tokenRepository.delete(verificationToken);
            sendEmail(successConfirmSubject, successConfirmMessage,
                    verificationToken.getUser().getEmail());
            log.info("User "+verificationToken.getUser()+" was confirm.");
        }
        else {
            log.info("Token "+token+" wasn't found.");
            throw new ResourceNotFoundException("This token doesn't exist");
        }
    }

    public void createCode(String email) {
        User user;

        if (!userRepository.existsByEmailAndEnabled(email,true)) {
            log.info("User with email "+email+" doesn't exist.");
            throw new ResourceNotFoundException(
                    "User with this email doesn't exist.");
        }
        user = userRepository.findByEmail(email);
        if (user.getCode()==null) {
            ResetCode code = new ResetCode(user);
            codeRepository.save(code);
            user.setCode(code);
        }
        codeMessage+=" "+user.getCode().getCode();
        sendEmail(codeSubject, codeMessage, email);
        //deleteCode(user.getCode());
        log.info("Code "+user.getCode()+" was created.");
    }

    public void confirmCode(NewPasswordDTO newPasswordDTO) {
        ResetCode code;

        if (!codeRepository.existsByCode(newPasswordDTO.getCode())) {
            log.info("Code "+newPasswordDTO.getCode()+" is wrong.");
            throw new ResourceNotFoundException("This code is wrong");
        }
        code = codeRepository.findByCode(newPasswordDTO.getCode());
        code.getUser().setPassword(bCryptPasswordEncoder.encode(
                                   newPasswordDTO.getNewPassword()));
        userRepository.save(code.getUser());
        codeRepository.delete(code);
        log.info("Password for user "+code.getUser()+" has been successed changed.");
    }

    @Async
    public void deleteExpiryTokensAndCodes(int measure, int value) {
        Runnable runnable = this::init;
        ScheduledExecutorService localExecutor = Executors.
                newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);
        Calendar calendar = Calendar.getInstance();
        Date date;

        calendar.add(measure, value);
        date = calendar.getTime();
        scheduler.schedule(runnable, date);
    }
/*
    @Async
    public void deleteCode(ResetCode code) {
        Runnable runnable = ()->{
            if (codeRepository.existsByCode(code.getCode())) {
                codeRepository.delete(code);
                log.info("Code "+code+" hasn't been confirmed");
            }
        };
        ScheduledExecutorService localExecutor = Executors.
                newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);
        Calendar calendar = Calendar.getInstance();
        Date date;

        calendar.setTime(code.getCreatedAt());
        calendar.add(Calendar.MINUTE,15);
        date = calendar.getTime();
        scheduler.schedule(runnable, date);
    }*/

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
            log.info("Email with text "+message+" was sent to address "+to+".");
        }
        catch (MessagingException e) {
            log.error("Email with text "+message+"wasn't sent");
            throw new UnknownServerException();
        }
    }
}
