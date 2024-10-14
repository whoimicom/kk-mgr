package com.whoimi.repository;

import com.whoimi.model.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


/**
 * @author whoimi
 */
@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long>, PagingAndSortingRepository<UserInfo,Long> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return
     */
    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findByPassword(String password);
    Optional<UserInfo> findByEmail(String email);

}
