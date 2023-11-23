package com.users.users.service;

import com.users.users.model.Views;
import com.users.users.repository.ViewsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewsService {
    @Autowired
    private ViewsRepository viewsRepository;

    @Transactional
    public void save(Views views){
        viewsRepository.save(views);
    }
    @Transactional
    public Integer findByRoles(Integer idRoles){
        return viewsRepository.findByRole(idRoles);
    }
    @Transactional
    public void update(Integer count, Integer id) {
        viewsRepository.update(count, id);
    }
}
