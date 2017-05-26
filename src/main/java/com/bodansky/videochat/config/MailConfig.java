package com.bodansky.videochat.config;

/*
 * Created by Adam Bodansky on 2017.04.05..
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
public class MailConfig {


    private static final String JAVA_MAIL_FILE = "classpath:mail/javamail.properties";
    private static final Logger log = LoggerFactory.getLogger(MailConfig.class);
    private static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    private final ApplicationContext applicationContext;
    private final Environment environment;

    @Autowired
    public MailConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        try {

            // Basic mail sender configuration, based on emailconfig.properties
            mailSender.setHost(this.environment.getProperty("mail.server.host"));
            mailSender.setPort(Integer.parseInt(this.environment.getProperty("mail.server.port")));
            mailSender.setProtocol(this.environment.getProperty("mail.server.protocol"));
            mailSender.setUsername(this.environment.getProperty("mail.server.username"));
            mailSender.setPassword(this.environment.getProperty("mail.server.password"));

            // JavaMail-specific mail sender configuration, based on javamail.properties
            final Properties javaMailProperties = new Properties();
            javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
            mailSender.setJavaMailProperties(javaMailProperties);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        log.info("MailConfig: JavaMailSender is created. ");
        return mailSender;
    }

    @Bean
    public MimeMessage mimeMessage() {
        return mailSender().createMimeMessage();
    }

    @Bean
    public MimeMessageHelper mimeMessageHelper() {
        MimeMessage mimeMessage = mailSender().createMimeMessage();
        //mimeMessage.setContent(html, "text/html; charset=utf-8");
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(this.environment.getProperty("mail.server.email"));
            mimeMessageHelper.setSubject(this.environment.getProperty("email.subject"));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return mimeMessageHelper;
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(2); //Sets a new order for the template engine in the chain.
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        return templateResolver;
    }
}