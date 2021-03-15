package com.john.service;

import com.john.entity.Collect;

import java.util.List;

public interface CollectService {
    List<Collect> findByUserId(Integer userId);
    int save(Collect collect);
    int delete(Collect collect);
}
