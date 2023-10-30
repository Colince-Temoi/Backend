package com.get_tt_right.beans;

/*LookUp method DI
 * We need to inject Ticket class instance into this class using Lookup method DI concept.
 * See Spring configuration file for more about this concept.
 * */
public abstract class TicketVendingMachine {

//	Properties||Associations||Dependencies - NA
	
//	Setter methods - NA
	
//	Getter methods - NA
	
//	Abstract method || lookup method -- Lookup for what? Ans. Implementation
	public abstract Ticket getTicket();
}
