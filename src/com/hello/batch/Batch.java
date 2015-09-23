package com.hello.batch;

import javax.transaction.SystemException;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import com.hello.entity.Employee;

class Batch {
	// private static SessionFactory sessionFactory=new
	// AnnotationConfiguration().addPackage("com.hello.entity").addAnnotatedClass(Employee.class).buildSessionFactory();
	private static SessionFactory sessionFactory;

	public static void main(String[] args) throws IllegalStateException,
			SystemException {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		// insert(10000);
		// insertWithBatch(10000);
		Session session = sessionFactory.openSession();
		// Query query=session.createQuery("delete from Employee where id =10");
		// query.executeUpdate();
	//	Transaction transaction = session.beginTransaction();
		Employee employee = new Employee("test", "test", 1000);
		//session.save(employee);
		
		Employee employee1 = (Employee) session
				.createQuery("from Employee where firstName='test'").list()
				.get(0);
		employee1.getId();
		Employee e = (Employee)session.load(Employee.class, employee1.getId());
		session.delete(e);
		//transaction.commit();

	}

	private static void insertWithBatch(int times)
			throws IllegalStateException, SystemException {
		Long start = System.nanoTime();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = (Transaction) session.beginTransaction();
			for (int j = 0; j < times; j++) {
				Employee employee = new Employee("test", "test", 1000);
				session.save(employee);
				if (j % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Long end = System.nanoTime();
		System.out.println("cost time " + (end - start));
	}

	private static void insert(int times) throws IllegalStateException,
			SystemException {
		Long start = System.nanoTime();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = (Transaction) session.beginTransaction();
			for (int j = 0; j < times; j++) {
				Employee employee = new Employee("test", "test", 1000);
				session.save(employee);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Long end = System.nanoTime();
		System.out.println("cost time " + (end - start));
	}

}