package com.project.dogsear.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository  extends JpaRepository<Search, Long> {
    public List<Search> findByUser(User user);
    public List<Search> findTop5ByUserOrderByDateDesc(User user);
    public List<Search> findTop10ByUserOrderByDateDesc(User user);

}
