package com.mfun.javaconfigdemo.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author: JMing
 * @date: 2019/11/18 14:59
 * @description: 数据库配置
 */

@Configuration
@EnableTransactionManagement // 启用事务注解
@MapperScan(basePackages={"com.mfun.javaconfigdemo.dao"}, // 自动扫描Mapper
        sqlSessionFactoryRef="sqlSessionFactoryBean")
public class JdbcConfig {
    @Value("${jdbc.driver:}")
    private String driver;

    @Value("${jdbc.url:}")
    private String dbUrl;

    @Value("${jdbc.username:}")
    private String username;

    @Value("${jdbc.password:}")
    private String password;

    @Value("${c3p0.checkoutTimeout:0}")
    private int checkoutTimeout;

    @Value("${c3p0.pool.initSize:3}")
    private int poolInitSize;

    @Value("${c3p0.pool.minSize:3}")
    private int poolMinSize;

    @Value("${c3p0.pool.maxSize:15}")
    private int poolMaxSize;

    @Value("${c3p0.acquireIncrement:3}")
    private int acquireIncrement;

    @Value("${c3p0.maxStatements:0}")
    private int maxStatements;

    @Value("${c3p0.maxStatementsPerConnect:0}")
    private int maxStatementsPerConnect;

    @Value("${c3p0.maxIdleTime:0}")
    private int maxIdleTime;

    @Value("${c3p0.idleConnectionTestPeriod:0}")
    private int idleConnectionTestPeriod;

    @Value("${c3p0.testConnectionOnCheckin:false}")
    private boolean testConnectionOnCheckin;

    /**
     * 配置数据源连接池，此处使用c3p0
     */
    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setJdbcUrl(dbUrl);
            cpds.setDriverClass(driver);
            cpds.setUser(username);
            cpds.setPassword(password);

            // 其他关键配置
            poolConfig(cpds);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        return cpds;
    }

    private void poolConfig(ComboPooledDataSource cpds) {
        // 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出
        // SQLException，如设为0则无限期等待。单位毫秒，默认为0
        cpds.setCheckoutTimeout(checkoutTimeout);

        // 初始化时获取的连接数，取值应在minPoolSize与maxPoolSize之间。Default: 3
        cpds.setInitialPoolSize(poolInitSize);

        // 连接池中保留的最小连接数
        cpds.setMinPoolSize(poolMinSize);

        // 连接池中保留的最大连接数
        cpds.setMaxPoolSize(poolMaxSize);

        // 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数
        cpds.setAcquireIncrement(acquireIncrement);

        // 控制数据源内加载的PreparedStatements数量。如果maxStatements
        // 与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0
        cpds.setMaxStatements(maxStatements);

        // 定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0
        cpds.setMaxStatementsPerConnection(maxStatementsPerConnect);

        // 最大空闲时间, 空闲时间内未使用则连接被丢弃。若为0则永不丢弃，单位：秒
        cpds.setMaxIdleTime(maxIdleTime);

        // 检查连接池中所有空闲连接的间隔时间，单位为秒
        cpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);

        // 如果设为 true 那么在归还连接时将校验连接的有效性。 Default: false
        // 异步操作，应用端不需要等待测试结果，但会增加一定的数据库调用开销
        cpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
    }

    /**
     * 配置Mybatis工厂
     * @param dataSource
     */
    @Bean(name = "sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean;
    }

    /**
     * 配置事务管理器
     * @param dataSource
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
