package com.IMeeting.resposirity;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gjw on 2019/4/9.
 */
//Spring Data JPA都是调用SimpleJpaRepository来创建实例
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    //用于操作数据库
    private final EntityManager em;

    //父类没有不带参数的构造方法，这里手动构造父类
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.em = entityManager;
    }

    //通过EntityManager来完成查询
    @Override
    public List<Object[]> listBySQL(String sql) {
        return em.createNativeQuery(sql).getResultList();
    }

    @Override
    public void add(Object entity) {
        em.persist(entity);
    }

    @Override
    public void update(Object entity) {
        em.merge(entity);
    }

    @Override
    public <T1> void delete(Class<T1> entityClass, Object entityid) {
        delete(entityClass, new Object[]{entityid});
    }

    @Override
    public <T1> void delete(Class<T1> entityClass, Object[] entityids) {
        for (Object id : entityids) {
            em.remove(em.getReference(entityClass, id));
        }
    }

    @Override
    public <T1> T1 find(Class<T1> entityClass, Object entityId) {
        return em.find(entityClass, entityId);
    }

}
