package com.john.repository;

import com.john.entity.Collect;

import java.util.List;

public interface CollectRepository {
    List<Collect> findByUserId(Integer userId);
    int save(Collect collect);
    int delete(Collect collect);
}
