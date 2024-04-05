package com.get_tt_right;

import java.util.ArrayList;
import java.util.Iterator;

/*Method level generics
 - There are 7 syntaxes for this
 1. public ret_type method_name(ArrayList<Integer> al){}
    +. This method can accept ONLY! Integer AL reference. 
 2. public ret_type method_name(ArrayList<?> al){}
    +. This method can accept any AL reference.
 3. public ret_type method_name(ArrayList<? extends X> al){}  // given X is a class-Wrapper class|whatever custom user-defined class that you may have specified.
    +. Here ? stands for anything,it will be replaced by X type|object ArrayList or X child classes objects ArrayList.
    +. If X is Number type, then this method can accept only Number AL or Number child AL. This can also apply for you custom/user defined classes.
 4. public ret_type method_name(ArrayList<? extends X> al){}  // given X is an interface
    +. Here ? stands for anything,it will be replaced by X type or X implementation classes data.     
 5. public ret_type method_name(ArrayList<? super X> al){}  // given X is a class-Wrapper class|whatever custom user-defined class that you may have specified.
    +. Here ? stands for anything,it will be replaced by X type or X base||parent classes data.
    +. If X is Integer type, then this method can accept only Integer AL or Integer parent|base classe(s) AL references/returned AL data. This can also apply for you custom/user defined classes references.
 6. public ret_type method_name(ArrayList<? super X> al){}  // given X is an interface
    +. Here ? stands for anything,it will be replaced by X type(X implementation classes AL)| base class AL to X implementation classes .
 7. public ret_type method_name(ArrayList<Object> al){} 
    +. Only Object type it will allow
 * */

//1. public ret_type method_name(ArrayList al){}
class J{
	public ArrayList<Double> myMethod(ArrayList al) {
		return al;		
	}
}
//2. public ret_type method_name(ArrayList<Integer> al){}
class K{
	public ArrayList<?> myMethod(ArrayList<?> al) {
		return al;		
	}
}

interface MN extends X{
	void sayHello();
}

class M implements MN {
	private String eName;
	
	public void setEName(String eName) {
		this.eName=eName;
	}
	
	public String myEmpData() {
		return eName;
	}

	@Override
	public void sayHello() {
		System.out.println("Hello!");
		
	}
}
class N extends M {
	private Double sal;
	
	public void setSal(Double sal) {
		this.sal=sal;
	}
	
	public Double printSal() {
		return sal;
	}

	@Override
	public void sayHello() {
		System.out.println("Hello");
		
	}
}



/*Class level Generics
1. oNLY A particular type
2. Multiple types - In case of classes
3. Multiple types - In case of Interfaces
* 
* */

//interface I{
//	void sayHello();
//}
// class MyImpl implements I{
//
//	@Override
//	public void sayHello() {
//		System.out.println("Hello Buddy!!");
//	}
//	 
// }
// Only a particular type 
//class  A<T> {
//	private T t;
//	
//	public A(T t) {
//		this.t=t;
//	}
//	public T getValue() {
//		return t;
//	}
//}

//Multiple types - In case of classes => The below generic class can only accept Number or Number subclasses when invoking its constructor 
//class  B<T extends Number> {
//	private T t;
//	
//	public B(T t) {
//		this.t=t;
//	}
//	public T getValue() {
//		return t;
//	}
//}

//Multiple types - In case of classes => The below generic class can only accept B or B subclasses when invoking its constructor 
//class  C<T extends B> {
//	private T t;
//	
//	public C(T t) {
//		this.t=t;
//	}
//	public T getValue() {
//		return t;
//	}
//}

//Multiple types - In case of Interfaces => The below generic class can only accept I or I implementation classes when invoking its constructor 
//class  D<T extends I> {
//	private T t;
//	
//	public D(T t) {
//		this.t=t;
//	}
//	public T getValue() {
//		return t;
//	}
//}

interface X{
	
}

class XY {}

class XYZ extends XY implements X{}
	

public class Generics {
	
	public static void myTestMthd(ArrayList<? super MN> al) {
		System.out.println(al);
	}

	public static void main(String[] args) {
		ArrayList al = new ArrayList();
		al.add(10);
		al.add(20);
		al.add(30);
		al.add("Col");
		
		ArrayList<Double> alx = new ArrayList<Double>();
		alx.add(10.1);
		alx.add(20.3);
		alx.add(30.7);
		
//		Method level generics invokations
		J j = new J();
//		System.out.println(j.myMethod(al));
		
		K k = new K();
		System.out.println(k.myMethod(alx));
//		myTestMthd(alx);
		myTestMthd(al);
		
ArrayList<M> list = new ArrayList<>();
        
        // Creating objects of class M and adding them to the ArrayList
        M m1 = new M();
        m1.setEName("Alice");
        list.add(m1);
        
        M m2 = new M();
        m2.setEName("Bob");
        list.add(m2);
        
        M m3 = new M();
        m3.setEName("Charlie");
        list.add(m3);
        
//		myTestMthd(list);
		
		// Creating an ArrayList of objects of class N
        ArrayList<X> listX = new ArrayList<>();
        ArrayList<XY> listXY = new ArrayList<>();
        ArrayList<XYZ> listXYZ = new ArrayList<>();
        ArrayList<MN> listMN = new ArrayList<>();
        
        // Creating objects of class N and adding them to the ArrayList
//        N n1 = new N();
////        n1.setEName("Dave");
//        n1.setSal(5000.0);
//        listN.add(n1);
//        
//        N n2 = new N();
////        n2.setEName("Eva");
//        n2.setSal(6000.0);
//        listN.add(n2);
		
        myTestMthd(listMN);
		
		
		
//		A<Integer> a =new A<Integer>(30);
//		System.out.println(a.getValue());
		
//		A<String> a1 =new A<String>("Col");
//		System.out.println(a1.getValue());
		
//		B<Number> b =new B<Number>(30);
//		System.out.println(b.getValue());
//		B<Integer> b1 =new B<Integer>(10);
//		System.out.println(b1.getValue());
		
//		C<B> c =new C<B>(new B<Number>(30));
//		System.out.println(c.getValue());
//		
//		D<I> d =new D<I>(new MyImpl());
//		d.getValue().sayHello();
		
		
		
		
		
		
//		During generics introduction(1.5) - ClassCastException we can avoid by restricting the AL to only accept a particular type
//		ArrayList<Integer> al = new ArrayList();
//		al.add(10);
//		al.add(20);
//		al.add(30);
//		al.add("Col");
		
//		Iterating al using one Cursor object: Iterator
//		Iterator<Integer> i = al.iterator();
//		while (i.hasNext()) {
//			Integer integerElement = (Integer) i.next();
//			System.out.println(integerElement);
//		}
		
		
		
		
//		Before generics - ClassCastException we will experince at runtime like in this case below

		
//		Iterating al using one Cursor object: Iterator
//		Iterator i =  al.iterator();
//		while (i.hasNext()) {
////			Object object = (Object) i.next();
//			Integer object = (Integer) i.next();
//			System.out.println(object);
//			
//		}

	}

}
