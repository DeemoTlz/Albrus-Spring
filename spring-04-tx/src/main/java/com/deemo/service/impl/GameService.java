package com.deemo.service.impl;

import com.deemo.dao.GameDao;
import com.deemo.service.IGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GameService implements IGameService {
    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    @Transactional
    public boolean insert(String game, double price) {
        return this.insert(game, price, false);
    }

    @Override
    @Transactional
    public boolean insert(String game, double price, boolean error) {
        boolean insert = this.gameDao.insert(game, price);

        if (error) {
            int x = 10 / 0;
        }
        log.info("insert result: {}.", insert);
        return insert;
    }

}
