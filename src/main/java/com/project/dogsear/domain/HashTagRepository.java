package com.project.dogsear.domain;

import com.project.dogsear.dto.HashReturnDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    public HashTag findByHashtext(String hash_text );

    public List<HashTag> findByHashtextStartingWith(String hash_text);

    public List<HashTag> findByUserAndHashtextStartingWith(User user,String hash_text);

    public HashTag findOneByUserAndHashtext(User user,String hash_text);

    public List<HashTag> findByUser(User user);

    @Query("select new com.project.dogsear.dto.HashReturnDto(h.id,h.hashtext,h.bookmark) from HashTag h where h.user =:user and h.bookmark = true")
    public List<HashReturnDto> findByUserAndBookmark(@Param("user") User user);

    @Query("select new com.project.dogsear.dto.HashReturnDto(h.id,h.hashtext,h.bookmark) from HashTag h where user_id =:user")
    public List<HashReturnDto> findByUser(@Param("user") Long user_id);

    @Query(value = "select hash_text from hash_tag where user_id = ?1 order by date desc limit 5",nativeQuery = true)
    public List<String> findByUserAndDateDesc(Long user_id);

    @Query(value = "select hash_tag.hash_id from hash_tag left outer join quote_hash on hash_tag.hash_id = quote_hash.hash_id where quote_hash_id is null and user_id = ?1",nativeQuery = true)
    public List<Long> findNullHashid(Long user_id);

    @Transactional
    @Modifying
    @Query(value = "delete from hash_tag where user_id =?1",nativeQuery = true)
    void deleteHashByUserId(Long user_id);
}
