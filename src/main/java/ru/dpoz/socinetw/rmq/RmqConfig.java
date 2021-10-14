package ru.dpoz.socinetw.rmq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RmqConfig
{
    public final static String queueName = "cache-feed";
    @Value("${spring.rabbitmq.host}")
    String rmqHost;
    @Value("${spring.rabbitmq.virtual-host}")
    String rmqVHost;
    @Value("${spring.rabbitmq.username}")
    String rmqUser;
    @Value("${spring.rabbitmq.password}")
    String rmqPass;

    /** Настройка Очереди */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /** Настройка Топика */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("cache-exchange");
    }

    /** Настройка связи между топком и очередью */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    /** Настройка подключения */
    @Bean ConnectionFactory connectionFactory()
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rmqHost);
        connectionFactory.setUsername(rmqUser);
        connectionFactory.setPassword(rmqPass);
        connectionFactory.setVirtualHost(rmqVHost);
        return connectionFactory;
    }

    /** Настройка контейнера слушателя */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
    {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /** Настройка получателя */
    @Bean
    RmqReceiver receiver()
    {
        return new RmqReceiver();
    }

    /** Настройка коллбека метода-обработчика получателя {@link ru.dpoz.socinetw.rmq.RmqReceiver#receiveMessage(ru.dpoz.socinetw.rmq.RmqEventMessage)} */
    @Bean
    MessageListenerAdapter listenerAdapter(RmqReceiver receiver)
    {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

}
