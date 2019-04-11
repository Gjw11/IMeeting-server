package com.IMeeting.resposirity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by gjw on 2019/4/9.
 */
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T,
        I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * 接到factory之后，把factory扔了spring data jpa
     */
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new BaseRepositoryFactory(em);
    }

    /**
     * //创建一个内部类，该类不用在外部访问
     * 他的作用是将我们的baseReposity的实现类扔给factorybean
     *
     * @param <T>
     * @param <I>
     */

    private static class BaseRepositoryFactory<T, I extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager em;

        public BaseRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        /**
         * 通过这两个方法来确定具体的实现类，也就是Spring Data Jpa具体实例化一个接口的时候会去创建的实现类。
         */
        //设置具体的实现类是BaseRepositoryImpl
//        @Override
//        public final JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information) {
//            return new BaseRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em);
//        }

        //设置具体的实现类的class
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}
