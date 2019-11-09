package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.BookCircleInfMapper;
import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.mapper.vBookCreaterBcircleMapper;
import com.ecust.sharebook.mapper.vBookMemberBcircleMapper;
import com.ecust.sharebook.pojo.BookCircleInf;
import com.ecust.sharebook.pojo.vBookCreaterBcircle;
import com.ecust.sharebook.pojo.vBookMemberBcircle;
import com.ecust.sharebook.service.TBookCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TBookCircleServiceIml implements TBookCircleService {
    @Autowired
    private BookCircleInfMapper tBookCircleInfMapper;
    @Autowired
    private rBcircleMemberMapper trBcircleMemberMapper;
    @Autowired
    private vBookCreaterBcircleMapper tvBookCreaterBcircleMapper;
    @Autowired
    private vBookMemberBcircleMapper tvBookMemberBcircleMapper;

    @Override
    public int insert(Map<String, Object> bcInf) {
        int result = tBookCircleInfMapper.insert(bcInf);
        return result;
    }

    @Override
    public BookCircleInf selectByPrimaryKey(Integer circleId){
        BookCircleInf result=tBookCircleInfMapper.selectByPrimaryKey(circleId);
        return result;
    }

    @Override
    public List<BookCircleInf> selectLikBCName(Map<String, Object> param) {
        List<BookCircleInf> list = tBookCircleInfMapper.selectLikebcName(param);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;

    }

    @Override
    public List<BookCircleInf> selectbyCreaterID(Map<String, Object> params) {
        List<BookCircleInf> list = tBookCircleInfMapper.listBycreaterID(params);
        List<Integer> list_id = trBcircleMemberMapper.selectByMemberId(params);
        for (int i = 0; i < list_id.size(); i++) {
            System.out.println("map.put  list_id" + i + "=" + list_id.get(i));
            BookCircleInf tmp = tBookCircleInfMapper.selectByPrimaryKey(list_id.get(i));
            list.add(tmp);
        }
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<BookCircleInf> selectbyNotCreaterIDMemberID(Map<String, Object> params) {
        List<Integer> list_id = trBcircleMemberMapper.selectByMemberId(params);  //本用户不是图书圈成员的所有图书圈ID的list
        Map<String, Object> map = new HashMap<>();
        map.put("list_id", list_id);
        List<BookCircleInf> list = tBookCircleInfMapper.listByNotcreaterID(params);

        for (Iterator<BookCircleInf> iterator = list.iterator(); iterator.hasNext(); ) {
            BookCircleInf x = iterator.next();
            for (int i = 0; i < list_id.size(); i++) {
                if (list_id.get(i).equals(x.getBookCircleId())) {
                    iterator.remove();
                    continue;
                }
            }
        }
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 根据书名得到所有拥有本书的图书圈list
     * @param params
     * @return
     */
    @Override
    public List<BookCircleInf> seletbybName_bc(Map<String, Object> params) {
        List<Integer> list1=tvBookCreaterBcircleMapper.selectByBookName(params);
        System.out.println("list.size==="+list1.size());
        List<Integer> list2=tvBookMemberBcircleMapper.selectByBookName(params);
        list1.addAll(list2);
        HashSet h = new HashSet(list1);
        list1.clear();
        list1.addAll(h);

        List<BookCircleInf> result=new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            result.add(tBookCircleInfMapper.selectByPrimaryKey(list1.get(i)));
            System.out.println("书名获得图书圈 循环"+i+":"+result.get(i).getBookCircleId());
        }
        if (result != null &&result.size() > 0) {
            return result;
        }
        return null;
    }

    /**
     * 根据图书圈名得到圈内成员所有公开书籍列表
     */
    @Override
    public List<vBookMemberBcircle> selectbyBCId(Map<String, Object> params){
        System.out.println("params:"+params);
        List<vBookMemberBcircle> list1=new ArrayList<>();
        List<vBookCreaterBcircle> list2=new ArrayList<>();
        list1=tvBookMemberBcircleMapper.selectByBCId(params);
        list2=tvBookCreaterBcircleMapper.selectByBCId(params);
        System.out.println("params:"+list1.size());
        System.out.println("params:"+list2.size());
        //把vBookCreaterBcircle转为vBookMemberBcircle
        List<vBookMemberBcircle> listTmp=new ArrayList<>();
        for(int i=0;i<list2.size();i++){
            vBookMemberBcircle tmp=new vBookMemberBcircle();
            tmp.setBookCircleId(list2.get(i).getBookCircleId());
            tmp.setBookId(list2.get(i).getBookId());
            tmp.setBookName(list2.get(i).getBookName());
            tmp.setIsbn(list2.get(i).getIsbn());
            tmp.setOwnerId(list2.get(i).getOwnerId());
            tmp.setPrivacy(list2.get(i).getPrivacy());
            listTmp.add(tmp);
        }

        list1.addAll(listTmp);

        for(int i=0;i<list1.size();i++){
            System.out.println("list"+i+"="+list1.get(i).getBookName());
        }
        if(list1!=null&& list1.size()>0)
            return list1;
        else
            return null;
    }


}
