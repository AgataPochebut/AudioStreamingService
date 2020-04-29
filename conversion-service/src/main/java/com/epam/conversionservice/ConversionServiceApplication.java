package com.epam.conversionservice;

import com.epam.conversionservice.service.ConversionService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;


@SpringBootApplication
public class ConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversionServiceApplication.class, args);
	}

//	@Bean
//	ConversionService bookingService(ConversionService conversionService) {
//		return conversionService;
//	}

	@Bean
	Queue queue() {
		return new ActiveMQQueue("testQueue");
	}

	@Bean
	JmsInvokerServiceExporter exporter(ConversionService implementation) {
		JmsInvokerServiceExporter exporter = new JmsInvokerServiceExporter();
		exporter.setServiceInterface(ConversionService.class);
		exporter.setService(implementation);
		return exporter;
	}

	@Bean
	SimpleMessageListenerContainer listener(@Qualifier("jmsConnectionFactory") ConnectionFactory factory, Queue queue, JmsInvokerServiceExporter exporter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(factory);
		container.setDestination(queue);
		container.setConcurrentConsumers(1);
		container.setMessageListener(exporter);
		return container;
	}
}
