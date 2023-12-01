/**
 * 
 */
/**
 * @author tmi
 *
 */
module app41_AOP_XmlApproach {
	requires spring.aop;
	requires spring.beans;
	requires commons.logging;
	requires spring.context;
	exports com.get_tt_right.business;
	exports com.get_tt_right.services;
	opens com.get_tt_right.business to spring.core;
}