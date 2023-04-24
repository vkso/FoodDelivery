package com.home.reggie.dto;

import com.home.reggie.entity.Setmeal;
import com.home.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
