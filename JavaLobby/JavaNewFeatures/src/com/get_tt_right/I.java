package com.get_tt_right;
//Differences between interface and AC from jdk 1.8
// Copy this on notepad and save it in a location where you will cmd
// Use javac I.java to compile
// Use javap I  and javap AC  to decompile so that you can verify everything in this file
interface I{
 //1. variables always public static final
int a = 11;
//2. We can't write constructor in interface: Reason: Main reason for constructor is to initialise non-static data
//I(){}
// 3. We can't blocks/initialisers, both static and non-static, inside interface
//{}
//static{}

}
abstract class AC{
  private int a = 111;
  int b = 222;
  final int c = 333;
  static int d = 44;
  protected int e = 555;  

  AC(){} 
  {}
static{}
}

// 4. Interface does not talk about the state of an object. AC does bcs we can be able to write non-static content and of course we have a constructor. State of an objec is nothing but non-static data
// 5. Interface allows abstract methods and not concrete methods, If you need concrete methods then utilize default methods. In AC we can write both Abstract methods and Concrete methods. We cannot write default methods here.
// 6. We can write logic in interface through default methods. We cannot write default methods in AC's
// 7. Interface supports multiple inheritance. AC just like class doesn't
// 8. We cannot override java.lang.Object class methods in interface. We can override java.lang.Object methods in AC.