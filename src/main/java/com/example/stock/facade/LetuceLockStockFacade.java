package com.example.stock.facade;

import com.example.stock.repository.RedisLocalRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LetuceLockStockFacade {

    private RedisLocalRepository redisLocalRepository;

    private StockService stockService;

    public LetuceLockStockFacade(RedisLocalRepository redisLocalRepository, StockService stockService) {
        this.redisLocalRepository = redisLocalRepository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (redisLocalRepository.lock(key)) {
            Thread.sleep(1000);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLocalRepository.unlock(key);
        }
    }
}
