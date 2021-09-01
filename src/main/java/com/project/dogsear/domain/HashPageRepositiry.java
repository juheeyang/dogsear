package com.project.dogsear.domain;

import com.project.dogsear.dto.HashReturnDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashPageRepositiry extends CrudRepository<HashTag, Long> {
    Page<HashTag> findByUser(User user, Pageable pageable);

    @Query("select new com.project.dogsear.dto.HashReturnDto(h.id,h.hashtext,h.bookmark) from HashTag h where user_id =:user_id and hits > 0")
    public List<HashReturnDto> findByUserAndHits(@Param("user_id") Long user_id,Pageable pageable);
}
