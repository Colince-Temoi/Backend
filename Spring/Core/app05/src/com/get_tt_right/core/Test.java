package com.get_tt_right.core;

class ThreadScope extends ThreadLocal<String>{
	@Override
	protected String initialValue() {
		return "No data in this scope";
	}
}

class A{
/*Accessing thread Scope data can only be accessed by Thread1.
 *Thread2 cannot access Thread1 scope data, but can access data from ThreadScope class.
 *If you have not provided initialValue method then, NULL value you will get as output 
  when Thread2 try to access Thread1 scope data.
 * */
	void m1() {
		
		System.out.println("M1(): Thread1 Scope: "+Thread1.threadScope.get());
		System.out.println("M1(): Thread2 Scope: "+Thread2.threadScope.get());
	}
/*Accessing thread Scope data can only be accessed by Thread2
 *Thread1 cannot access Thread2 scope data, but can access data from ThreadScope class.
 *If you have not provided initialValue method then, NULL value you will get as output 
  when Thread1 try to access Thread2 scope data.
 * */
	void m2() {
		System.out.println("M2(): Thread2 Scope: "+Thread2.threadScope.get());
		System.out.println("M2(): Thread1 Scope: "+Thread1.threadScope.get());
	}
}

class Thread1 extends Thread{
	static ThreadScope threadScope=new ThreadScope();
	private A a;
	public Thread1(A a) {
		this.a=a;
	}
	@Override
	public void run() {
/*Providing Thread1 scope data, that will only be accessed by Thread1 executions,Nothing but what
  Thread1 will be executing,
 *No other Thread whatsoever can access this data.
 *IMPORTANT!!-Providing thread scope data should occur before the execution of any code by a Thread.
 * */
		threadScope.set("AAA");
		a.m1();
	}
	
}

class Thread2 extends Thread{

	static ThreadScope threadScope=new ThreadScope();
	A a;
	public Thread2(A a) {
		this.a=a;
	}
	
	public void run() {
		threadScope.set("BBB");
		a.m2();
	}
	
}

public class Test {

	public static void main(String[] args) {
		A a=new A();
		
		Thread1 t1=new Thread1(a);
		Thread2 t2=new Thread2(a);
		
		t1.start();
		t2.start();

	}

}
/*Flow of execution.
 * Starts in main class. main class is executed by main thread.
 * class A object is created by calling its default constructor and we are storing the reference in "a"
 * Create thread classes [Thread1 && Thread2] objects by calling their parameterized constructors while
   passing "a" reference as argument.
 * Invoke start() method for both thread classes.
   - Internally run() method will be called and the flow of execution handed over to the respective
     user-defined threads to proceed execution.
   - Like this, three threads will be executing concurrently: Thread0, Thread1 and main.
 *IMPORTANT!!: Go back to MultiThreading topic in your notes and you will understand what is
             happening here clearly.
 * */
