package com.get_tt_right.business;

/*NOTES
 -------
 +. We are making use of some initial values to amt and accNo attributes(You can treat them as existing values inside DB).
 +. deposit business method taking two inputs from the user: amt to deposit and the accNo on which the amt is being deposited.
 +. Before depositing to accNo, we are checking if inputed accNo from user matches existing accNo.
    - If true: deposit into that accNo.
    - If false: we have the option to return 0, but usually this should not be the case as we need to handle that and return some meaningful information to the user. Therefore;
      => We need to create exception package in which we are going to maintain Exception classes.
      => For this deposit method's else part we need to throw AccountNumberNotFoundException and therefore:
         * Create AccountNumberNotFoundException class.
         * Override toString() method-Inside overriden toString method just maintain one simple print statement.
 +. For this business class; I need to implement Logging Service. I have two options:
    - Mix Service logic with business logic-Do not use this approach!!
    - Separate Service from business and use AOP to combine the two at runtime.-Be using this!! Stay intelligent.
        Importance
        ----------
        1. Clear logic separation.
           => As we will also have capability to execute this service at our exact required points.
 */
public class Bank {

//	Associations||dependencies||attributes
	private int amt=5000;
	private String accNo="CBA123";
	
//	Setters-->N/A: I am not injecting||setting data from outside this class-->I have hard coded my required data.
//	getters -->N/A: To access this class data from outside this class.
	
//	Business method(s) -->Holds the functionality||logic
	public int deposit(int amt, String accNo){
		
        System.out.println();
        System.out.println("------------------deposit method executing-------------------------------");
		
//		Check if inputed accNo matches existing thus.accNo
		if (accNo.equals(this.accNo)) {
		
			/* If you don't know AOP, this is how you will mix up your code(Service + Business): Now that you know AOP; don;t try this at home or at school!! Haha; Stay intelligent champ!!
			 * Log l = LogFactory.getLog(Bank.class); l.
			 * info("Will execute first before deposit method logic||code||functionality execution"
			 * );
			 */
			
			System.out.println("In deposit method");
			
			this.amt+=amt;
			return this.amt;
		} else {
//			return 0;  // If accNo wrong we should not return zero balances; it should not allow-thats why we are throwing exception.
			throw new AccountNotFoundException();
		}
		
	}
	
	public void anotherMethod() {
		System.out.println();
        System.out.println("-----------------anotherMethod executing--------------------------------");
		System.out.println("I am executing after LogBefore Service has executed");
	}
}
