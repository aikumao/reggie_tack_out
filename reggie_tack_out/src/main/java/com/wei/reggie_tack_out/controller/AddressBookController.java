package com.wei.reggie_tack_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wei.reggie_tack_out.common.BaseContext;
import com.wei.reggie_tack_out.common.CustomException;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.entity.AddressBook;
import com.wei.reggie_tack_out.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
            addressBook.setUserId(BaseContext.getCurrentId());
            log.info("addressBook={}", addressBook);

            //条件构造器
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
            queryWrapper.orderByDesc(AddressBook::getUpdateTime);

            List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
            return Result.success(addressBooks);
    }


    /* 新增收获地址 */
    @PostMapping
    public Result<AddressBook> addAddress(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /* 设置默认地址 */
    @GetMapping("/default")
    public Result<AddressBook> defaultAddress() {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //当前用户
        queryWrapper.eq(userId != null, AddressBook::getUserId, userId);
        //默认地址
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return Result.success(addressBook);
    }

   /* 根据 id 查询地址 回显在编辑地址页面 */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null){
            throw new CustomException("地址信息不存在");
        }
        return Result.success(addressBook);
    }
    /* 修改地址 */
    @PutMapping
    public Result<String> updateAdd(@RequestBody AddressBook addressBook) {
        if (addressBook == null) {
            throw new RuntimeException("地址信息不存在，请刷新重试");
        }
        addressBookService.updateById(addressBook);
        return Result.success("地址修改成功");
    }

    /* 删除地址 */
    @DeleteMapping()
    public Result<String> deleteAdd(@RequestParam("ids") Long id) {
        if (id == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        addressBookService.removeById(id);
        return Result.success("地址删除成功");
    }

}
