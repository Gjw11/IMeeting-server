package com.IMeeting.resposirity;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2018/11/19.
 */
@Repository
public interface UserinfoRepository extends JpaRepository<Userinfo,Integer>{
    Userinfo findByUsernameAndPasswordAndStatus(String username,String password,Integer status);
    Userinfo findByPhoneAndPasswordAndStatus(String phone,String password,Integer status);
    Userinfo findByPhone(String phone);
    Userinfo findByWorknumAndTenantId(String worknum,Integer tenantId);
    List<Userinfo> findByDepartId(Integer departId);
    List<Userinfo> findByTenantIdAndStatus(Integer tenantId,Integer status);
    List<Userinfo> findByPositionId(Integer positionId);
    List<Userinfo> findByRoleId(Integer roleId);
    Optional<Userinfo> findById(Integer id);
    Userinfo findByWorknum(String worknum);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.password=?1 where m.phone=?2")
    int updatePwd(String password,String phone);
    @Transactional
    @Modifying(clearAutomatically = true)//刷新hibernate的一级缓存
    @Query(value = "update Userinfo m set m.password=?1 where m.id=?2")
    int changePwd(String password,Integer id);
    Userinfo findByIdAndPassword(Integer id,String password);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.phone=?1 where m.id=?2")
    int updatePhone(String phone,Integer id);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.status=0 where m.id=?1")
    int deleteOne(Integer id);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.username=?2 where m.id=?1")
    int updateUsername(Integer id,String username);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.resume=?1 where m.id=?2")
    int updateResume(String resume,Integer userId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.worknum=?2,m.name=?3,m.phone=?4,m.departId=?5,m.positionId=?6,m.roleId=?7 where m.id=?1")
    int updateUserInfo(Integer id,String worknum,String name, String phone,Integer departId,Integer positionId,Integer roleId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Userinfo m set m.password=?2 where m.id=?1")
    int resetPwd(Integer userId,String pwd);
}
