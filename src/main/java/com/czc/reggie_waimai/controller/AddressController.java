package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.czc.reggie_waimai.common.BaseContext;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.AddressBook;
import com.czc.reggie_waimai.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressController {

    @Autowired
    private AddressBookService addressBookService;

//新增地址

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId((BaseContext.getCurrentId()));//BaseContext.getCurrentId()是获取当前用户ID的方法

        log.info("addressBook:{}",addressBook);

        addressBookService.save(addressBook);
        return R.success(addressBook);

    }
/**
 * 设置默认地址
 */

@PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
    log.info("addressBook:{}", addressBook);
    LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
    wrapper.set(AddressBook::getIsDefault, 0);
    //SQL:update address_book set is_default = 0 where user_id = ?
    addressBookService.update(wrapper);

    addressBook.setIsDefault(1);
    //SQL:update address_book set is_default = 1 where id = ?
    addressBookService.updateById(addressBook);
    return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id){
        AddressBook addressBook=addressBookService.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }

    }
    /**
     * 查询默认地址
     */

    @GetMapping("default")
    public R<AddressBook>getDefault(){

        LambdaQueryWrapper<AddressBook>queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
/*addressBookService是一个服务类，其中包含了一些操作AddressBook实例的方法。getOne是其中的一个方法，用于从数据库中获取一个AddressBook实例。
具体来说，它使用了MyBatis-Plus提供的getOne方法，该方法接受一个QueryWrapper对象作为参数，用于指定查询条件。QueryWrapper是MyBatis-Plus提供的一个查询条件构造器，
它可以帮助我们构建复杂的查询条件，例如等于、不等于、大于、小于等条件。在这里，queryWrapper参数可能包含了一个等于某个字段值的条件，以便从数据库中获取指定的AddressBook实例。
如果查询结果为空，getOne方法将返回null。*/
        AddressBook addressBook=addressBookService.getOne(queryWrapper);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }

    }



    /**
     * 查询指定用户的全部地址
     */
  @GetMapping("/list")
    public R<List<AddressBook>>list(AddressBook addressBook){
      addressBook.setUserId(BaseContext.getCurrentId());

      log.info("addressBook:{}",addressBook);

      //条件构造器
      LambdaQueryWrapper<AddressBook>queryWrapper=new LambdaQueryWrapper<>();

      queryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());

      queryWrapper.orderByDesc(AddressBook::getUpdateTime);

      return R.success(addressBookService.list(queryWrapper));


  }


}
