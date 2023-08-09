package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;


@ApplicationScoped
public class HibernateUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			if(sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
				
			return sessionFactory;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao iniciar a conexao do Hibernate.");
		}
	}
	
	public static SessionFactory getSessionFactoty() {
		
		return sessionFactory;
	}
	
	public static Session getCurrentSession() {
		
		return getSessionFactoty().getCurrentSession();
	}
	public static Session openSession() {
		if(sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory.openSession();
	}
	
	public static Connection getCurrentConnectionHibernate() throws SQLException{
		
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}
	
	public static Connection getConnectionFromTomCat() throws SQLException, NamingException{
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource)context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		return ds.getConnection();
	}
	
	public static DataSource getDataSourceJndi() throws NamingException{
		InitialContext context = new InitialContext();
		return (DataSource)context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}
}
