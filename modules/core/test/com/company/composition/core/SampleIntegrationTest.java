package com.company.composition.core;

import com.company.composition.CompositionTestContainer;
import com.company.composition.entity.Child;
import com.company.composition.entity.Parent;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

public class SampleIntegrationTest {

    @RegisterExtension
    public static CompositionTestContainer cont = CompositionTestContainer.Common.INSTANCE;

    private static Metadata metadata;
    private static Persistence persistence;
    private static DataManager dataManager;

    @BeforeAll
    public static void beforeAll() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
    }

    @AfterAll
    public static void afterAll() throws Exception {
    }

    @Test
    public void testComposition() {
        Parent parent = dataManager.create(Parent.class);
        parent.setName("parent");

        Child child1 = dataManager.create(Child.class);
        Child child2 = dataManager.create(Child.class);

        child1.setName("child-1");
        child1.setParent(parent);

        child2.setName("child-2");
        child2.setParent(parent);

        parent.setChilds(List.of(child1, child2));

        dataManager.commit(parent, child1, child2);

        Parent reloadParent = dataManager.load(Id.of(parent))
                .view("parent-test-view")
                .optional().orElse(null);

        Assertions.assertNotNull(reloadParent);
        Assertions.assertEquals(parent.getName(), reloadParent.getName());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(parent.getChilds(), reloadParent.getChilds()));

    }

    @Test
    @Ignore("it's not purpose of this project")
    public void testLoadUser() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<User> query = em.createQuery(
                    "select u from sec$User u where u.login = :userLogin", User.class);
            query.setParameter("userLogin", "admin");
            List<User> users = query.getResultList();
            tx.commit();
            Assertions.assertEquals(1, users.size());
        }
    }
}