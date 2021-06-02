package kim.kin.repository;

import kim.kin.model.UserInfo;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author choky
 */
@Repository
public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long> {
//public interface UserInfoRepository extends QueryByExampleExecutor<UserInfo>,QuerydslPredicateExecutor<UserInfo>{
//public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long>,QuerydslPredicateExecutor<UserInfo>{

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return
     */
    Optional<UserInfo> findByUsername(String username);


}
