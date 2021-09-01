package com.project.dogsear.domain;

import com.project.dogsear.dto.QuotePageDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.dogsear.domain.QuoteHash;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuoteHashRepository extends JpaRepository<QuoteHash, Long> {
    public QuoteHash findOneByHashidAndQuoteid(HashTag hashTag,QuoteEntity quote);
    public List<QuoteHash> findByQuoteid(QuoteEntity quoteEntity);
    public List<QuoteHash> findByHashid(HashTag hashTag);
    public Long countByHashid(HashTag hashTag);

    @Query("select new com.project.dogsear.dto.QuotePageDto(q.id,q.title,q.quote,q.writer,q.date,q.color) from QuoteHash p join p.quoteid q where hash_id=:hash")
    public List<QuotePageDto> findByHashidJoinWithQuote(@Param("hash") Long hash);

    @Query("select new com.project.dogsear.dto.QuotePageDto(q.id,q.title,q.quote,q.writer,q.date,q.color) from QuoteHash p join p.quoteid q join p.hashid h where h.hashtext=:hash")
    public List<QuotePageDto> findByHashidJoinWithQuote(@Param("hash") String hash_text);

    @Transactional
    @Modifying
    @Query(value = "delete from quote_hash where quote_id in (select quote_id from quote where user_id =?1)",nativeQuery = true)
    void deleteQuoteHashByUserId(Long user_id);
}
