package com.project.dogsear.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.dogsear.domain.QuoteEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {

    public List<QuoteEntity> findByUser(User user);
    public List<QuoteEntity> findByUserAndDate(User user,LocalDate date);

//    @Query("select date,count(date) from QuoteEntity where user_id =:id group by date")
//    public List<Object[]> findByUser(@Param("id") Long user_id);

    @Query(value = "select h.hash_text " +
            "from quote as q join quote_hash as qh on q.quote_id=qh.quote_id  join hash_tag as h on qh.hash_id = h.hash_id " +
            "where q.user_id=?1 " +
            "group by qh.hash_id " +
            "order by count(qh.hash_id) desc " +
            "limit 3;",nativeQuery = true)
    public List<Object> findByUserBest3(Long user_id);

    @Transactional
    @Modifying
    @Query(value = "delete from quote where user_id = ?1",nativeQuery = true)
    void deleteQuoteByUserId(Long user_id);
}
