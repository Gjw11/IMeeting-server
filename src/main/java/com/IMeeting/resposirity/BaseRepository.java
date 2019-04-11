package com.IMeeting.resposirity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gjw on 2019/4/9.
 */
@NoRepositoryBean //表示该接口不会创建这个接口的实例
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<Object[]> listBySQL(String sql);

    /**
     * 保存实体
     *
     * @param entity 实体id
     */
    public void add(Object entity);

    /**
     * 更新实体
     *
     * @param entity 实体id
     */
    public void update(Object entity);

    /**
     * 删除实体
     *
     * @param entityClass 实体类
     * @param entityid    实体id
     */
    public <T> void delete(Class<T> entityClass, Object entityid);

    /**
     * 删除实体
     *
     * @param entityClass 实体类
     * @param entityids   实体id数组
     */
    public <T> void delete(Class<T> entityClass, Object[] entityids);

    /**
     * 获取实体
     *
     * @param <T>
     * @param entityClass 实体类
     * @param entityId   实体id
     * @return
     */
    public <T> T find(Class<T> entityClass, Object entityId);
}
