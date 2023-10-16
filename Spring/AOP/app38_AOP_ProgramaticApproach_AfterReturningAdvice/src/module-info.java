/**
 * 
 */
/**
 * @author tmi
 *
 */
module app38_AOP_ProgramaticApproach_AfterReturningAdvice {
	requires commons.logging;
	requires spring.aop;
	exports com.get_tt_right.business;
	opens com.get_tt_right.business to spring.core;
}