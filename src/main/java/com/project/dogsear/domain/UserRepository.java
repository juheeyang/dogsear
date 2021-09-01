package com.project.dogsear.domain;

import com.project.dogsear.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    @Override
    void deleteById(Long aLong);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET image = ?1 where user_id = ?2",nativeQuery = true)
    void editImageById(Integer image,Long user_id);

}
