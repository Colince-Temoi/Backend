/**
 * 
 */
/**
 * @author tmi
 *
 */
module app39_AOP_ProgramaticApproach_MethodInterceptor {
	requires spring.aop;
	requires commons.logging;
	exports com.get_tt_right.business;
	opens com.get_tt_right.business to spring.core;
}