package com.hello.Manager;

import java.util.Iterator;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hello.entity.Employee;

public class ManagerWithAnnotation {
	private static SessionFactory factory;

	public static void main(String[] args) throws IllegalStateException, SystemException {

		factory = new Configuration().configure().buildSessionFactory();

		ManageEmployee mEmployee = new ManageEmployee();
		Integer id1 = mEmployee.addEmployee("a", "A", 10000);
		Integer id2 = mEmployee.addEmployee("b", "B", 13000);
		Integer id3 = mEmployee.addEmployee("c", "C", 12000);

		mEmployee.listEmployees();
		mEmployee.update(id1, 5000);
		mEmployee.delete(id2);
		mEmployee.listEmployees();

	}

	private void delete(Integer id2) throws IllegalStateException, SystemException {
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

	private void update(Integer id1, int i) throws IllegalStateException, SystemException {
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

	private Integer addEmployee(String string, String string2, int i) throws IllegalStateException, SystemException {
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
			List emList = (List) session.createQuery("from Employee").list();
			for (Iterator iterator = (emList).iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print("first name  " + employee.getFirstName());
				System.out.print("last name  " + employee.getLastName());
				System.out.println("salary  " + employee.getSalary());
			}
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

}
