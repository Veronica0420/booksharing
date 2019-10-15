package com.ecust.sharebook.config.EhCache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTypeDaoImplTest {

    @Autowired
    private GoodsTypeDaoImpl typeDao;

    @Test
    public void testSave() {
        String typeId = "type111";
        // 模拟第一次保存
        String returnStr1 = typeDao.save(typeId);
        System.out.println(returnStr1);
        String returnStr2 = typeDao.select(typeId);
        System.out.println(returnStr2);

    }

    @Test
    public void testUpdate() {
        String typeId = "type111";
        // 模拟第一次查询
        String returnStr1 = typeDao.select(typeId);
        System.out.println(returnStr1);
        // 模拟第二次查询

        // 模拟更新
        String returnStr3 = typeDao.update(typeId);
        System.out.println(returnStr3);
        // 模拟查询
        String returnStr4 = typeDao.select(typeId);
        System.out.println(returnStr4);
    }

    @Test
    public void testDelete() {
        String typeId = "type111";
        // 模拟第一次查询
        String returnStr1 = typeDao.select(typeId);
        System.out.println(returnStr1);
        // 模拟第二次查询
        String returnStr2 = typeDao.select(typeId);
        System.out.println(returnStr2);
        // 模拟删除
        String returnStr3 = typeDao.delete(typeId);
        System.out.println(returnStr3);
        // 模拟查询
        String returnStr4 = typeDao.select(typeId);
        System.out.println(returnStr4);
    }

    @Test
    public void testSelect() {
        String typeId = "type111";
        // 模拟第一次查询
        String returnStr1 = typeDao.select(typeId);
        System.out.println(returnStr1);

    }

}