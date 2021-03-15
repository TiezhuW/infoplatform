package com.john.service.impl;

import com.john.entity.Collect;
import com.john.repository.CollectRepository;
import com.john.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectRepository collectRepository;

    @Override
    public List<Collect> findByUserId(Integer userId) {
        return collectRepository.findByUserId(userId);
    }

    @Override
    public int save(Collect collect) {
        return collectRepository.save(collect);
    }

    @Override
    public int delete(Collect collect) {
        return collectRepository.delete(collect);
    }
}
