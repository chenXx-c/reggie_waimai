package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.Dto.SetmealDto;
import com.czc.reggie_waimai.common.CustomException;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Category;
import com.czc.reggie_waimai.entity.Setmeal;
import com.czc.reggie_waimai.entity.SetmealDish;
import com.czc.reggie_waimai.mapper.CategoryMapper;
import com.czc.reggie_waimai.mapper.SetmealDishMapper;
import com.czc.reggie_waimai.mapper.SetmealMapper;
import com.czc.reggie_waimai.service.SetmealDishService;
import com.czc.reggie_waimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {


        //保存套餐基本信息,操作setmeal表，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息,操作setmeal dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);//saveBatch是MyBatis-Plus框架提供的方法，它可以将一组对象批量保存到数据库中


    }


    @Override
    public void pageBySetmeal(int page, int pageSize, String name) {
        Page<Setmeal> mealPage = new Page<>(page, pageSize);
        Page<SetmealDto> mealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(mealPage, queryWrapper);

        // pageInfo 里面的 records 是没有categoryName的！
        // 所以 records 直接 忽略，要在后面手动赋值
        BeanUtils.copyProperties(mealPage, mealDtoPage, "records");
        List<Setmeal> records = mealPage.getRecords();

        // 处理 records
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // item是一条records(list)记录，将Setmeal中的字段赋值给 setmealDto
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        mealDtoPage.setRecords(list);

//        return R.success(mealDtoPage);
    }

    @Override
    public void sellStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, Setmeal::getId, ids);

        List<Setmeal> list = setmealMapper.selectList(queryWrapper);

        for (Setmeal setmeal : list) {
            if (setmeal != null) {
                setmeal.setStatus(status);
                setmealMapper.updateById(setmeal);
            }
        }

//        return R.success("售卖状态修改成功");
    }


    @Transactional
    /*@RequestParam是一个Spring框架的注解，用于将HTTP请求中的参数绑定到方法的参数上。
在Spring MVC中，当我们定义一个Controller方法时，可以使用@RequestParam注解来指定该方法所需的参数，并将它们绑定到HTTP请求中的参数上。例如：
@GetMapping("/users")
public List<User> getUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
    // ...
}
在上面的例子中，我们定义了一个名为getUsers的Controller方法，它接受两个参数：page和size。
这两个参数都使用了@RequestParam注解来指定它们在HTTP请求中的参数名。当一个HTTP请求被发送到该Controller方法时，Spring框架会自动将请求中的page和size参数的值绑定到方法的参数上，并将它们传递给方法进行处理。
需要注意的是，@RequestParam注解有一些可选的属性，例如required、defaultValue和value等，它们可以用来指定参数是否必须、默认值以及参数名等信息。*/
    public void removeWithDish(List<Long> ids) {
        // 查菜品的状态，是否可以删除（）
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();


/*`setmealQueryWrapper.in()` 是 MyBatis-Plus 中的一个查询构造器方法，用于构建一个 IN 查询条件。它的作用是查询指定属性值在一个集合中的记录。在这段代码中，`setmealQueryWrapper.in(Setmeal::getId, ids)` 的作用是查询 Setmeal 实体类中 id 属性值在 ids 集合中的记录。
`setmealQueryWrapper.eq()` 是 MyBatis-Plus 中的一个查询构造器方法，用于构建一个等于查询条件。它的作用是查询指定属性值等于某个值的记录。在这段代码中，`setmealQueryWrapper.eq(Setmeal::getStatus, 1)` 的作用是查询 Setmeal 实体类中 status 属性值等于 1 的记录。*/
        setmealQueryWrapper.in(Setmeal::getId, ids);//查询套餐状态上是否可以删除
        setmealQueryWrapper.eq(Setmeal::getStatus, 1);



        int count = this.count(setmealQueryWrapper);
        if (count > 0) {
                 throw new CustomException("菜品正在售卖中！不能删除！");
        }

        //如果可以删除，先删除套餐中的数据-setmeal
        this.removeByIds(ids);


       // setmealMapper.deleteBatchIds(ids);
       //
        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.in(SetmealDish::getSetmealId, ids);

        //删除关系表的数据
          setmealDishService.remove(setmealDishQueryWrapper);
//        return R.success("删除菜品成功！");


    }
}