/**
 * 
 */
/**
 * @author tmi
 *
 */
module app37_AOP_ProgramaticApproach_MethodBeforeAdvice {
	requires commons.logging;
	requires spring.aop;
	exports com.get_tt_right.business;
	opens com.get_tt_right.business to spring.core;
}