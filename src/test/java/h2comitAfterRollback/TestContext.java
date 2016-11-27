package h2comitAfterRollback;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



@Configuration
@EnableTransactionManagement
public class TestContext {
	
		
		@Value("classpath:DummyDatabase.sql")
		private Resource schemaScript;

		@Bean
		public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
		    final DataSourceInitializer initializer = new DataSourceInitializer();
		    initializer.setDataSource(dataSource);
		    initializer.setDatabasePopulator(databasePopulator());
		    return initializer;
		}
		

		private DatabasePopulator databasePopulator() {
		    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		    populator.addScript(schemaScript);
		    return populator;
		}
		
	
		@Bean
		protected HikariConfig hikariConfig() {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("org.h2.Driver");
			config.setJdbcUrl("jdbc:h2:~/test/strangeTransaction;TRACE_LEVEL_FILE=2;MV_STORE=FALSE;DB_CLOSE_DELAY=-1");
			config.setUsername("sa");
			config.setPassword("");
			config.setMinimumIdle(1);
			config.setInitializationFailFast(false);
			//config.setAutoCommit(false); // when uncomented gives a table locking timeout
			return config;
		}

		@Bean
		public DataSource dataSource() throws Exception {
			HikariConfig config = hikariConfig();
			HikariDataSource dataSource = new HikariDataSource(config);
			return dataSource;
		}
		
		@Bean
		public PlatformTransactionManager transactionManager() throws Exception {
			return new DataSourceTransactionManager(dataSource());
		}

}
