package xyz.linyh.messageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import xyz.linyh.messageservice.ws.MessageEndpoint;

@SpringBootApplication
public class MessageServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MessageServiceApplication.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("-------------------------------------------------");
        MessageEndpoint messageEndpoint = (MessageEndpoint) run.getBean("messageEndpoint");
        System.out.println(messageEndpoint.messageService);

    }

}
