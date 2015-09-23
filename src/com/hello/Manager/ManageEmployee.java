package com.hello.Manager;

import java.util.Iterator;

import javax.management.Query;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.hello.entity.Employee;

public class ManageEmployee {
	private static SessionFactory factory;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IllegalStateException, SystemException {

		factory = new AnnotationConfiguration().configure().addPackage("com.hello.entity").addAnnotatedClass(Employee.class).buildSessionFactory();

		ManageEmployee mEmployee = new ManageEmployee();
//		Integer id1 = mEmployee.addEmployee("a", "A", 10000);
//		Integer id2 = mEmployee.addEmployee("b", "B", 13000);
//		Integer id3 = mEmployee.addEmployee("c", "C", 12000);

//		mEmployee.listEmployees();
//		mEmployee.update(id1, 5000);
//		mEmployee.delete(id2);
		System.out.println("============");
		mEmployee.listEmployees();
		System.out.println("============");
		mEmployee.listEmployeesByName("a");
		System.out.println("============");
		mEmployee.listEmployeesByPage(2, 8);
		System.out.println("=======sum====="+mEmployee.sum());
		
	}

	void delete(Integer id2) throws IllegalStateException, SystemException {
		Session session =factory.openSession();
		org.hibernate.Transaction tx=null;
		try {
			tx= session.beginTransaction();
			Employee employee=(Employee) session.get(Employee.class, id2);
			session.delete(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

	void update(Integer id1, int i) throws IllegalStateException, SystemException {
		Session session = factory.openSession();
		org.hibernate.Transaction tx = null;
		try {
			tx =  session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, id1);
			employee.setSalary(i);
			session.update(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

	Integer addEmployee(String string, String string2, int i) throws IllegalStateException, SystemException {
		Session session = factory.openSession();
		org.hibernate.Transaction tx = null;
		Integer idInteger = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(string, string2, i);
			idInteger = (Integer) session.save(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
		return idInteger;
	}

	void listEmployees() throws IllegalStateException, SystemException {
		Session session = factory.openSession();
		org.hibernate.Transaction tx = null;
		try {
			tx =session.beginTransaction();
			List emList = (List) session.createQuery("from Employee AS E order by E.salary desc").list();
			for (Iterator iterator = (emList).iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print(" id  " + employee.getId());
				System.out.print(" first name  " + employee.getFirstName());
				System.out.print(" last name  " + employee.getLastName());
				System.out.println(" salary  " + employee.getSalary());
			}
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	void listEmployeesByName(String name){
		Session session=factory.openSession();
		org.hibernate.Transaction tx=null;
		try {
			tx=session.beginTransaction();
			org.hibernate.Query query= session.createQuery("FROM Employee as E WHERE firstName=:name");
			query.setParameter("name",name);
			List emList=query.list();
			for (Iterator iterator = emList.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print(" id  " + employee.getId());
				System.out.print(" first name  " + employee.getFirstName());
				System.out.print(" last name  " + employee.getLastName());
				System.out.println(" salary  " + employee.getSalary());
			}
			tx.commit();
		} catch (Exception e) {
			if (tx!=null) {
				tx.rollback();
			}
		}finally{
			session.close();
		}
	}
	void listEmployeesByPage(int start,int end){
		Session session=factory.openSession();
		org.hibernate.Transaction tx=null;
		try {
			tx=session.beginTransaction();
//			org.hibernate.Query query= session.createQuery("FROM Employee");
//			query.setFirstResult(start);
//			query.setMaxResults(end);
			Criteria cr=session.createCriteria(Employee.class);
			cr.setFirstResult(start);
			cr.setMaxResults(end);
			cr.addOrder(Order.desc("salary"));
			List emList=cr.list();
			for (Iterator iterator = emList.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print(" id  " + employee.getId());
				System.out.print(" first name  " + employee.getFirstName());
				System.out.print(" last name  " + employee.getLastName());
				System.out.println(" salary  " + employee.getSalary());
			}
			
			tx.commit();
		} catch (Exception e) {
			if (tx!=null) {
				tx.rollback();
			}
		}finally{
			session.close();
		}
	}
	String  sum(){
		Session session=factory.openSession();
	try {
		Criteria criteria=session.createCriteria(Employee.class);
		criteria.setProjection(Projections.sum("salary"));
		List l=criteria.list();
		System.out.println(l.get(0)+" "+l.size());
		return l.get(0).toString();//返回integer为什么为空
	} catch (Exception e) {
	}finally{
		session.close();
	}
	return null;
}
}
