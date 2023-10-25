/**
 * @author tmi
 *
 */
module app40_AOP_ProgrammaticApproach_ThrowsAdvice {
	requires spring.aop;
	requires commons.logging;
	exports com.get_tt_right.business;
	exports com.get_tt_right.service;
	opens com.get_tt_right.business to spring.core;
	}