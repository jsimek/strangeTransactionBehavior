package h2comitAfterRollback;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

@Transactional
@Rollback(true)
public class TransactionTest {
	
	@Inject
	private DataSource ds;
	
	@Test
	public void methodA() throws Exception {
		Connection conn = ds.getConnection();
		conn.prepareStatement("insert into dummy(b) VALUES(55) ").executeUpdate();
	}
	
	
	@Test
	public void methodB() throws Exception {
		Connection conn = ds.getConnection();
		ResultSet rs = conn.prepareStatement("SELECT * FROM DUMMY").executeQuery();
		Assert.assertFalse("Table should be empty", rs.next());
		
	}

}
