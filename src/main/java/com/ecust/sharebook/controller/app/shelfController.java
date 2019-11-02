package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.shelf.CatgBook;
import com.ecust.sharebook.pojo.util.shelf.IndexData;
import com.ecust.sharebook.pojo.util.shelf.myShelf;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import com.ecust.sharebook.utils.shiro.UserRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/small")
public class shelfController {
    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private TCateService tCateService;
    @Autowired
    private TUserBookService tUserBookService;
    @Autowired
    private TBookService tBookService;
    @Autowired
    private TBookCategoryService tBookCategoryService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TBookUserBorrowService tBookUserBorrowService;


    /**
     * shelf  加载书架
     * params：access_token ,mode
     * res: data:IndexData
     *      status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/myshelf")
    public R index(@RequestParam Map<String, Object> params){
        System.out.println("/myshelf");
        IndexData data = new IndexData();
        R r=new R();
        try {
            String openId =jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());

            Map<String,Object> result = new HashMap<>();

            if(openId != null){
                Map<String, Object> param = new HashMap<>();
                param.put("openId",openId);
                List<UserInf>  userInfs= tMemberService.list(param); //查询用户信息（根据openId 查询 userId）
                if(userInfs  != null){
                    UserInf seMember = userInfs.get(0);
                    Integer userId = seMember.getUserId();
                    param.clear();
                    param.put("userId",userId);

                    //查询所有分类
                    List<CategoryInf> cat_list  = tCateService.list();;

                    //添加分类-所有
                    CategoryInf categoryInfTemp = new CategoryInf();
                    categoryInfTemp.setCatgId(0);
                    categoryInfTemp.setCatgName("所有");
                    cat_list.add(0,categoryInfTemp);

                    //每一分类创建书籍列表
                    List<CatgBook> catg_book_list = new ArrayList<>();
                    for(CategoryInf ci:cat_list){
                        CatgBook cb = new CatgBook();
                        cb.setCatgId(ci.getCatgId());
                        catg_book_list.add(cb);
                    }

                    //查询分类下的所有书籍
                    try{
                        //查询借阅的书籍和拥有的书籍
                        List<myShelf> myshelfs =  tUserBookService.findShelf(param);

                        for(myShelf myShelfTemp : myshelfs){
                            myShelfTemp.sortTime(seMember.getUserId());
                            BookInf bookInfTemp = tBookService.selectByIsbn(myShelfTemp.getIsbn());
                            myShelfTemp.setBookName(bookInfTemp.getBookName());
                            myShelfTemp.setPicPath(bookInfTemp.getPicPath());
                            myShelfTemp.setAuthor(bookInfTemp.getAuthor());
                            for(CatgBook catgBookTemp :catg_book_list){
                                //所有书籍
                                if(catgBookTemp.getCatgId()==0){
                                    catgBookTemp.getCatg_book_list().add(myShelfTemp);
                                    continue;
                                }else{
                                    //将书籍放置到对应的分类中
                                    param.clear();
                                    param.put("isbn",myShelfTemp.getIsbn());
                                    param.put("catgId",catgBookTemp.getCatgId());
                                    rBookCategory rbg = tBookCategoryService.findCatgbyIsbn(param);
                                    if(rbg !=null){
                                        catgBookTemp.getCatg_book_list().add(myShelfTemp);
                                    }
                                }

                            }
                        }
                        data.setCat_list(cat_list);
                        data.setCatg_book_list(catg_book_list);
                        r.put("data",data);
                        result.put("isSuccess",2);
                    }catch (Exception e){
                        result.put("isSuccess",3);
                    }


                }else
                    result.put("isSuccess",1);
            }else
                result.put("isSuccess",0);

            r.put("status",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }




    /**
     * othershelf  加载书架
     * params：fid
     * res: data:IndexData
     *      status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/othershelf")
    public R Findex(@RequestParam Map<String, Object> params){
        System.out.println("/othershelf");
        IndexData data = new IndexData();
        R r=new R();
        try {
             Integer fid = Integer.valueOf(params.get("fid").toString());
            Map<String,Object> result = new HashMap<>();

            if(fid != null){
                Map<String, Object> param = new HashMap<>();
                param.put("userId",fid);
                param.put("ownerId",fid);
                try{

                    UserInf  seMember= tMemberService.list(param).get(0); //查询用户信息（fid (userId)查询）
                    if(seMember  != null){
                        //查询所有分类
                        List<CategoryInf> cat_list  = tCateService.list();;

                        //添加分类-所有
                        CategoryInf categoryInfTemp = new CategoryInf();
                        categoryInfTemp.setCatgId(0);
                        categoryInfTemp.setCatgName("所有");
                        cat_list.add(0,categoryInfTemp);

                        //每一分类创建书籍列表
                        List<CatgBook> catg_book_list = new ArrayList<>();
                        for(CategoryInf ci:cat_list){
                            CatgBook cb = new CatgBook();
                            cb.setCatgId(ci.getCatgId());
                            catg_book_list.add(cb);
                        }

                        //查询分类下的所有书籍

                        //拥有的书籍以及privacy=0 （公开）
                        List<myShelf> otherShelfs =  tUserBookService.findOtherShelf(param);

                        for(myShelf myShelfTemp : otherShelfs){
                            myShelfTemp.setBorrowAll();
                            BookInf bookInfTemp = tBookService.selectByIsbn(myShelfTemp.getIsbn());
                            myShelfTemp.setBookName(bookInfTemp.getBookName());
                            myShelfTemp.setPicPath(bookInfTemp.getPicPath());
                            myShelfTemp.setAuthor(bookInfTemp.getAuthor());
                            for(CatgBook catgBookTemp :catg_book_list){
                                //所有书籍
                                if(catgBookTemp.getCatgId()==0){
                                    catgBookTemp.getCatg_book_list().add(myShelfTemp);
                                    continue;
                                }else{
                                    //将书籍放置到对应的分类中
                                    param.clear();
                                    param.put("isbn",myShelfTemp.getIsbn());
                                    param.put("catgId",catgBookTemp.getCatgId());
                                    rBookCategory rbg = tBookCategoryService.findCatgbyIsbn(param);
                                    if(rbg !=null){
                                        catgBookTemp.getCatg_book_list().add(myShelfTemp);
                                    }
                                }

                            }
                        }
                        data.setCat_list(cat_list);
                        data.setCatg_book_list(catg_book_list);
                        r.put("data",data);
                        result.put("isSuccess",2);

                    }else
                        result.put("isSuccess",1);  //seMember ==null
                }catch (Exception e){
                    System.out.println("查无此用户信息fid");
                }


            }else
                result.put("isSuccess",0);  //fid == null

            r.put("status",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



    /**
     * shelf-list  加载书籍列表
     * params：isbn ,access_token
     * res: data:booklist
     *      status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/myshelf/list")
    public R findList(@RequestParam Map<String, Object> params){
        System.out.println("/myshelf/list");
        R r=new R();
        try {
            String openId =jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            String isbn = params.get("isbn").toString();
            Map<String, Object> param = new HashMap<>();
            param.put("openId",openId);
            param.put("isbn",isbn);
            Map<String,Object> result = new HashMap<>();

            if(openId != null){
                List<UserInf>  userInfs= tMemberService.list(param); //查询用户信息（根据openId 查询 userId）
                if(userInfs  != null) {
                    UserInf seMember = userInfs.get(0);
                    Integer userId = seMember.getUserId();
                    param.put("userId", userId);
                    System.out.println(param);

                            //查询借阅的书籍和拥有的书籍
                            myShelf bookList = tUserBookService.findShelfCateLog(param).get(0);
                            if(bookList!=null){
                                bookList.sortTime(userId);
                                BookInf bookInfTemp = tBookService.selectByIsbn(bookList.getIsbn());
                                bookList.setBookName(bookInfTemp.getBookName());
                                bookList.setPicPath(bookInfTemp.getPicPath());
                                bookList.setAuthor(bookInfTemp.getAuthor());

                                r.put("data", bookList);
                                result.put("isSuccess", 2);
                            }else
                                result.put("isSuccess", 1);



                }else
                    result.put("isSuccess",0);
            }else
                result.put("isSuccess",-1);

            r.put("status",result);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(("无数据"));
            return R.error();
        }
        return r;
    }

    /**
     * othershelf-list  加载书籍列表
     * params：isbn ,access_token
     * res: data:booklist
     *      status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/othershelf/list")
    public R findOtherList(@RequestParam Map<String, Object> params){
        System.out.println("/othershelf/list");
        R r=new R();
        try {
            Integer fid = Integer.valueOf(params.get("fid").toString());
            String isbn = params.get("isbn").toString();
            Map<String,Object> result = new HashMap<>();

            if(fid != null) {
                Map<String, Object> param = new HashMap<>();
                param.put("userId", fid);
                param.put("ownerId", fid);
                param.put("isbn",isbn);
                try {

                    UserInf seMember = tMemberService.list(param).get(0); //查询用户信息（fid (userId)查询）
                    if (seMember != null) {
                        try {
                            //查询借阅的书籍和拥有的书籍
                            myShelf bookList = tUserBookService.findOtherShelfCateLog(param).get(0);
                            if (bookList != null) {
                                bookList.setBorrowAll();
                                BookInf bookInfTemp = tBookService.selectByIsbn(bookList.getIsbn());
                                bookList.setBookName(bookInfTemp.getBookName());
                                bookList.setPicPath(bookInfTemp.getPicPath());
                                bookList.setAuthor(bookInfTemp.getAuthor());

                                r.put("data", bookList);
                                result.put("isSuccess", 2);
                            } else
                                result.put("isSuccess", 1);

                        } catch (Exception e) {
                            System.out.println("查无书籍");
                        }
                    } else
                        result.put("isSuccess", 0);  //seMember == null
                } catch (Exception e) {
                    System.out.println("查无此用户信息fid");
                }
            }else
                result.put("isSuccess", -1);  //fid == null
            r.put("status",result);


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



    /**
     * asecond/bdself 详情界面
     * params:bookId
     * res ：status:is_exist
     *       userBook
     *       bookInf
     *       userInfo{ nickName,avatarUrl }
     *
     **/
    @ResponseBody
    @GetMapping({"/asecond/bdself"})
    public R queryBook(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/asecond/bdself");
        try {
            Integer bookId = Integer.valueOf(params.get("bookId").toString());
            String openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            Map<String , Object> param = new HashMap<>();
            Map<String, Object> status = new HashMap<>();
            Map<String, Object> userInfos = new HashMap<>();

            //根据bookid 查询 isbn
            rUserBook rUserBookTemp =  tUserBookService.selectByPrimaryKey(bookId);
            if(rUserBookTemp!=null){
                param.put("isbn",rUserBookTemp.getIsbn());
                BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
                if(bookInfTemp!=null){
                    status.put("is_exist",2); //成功查阅到书籍信息
                    param.put("openId",openId);
                    UserInf userInf=tMemberService.list(param).get(0);
                    userInfos.put("nickName",userInf.getNickName());
                    userInfos.put("avatarUrl",userInf.getAvatarUrl());
                    r.put("userBook",rUserBookTemp);
                    r.put("bookInf",bookInfTemp);
                    r.put("userInfo",userInfos);

                }else status.put("is_exist",1); //查阅到书籍信息失败
            }
            else
                status.put("is_exist",0); //bookid 不存在

            r.put("status",status);


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    /**
     * asecond/bdself  设置公开/私密
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdself/privacy"})
    public R setPrivacy(@RequestParam Map<String, Object> params){
        System.out.println("/asecond/bdself/privacy");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            result.put("save_success", -1);
            String openId=jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            param.put("openId",openId);
            Integer bookId=Integer.valueOf(params.get("bookId").toString());
            Integer privacy =  Integer.valueOf(params.get("privacy").toString());
            rUserBook searchBookTemp  = new rUserBook();
            searchBookTemp.setPrivacy(privacy);
            searchBookTemp.setBookId(bookId);
            if(openId!=null&&bookId!=null&&privacy!=null){
                UserInf seMember = tMemberService.list(param).get(0);
                if(seMember!=null){
                    params.put("ownerId",seMember.getUserId());
                    int i = tUserBookService.updateByPrimaryKeySelective(searchBookTemp);
                    if(i==0) {
                        result.put("save_success", 0); //更新失败
                    }else{
                        result.put("save_success", 1); //更新成功
                    }
                }

            }
            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


    @ResponseBody
    @GetMapping({"asecond/bdself/deleteBook"})
    public R deleteBook(@RequestParam Map<String, Object> params){
        System.out.println("asecond/bdself/deleteBook");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> type = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            result.put("save_success", -1);
            String openId=jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            param.put("openId",openId);
            UserInf seMember= tMemberService.list(param).get(0); //查询用户信息（根据openId 查询
            Integer bookId=Integer.valueOf(params.get("bookId").toString());
            param.put("bookId",bookId);
            String isbn = tUserBookService.list(param).get(0).getIsbn();
            param.put("isbn",isbn);

            if(openId!=null&&bookId!=null&&seMember!=null){
                    int i = tUserBookService.deleteByPrimaryKey(bookId);
                    if(i==0) {
                        result.put("save_success", 0); //删除失败
                    }else{
                        result.put("save_success", 1); //删除成功
                        param.put("userId", seMember.getUserId());
                        //查询借阅的书籍和拥有的书籍
                        try{
                            List <myShelf> bookLists = tUserBookService.findShelfCateLog(param);
                            if(bookLists!=null&&bookLists.size()!=0){
                                type.put("isList", 1); //否则跳转到list
                            }
                            else{
                                type.put("isList", 0); //无书籍，跳转到首页
                            }

                        }catch(Exception e){
                            System.out.println("null");
                        }
                        r.put("type",type);
                    }


            }
            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }




    /**
     * asecond/apply  申请借阅/取消
     **/

    @ResponseBody
    @GetMapping({"/asecond/apply"})
    public R apply(@RequestParam Map<String, Object> params){
        System.out.println("/asecond/privacy");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            String isbn = params.get("isbn").toString();
            String ownerId = params.get("userId").toString();
            String mode = params.get("mode").toString();

            Map<String,Object>  param = new HashMap<>();
            params.put("openId",openId);
            param.put("ownerId",ownerId);
            param.put("isbn",isbn);


            if(openId!=null&&ownerId!=null&&isbn!=null){
                UserInf seMember = tMemberService.selectOne(params);
                rUserBook mrUserBook= tUserBookService.SelectByIsbn(param);
                if(seMember!=null && mrUserBook!=null){
                    Integer userId  = seMember.getUserId();
                    Integer bookId  = mrUserBook.getBookId();

                    param.clear();
                    param.put("userId",userId);
                    param.put("bookId",bookId);
                    if(mode.equals("apply")){
                        Integer usrBorrowState = Integer.valueOf(1);
                        param.put("usrBorrowState",usrBorrowState);
                        int i = tBookUserBorrowService.updateState(param);
                        if(i==0) {
                            result.put("save_success", 0); //更新失败
                        }else{
                            result.put("save_success", 1); //更新成功
                        }
                    }else{
                        Integer usrBorrowState = Integer.valueOf(0);
                        param.put("usrBorrowState",usrBorrowState);
                        int i = tBookUserBorrowService.updateState(param);
                        if(i==0) {
                            result.put("save_success", 0); //更新失败
                        }else{
                            result.put("save_success", 1); //更新成功
                        }
                    }

                }

            }
            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    /**
     * asecond/bdother/opt  申请借阅,取消申请,归还
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/opt"})
    public R opt(@RequestParam Map<String, Object> params){
        System.out.println("/asecond/bdother/opt");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            Integer id = Integer.valueOf(params.get("id").toString());
            String mode = params.get("mode").toString();
            Map<String,Object>  param = new HashMap<>();
            param.put("openId",openId);
            UserInf seMember = tMemberService.list(param).get(0);
            param.clear();
            if(openId!=null&&mode!=null && seMember!=null){
                if(mode.equals("apply")){
                    String time = params.get("time").toString();
                    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(time);
                    rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                    rBookUserBorrows.setBookId(id);
                    rBookUserBorrows.setBorrowTime(date);
                    rBookUserBorrows.setUserId(seMember.getUserId());
                    rUserBook rUserBooks = new rUserBook();
                    rUserBooks.setBookId(id);
                    rUserBooks.setBorrowState(Integer.valueOf(1));
                    MessageInf message = new MessageInf();

                    param.put("bookId",id);
                    message.setmType(Integer.valueOf(0));//申请
                    message.setSenderId(seMember.getUserId());
                    message.setReceiverId(tUserBookService.list(param).get(0).getOwnerId());
                    message.setDateTime(date);

                    int i =tBookUserBorrowService.save(rBookUserBorrows,rUserBooks,message);
                    if(i==0) {
                        result.put("save_success", 0); //更新失败
                    }else{
                        result.put("save_success", 1); //更新成功
                    }


                }else if(mode.equals("cancel")){ //取消申请
                    Integer usrBorrowState = Integer.valueOf(5);
                    param.put("usrBorrowState",usrBorrowState);
                    param.put("borrowId",id);
                    int i = tBookUserBorrowService.updateState(param);
                    if(i==0) {
                        result.put("save_success", 0); //更新失败
                    }else{
                        result.put("save_success", 1); //更新成功
                        try{
                            param.put("usrBorrowState",Integer.valueOf(1));
                            param.put("borrowId",id);
                            List<rBookUserBorrow> rBookUserBorrows = tBookUserBorrowService.list(param);
                            if(rBookUserBorrows.size()!=0&&rBookUserBorrows!=null){
                               System.out.println("还有申请");
                            }else{
                                //若无申请，修改 ruserbook state --0  可借阅
                                rUserBook record = new rUserBook();
                                record.setBookId(rBookUserBorrows.get(0).getBookId());
                                record.setBorrowState(Integer.valueOf(0)); //无申请
                              tUserBookService.updateByPrimaryKeySelective(record);
                            }
                        }catch (Exception e){
                            System.out.println("rBookUserBorrows null");
                        }
                    }
                }else{
                    //return
                    Integer usrBorrowState = Integer.valueOf(3); //归还中
                    param.put("usrBorrowState",usrBorrowState);
                    param.put("borrowId",id);
                    int i = tBookUserBorrowService.updateState(param);
                    if(i==0) {
                        result.put("save_success", 0); //更新失败
                    }else{
                        result.put("save_success", 1); //更新成功
                    }
                }



            }
            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }





    /**
     * asecond/otherself 详情界面
     * params:id ,mode ,isbn ,fid
     * res ：status:is_exist
     *       userBook
     *       bookInf
     *       userInfo{ nickName,avatarUrl }
     *       borrow(other)
     *
     **/
    @ResponseBody
    @GetMapping({"/asecond/bdother"})
    public R queryOtherBook(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/asecond/bdother");
        try {
            String openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            Integer id = Integer.valueOf(params.get("id").toString());
            String  mode = params.get("mode").toString();
            String  isbn = params.get("isbn").toString();
            Map<String , Object> param = new HashMap<>();
            Map<String, Object> status = new HashMap<>();
            Map<String, Object> userInfos = new HashMap<>();  //自己的信息
            Map<String, Object> fuserInfos = new HashMap<>();
            if(mode.equals("self")){

                try{
                    //根据bookid 查询 isbn
                    param.put("borrowId",id);
                    rBookUserBorrow rBookUserBorrowTemp =  tBookUserBorrowService.list(param).get(0);
                    if(rBookUserBorrowTemp!=null){
                        param.put("isbn",isbn);
                        try{
                            BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
                            if(bookInfTemp!=null){
                                status.put("is_exist",2); //成功查阅到书籍信息
                                param.put("openId",openId);
                                UserInf userInf=tMemberService.list(param).get(0);
                                userInfos.put("nickName",userInf.getNickName());
                                userInfos.put("avatarUrl",userInf.getAvatarUrl());
                                param.clear();
                                rUserBook rUserBookTemp =  tUserBookService.selectByPrimaryKey(rBookUserBorrowTemp.getBookId());
                                param.put("userId",rUserBookTemp.getOwnerId());
                                UserInf fuserInf=tMemberService.list(param).get(0);
                                fuserInfos.put("nickName",fuserInf.getNickName());
                                fuserInfos.put("avatarUrl",fuserInf.getAvatarUrl());
                                fuserInfos.put("userId",fuserInf.getUserId());
                                r.put("userBook",rBookUserBorrowTemp);
                                r.put("bookInf",bookInfTemp);
                                r.put("userInfo",userInfos);
                                r.put("fuserInfo",fuserInfos);
                            }else status.put("is_exist",1); //查阅到书籍信息失败
                        } catch (Exception e){
                        System.out.println("bdother-self bookInfTemp==null");
                    }


                    }
                    else
                        status.put("is_exist",0); //bookid 不存在
                }catch (Exception e){
                    System.out.println("bbdother-self rBookUserBorrowTemp==null");
                }

                r.put("status",status);
            }else{
                Integer fid = Integer.valueOf(params.get("fid").toString());

                try{
                    //根据bookid 查询 isbn
                    rUserBook rUserBookTemp =  tUserBookService.selectByPrimaryKey(id);
                    if(rUserBookTemp!=null){
                        param.put("isbn",isbn);
                        BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
                        if(bookInfTemp!=null){
                            status.put("is_exist",2); //成功查阅到书籍信息
                            param.put("openId",openId);
                            UserInf userInf=tMemberService.list(param).get(0);
                            userInfos.put("nickName",userInf.getNickName());
                            userInfos.put("avatarUrl",userInf.getAvatarUrl());
                            param.clear();
                            param.put("userId",fid);
                            UserInf fuserInf=tMemberService.list(param).get(0);
                            fuserInfos.put("nickName",fuserInf.getNickName());
                            fuserInfos.put("avatarUrl",fuserInf.getAvatarUrl());
                            r.put("userBook",rUserBookTemp);
                            r.put("bookInf",bookInfTemp);
                            r.put("userInfo",userInfos);
                            r.put("fuserInfo",fuserInfos);
                            param.clear();
                            param.put("userId",userInf.getUserId());
                            param.put("bookId",id);

                            try{
                                List<rBookUserBorrow> rBookUserBorrows=tBookUserBorrowService.listByState(param);
                                if(rBookUserBorrows.size()!= 0 && rBookUserBorrows!=null){
                                    r.put("borrow",true); //1，2，3
                                }else{
                                    r.put("borrow",false);
                                }
                            }catch (Exception e){
                                System.out.println("check null");
                                r.put("borrow",false);
                            }
                        }else status.put("is_exist",1); //查阅到书籍信息失败
                    }
                    else
                        status.put("is_exist",0); //bookid 不存在
                }catch (Exception e){
                    System.out.println("bbdother-other rBookUserBorrowTemp==nul");
                }

                r.put("status",status);

            }


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



}
