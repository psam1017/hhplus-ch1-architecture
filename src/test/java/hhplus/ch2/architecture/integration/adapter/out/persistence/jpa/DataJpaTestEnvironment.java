package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public abstract class DataJpaTestEnvironment {

    @Autowired
    EntityManager em;

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
