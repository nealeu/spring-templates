package org.opencredo.batch.modules.batch;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencredo.batch.modules.domain.Order;
import org.opencredo.batch.modules.domain.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/batch-applicationContext.xml"})
public class SimpleBatchTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Job job;

    @Autowired
    JobLauncher jobLauncher;

    @PersistenceContext
    EntityManager entityManager;

    TransactionTemplate dataTxTemplate;

    int orderCount = 40;

    @Autowired
    public void setTransactionManager(JpaTransactionManager txManager) {
        this.dataTxTemplate = new TransactionTemplate(txManager);
    }

    @Before
    public void createTestData() {
        dataTxTemplate.execute(new TransactionCallback<Object>() {
            public Object doInTransaction(TransactionStatus status) {
                for (int i = 0; i < orderCount; i++) {
                    Order order = new Order();
                    entityManager.persist(order);
                }
                entityManager.flush();
                return null;
            }
        });
    }


    @Test
    public void jobShouldRunAndProcessCorrectNumberOfItems() throws Exception {
        assertEquals("Wrong count of orders at start", orderCount, (long) countOrdersWithStatus(OrderStatus.REQUESTED));
        JobParameters parameters = new JobParameters();
        jobLauncher.run(job, parameters);
        assertEquals("Wrong count of orders at end", 0, (long) countOrdersWithStatus(OrderStatus.REQUESTED));

    }

    public Long countOrdersWithStatus(OrderStatus status) {
        return (Long) entityManager.createQuery("select count(o) from Order as o where o.status = :status").setParameter("status", status).getSingleResult();
    }


}
