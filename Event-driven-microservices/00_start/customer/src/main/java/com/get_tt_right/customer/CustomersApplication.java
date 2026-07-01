package com.get_tt_right.customer;

import com.get_tt_right.common.config.AxonConfig;
import com.get_tt_right.customer.command.interceptor.CustomerCmdInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
//@Import({AxonConfig.class})
@Import(AxonConfig.class) // Since we have just a single class that we are importing we can get rid of the curly braces.
public class CustomersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersApplication.class, args);
    }

    /** To this method, I am going to pass 2 input parameters, ApplicationContext and CommandGateway.
     * As of now, we are dispatching all the commands from the CustomerCommandController with the help of CommandGateway bean only. That's why we need to pass CommandGateway bean as input parameter to this method. On top of this method, we need to add/mention @Autowired annotation.
     * Now using this CommandGateway bean/object, I need to invoke a method which is registerDispatchInterceptor. To this method I need to pass the bean of my interceptor class. How? Using the ApplicationContext object, I can get the bean of my interceptor class. i.e., context.getBean(CustomerCommandInterceptor.class)
     * With this, we should be good to go. And like this, our Interceptor is going to be registered with the CommandGateway. Now, anytime if you try to dispatch a command with the help of CommandGateway, this interceptor logic is going to be executed.
     * */
    @Autowired
    public void registerCustomerCommandInterceptor(ApplicationContext context, CommandGateway commandGateway) {
        commandGateway.registerDispatchInterceptor(context.getBean(CustomerCmdInterceptor.class));
    }

    /** To this configure method, we are going to pass an input parameter of type EventProcessingConfigurer and the parameter name I am going to keep it as 'config'
     * Now, using this 'config' I am going to invoke a method which is registerListenerInvocationErrorHandler. To this method, first - we need to mention our processing group name which is "customer-group", followed by the 2nd input as a lambda expression that will have a definition on how we want to handle the errors if there is an Exception happened during the event processing.
     * The lambda expression we are defining here is going to take some input parameter with the name 'conf' - from this lambda expression I am going to invoke PropagatingErrorHandler.instance() method.
     * With this configuration, what we are trying to tell is - under my "customer-group" processing group, if any RTE happens in the course of an execution flow while events are being processed - the same RTE, I want to propagate as it is to the Command layer without making any changes. So, if you go to this PropagatingErrorHandler - if you try to read the doc on top of the class, you will see that it is a "Singleton ErrorHandler implementation that does not do anything." So, it is simply going to propagate whatever RTE that happened to the upper/top layers.
     * With this configuration right now, the errors will be propagated to the top layer but as a last step, we need to configure the Subscribing Event processor so that roll back also is going to happen automatically because everything is going to be processed using a single thread. To configure the Subscribing Event processor, we can go to the application.yml of customer ms. There, just under the axon parent property, we need to add the child property "eventhandling.processors.<processor name which is> "customer-group" followed by the child property "mode" to which we need to assign the subscribing event processor. That's why to mode we are assigning the value "subscribing" as the event processor.
     * So, yea - those were the only changes that we needed to make. Below is a summary of the changes that we have done:
     * 1. Inside the CustomerProjection class - by defining the @ProcessingGroup("customer-group") annotation.
     * 2. Defining the configure method inside the CustomersApplication - Here we have configured to propagate the Exceptions to the top layer.
     * 3. Finally, we configured subscribing event processor mode to the "customer-group".
     * Now, you can save the changes, do a build and try to invoke the update operation again. Check continuation to this demo documentation in the GatewayserverApplication.
     *
     * */
    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerListenerInvocationErrorHandler("customer-group",
                conf -> PropagatingErrorHandler.instance());
    }

}
