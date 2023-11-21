package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.entity.AddressBook;
import com.wei.reggie_tack_out.mapper.AddressBookMapper;
import com.wei.reggie_tack_out.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
