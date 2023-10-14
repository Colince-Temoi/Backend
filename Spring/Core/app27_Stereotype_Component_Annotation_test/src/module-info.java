/**
 * 
 */
/**
 * @author tmi
 *
 */
module app27_Stereotype_Component_Annotation_test {
	requires spring.context;
	requires spring.beans;
	exports com.get_tt_right.vehicles;
	exports com.get_tt_right.engines;
    opens com.get_tt_right.vehicles to spring.core;

}