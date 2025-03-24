package com.sky.service;

import com.sky.dto.SetmealDTO;
import io.swagger.v3.oas.annotations.servers.Server;


public interface SetMealService {
    void insert(SetmealDTO setmealDTO);
}
