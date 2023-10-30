package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Ticket;
import com.get_tt_right.beans.TicketVendingMachine;
/*
 * TicketVendingMachineProxy class is the child class of TicketVendingMachine class.
 * */
public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
//		TicketVendingMachineProxy class object will be returned, and that is what we are upcasting into TicketVendingMachine abstract class reference.
		TicketVendingMachine ticketvendingmachine = (TicketVendingMachine) context.getBean("ticketvendingmachine");
		System.out.println(ticketvendingmachine.getClass().getCanonicalName());
		
		Ticket ticket = ticketvendingmachine.getTicket();
		ticket.printTicket();
		
		Ticket ticket1 = ticketvendingmachine.getTicket();
		ticket.printTicket();
		
//		Check if the two objects are singleton or prototype.
		System.out.println(ticket==ticket1);
	}

}
