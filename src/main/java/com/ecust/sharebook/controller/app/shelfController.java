package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.shelf.IndexData;
import com.ecust.sharebook.pojo.util.shelf.book;
import com.ecust.sharebook.pojo.util.shelf.myShelf;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import com.ecust.sharebook.utils.common.transferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private TMessageService tMessageService;
    @Autowired
    private optService toptService;
    @Autowired
    private TFriendService tFriendService;


    /**
     * shelfIndex
     * 加载所有书籍，包括
     * 借阅的书籍：若有多个书籍--跳转进入为列表，否则直接进入详情界面
     * 我的书籍：若有多个书籍--跳转进入为列表
     * url:api.shelf.shelfIndex
     * data:bCatgId：分类（0-所有）
     * privacy：公开0 私密1 所有2
     * isself：我的1 借阅2 所有0
     * 返回：data: IndexData{
     * 分类列表cat_list
     * 书籍列表books
     * 书籍总数count}
     * <p>
     * status:isSuccess（1-成功）（0-失败）
     * myUserId---myUserId
     */

    @ResponseBody
    @GetMapping("/shelfIndex")
    public R shelfIndex(@RequestParam Map<String, Object> params) {

        System.out.println("/shelfIndex");
        System.out.println(params);
        IndexData data = new IndexData();
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        Integer bCatgId = Integer.valueOf(params.get("bCatgId").toString()); //分类
        Integer privacy = Integer.valueOf(params.get("privacy").toString()); // 公开私密
        Integer isself = Integer.valueOf(params.get("isself").toString());  //我的，借阅

        List<CategoryInf> cat_list = new ArrayList<>();
        List<book> bookFinal = new ArrayList<>();


        if (openId != null) {
            if (params.size() == 5) {
                Integer userId = Integer.valueOf(params.get("userId").toString());
                param.put("userId", userId);

            } else {
                param.put("openId", openId);
            }


            try {
                UserInf me = tMemberService.listPublic(param).get(0); //查询用户信息（根据openId 查询 userId）
                if (me != null) {
                    Integer ownerId = me.getUserId();
                    param.clear();

                    /**
                     *  查询所有分类,添加分类-所有
                     * **/

                    cat_list = tCateService.list();
                    CategoryInf categoryInfTemp = new CategoryInf();
                    categoryInfTemp.setCatgId(0);
                    categoryInfTemp.setCatgName("所有");
                    cat_list.add(0, categoryInfTemp);
                    data.setCat_list(cat_list);

                    if (bCatgId != 0) {
                        //所有分类
                        param.put("bCatgId", bCatgId);
                    }

                    param.put("ownerId", ownerId);

                    List<book> bookM = new ArrayList<>();
                    List<book> bookB = new ArrayList<>();

                    if (isself == 0 || isself == 1) {
                        //所有书籍 || 我的书籍
                        //添加分类-我的书籍
                        if (privacy != 2) {
                            //所有
                            param.put("privacy", privacy);
                        }

                        try {
                            bookM = tUserBookService.ownershelf(param);

                            for (book bookMtemp : bookM) {
                                BookInf bookInf = tBookService.selectByPrimaryKey(bookMtemp.getIsbn());
                                bookMtemp.setAuthor(bookInf.getAuthor());
                                bookMtemp.setBookName(bookInf.getBookName());
                                bookMtemp.setPicPath(bookInf.getPicPath());
                                bookMtemp.setType(true);
                                bookFinal.add(bookMtemp);
                            }
                        } catch (Exception e) {
                            System.out.println("bookM--null");
                        }
                    }
                    if (isself == 0 || isself == 2) {
                        //所有书籍 || 借阅书籍
                        try {

                            bookB = tUserBookService.borrowShelf(param);
                            for (book bookBtemp : bookB) {
                                BookInf bookInf = tBookService.selectByPrimaryKey(bookBtemp.getIsbn());
                                bookBtemp.setAuthor(bookInf.getAuthor());
                                bookBtemp.setBookName(bookInf.getBookName());
                                bookBtemp.setPicPath(bookInf.getPicPath());
                                bookBtemp.setType(false);
                                bookFinal.add(bookBtemp);
                            }
                        } catch (Exception e) {
                            System.out.println("bookB--null");
                        }
                    }


                    if (bookFinal != null && bookFinal.size() != 0) {
                        data.setCount(bookFinal.size());
                        data.setBooks(bookFinal);
                    }
                    result.put("isSuccess", 1);  //查寻成功
                    r.put("data", data);
                    r.put("myUserId", me.getUserId());

                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("isSuccess", 0); //查寻不到用户
            }
        }

        r.put("status", result);


        return r;
    }


    /**
     * search
     * 查询书籍-模糊查询
     * url:api.shelf.search
     * data:bookName：名称
     * ownerId：用户id
     * <p>
     * 返回：data: IndexData{
     * 书籍列表books
     * 书籍总数count}
     * status:isSuccess（1-成功）（0-失败）
     */

    @ResponseBody
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params) {
        System.out.println("/search");
        System.out.println(params);
        IndexData data = new IndexData();
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();


        List<book> bookM = new ArrayList<>();
        List<book> bookB = new ArrayList<>();
        List<book> bookFinal = new ArrayList<>();


        Integer ownerId = Integer.valueOf(params.get("ownerId").toString()); //ownerId
        String bookName = params.get("bookName").toString();

        if (bookName != null && ownerId != null) {

            param.put("bookName", bookName);
            param.put("ownerId", ownerId);

            try {
                bookM = tUserBookService.ownershelf(param);

                for (book bookMtemp : bookM) {
                    BookInf bookInf = tBookService.selectByPrimaryKey(bookMtemp.getIsbn());
                    bookMtemp.setAuthor(bookInf.getAuthor());
                    bookMtemp.setBookName(bookInf.getBookName());
                    bookMtemp.setPicPath(bookInf.getPicPath());
                    bookMtemp.setType(true);
                    bookFinal.add(bookMtemp);
                }
            } catch (Exception e) {
                System.out.println("bookM--null");
            }

            try {

                bookB = tUserBookService.borrowShelf(param);
                for (book bookBtemp : bookB) {
                    BookInf bookInf = tBookService.selectByPrimaryKey(bookBtemp.getIsbn());
                    bookBtemp.setAuthor(bookInf.getAuthor());
                    bookBtemp.setBookName(bookInf.getBookName());
                    bookBtemp.setPicPath(bookInf.getPicPath());
                    bookBtemp.setType(false);
                    bookFinal.add(bookBtemp);
                }
            } catch (Exception e) {
                System.out.println("bookB--null");
            }

            if (bookFinal != null && bookFinal.size() != 0) {
                data.setCount(bookFinal.size());
                data.setBooks(bookFinal);
                result.put("isSuccess", 1);  //查寻成功
                r.put("data", data);
            } else {
                result.put("isSuccess", 0);  //无书
            }

        } else {
            result.put("isSuccess", 0); //查寻失败
        }

        r.put("status", result);


        return r;
    }


    /**
     * findBookList-list  加载书籍列表
     * params：isbn ,access_token
     * res: data:booklist
     * status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/shelfIndex/findBookList")
    public R findBookList(@RequestParam Map<String, Object> params) {
        System.out.println("/shelfIndex/findBookList");
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try {
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            String isbn = params.get("isbn").toString();
            String mode = params.get("mode").toString();

            param.put("openId", openId);
            param.put("isbn", isbn);


            if (openId != null) {
                UserInf me = tMemberService.listPublic(param).get(0); //查询用户信息（根据openId 查询 userId）
                if (me != null) {
                    Integer userId = me.getUserId();

                    List<Map<String, Object>> bookList = new ArrayList<>();
                    //查询借阅的书籍和拥有的书籍
                    if (mode.equals("owner") || mode.equals("other")) {
                        if (mode.equals("owner")) {
                            param.put("ownerId", userId);
                        } else {
                            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
                            param.put("ownerId", ownerId);
                        }
                        bookList = tUserBookService.findOwnerBookList(param);
                        for (Map<String, Object> temp : bookList) {
                            temp.put("sortTime", temp.get("sortTime").toString().split(" ")[0]);

                            if (Integer.valueOf(temp.get("privacy").toString()).equals(0)) {
                                //公开
                                String type = "";
                                type = transferUtil.borrowState(Integer.valueOf(temp.get("borrowState").toString()));
                                temp.put("type", type);
                            } else {
                                temp.put("type", transferUtil.privacy(Integer.valueOf(temp.get("privacy").toString())));
                            }
                        }
                    } else if (mode.equals("borrow")) {
                        param.put("ownerId", userId);
                        bookList = tUserBookService.findBorrowBookList(param);
                        for (Map<String, Object> temp : bookList) {
                            temp.put("sortTime", temp.get("sortTime").toString().split(" ")[0]);
                            String type = " ";
                            type = transferUtil.usrBorrowState(Integer.valueOf(temp.get("usrBorrowState").toString()));
                            temp.put("type", type);
                        }

                    }
                    r.put("data", bookList);
                    result.put("isSuccess", 1);
                    r.put("status", result);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", 0);
            r.put("status", result);
            System.out.println(("无数据"));
        }
        return r;
    }


    /**
     * asecond/bdself 详情界面
     * params:bookId
     * res ：status:is_exist
     * userBook
     * bookInf
     * userInfo{ nickName,avatarUrl }
     **/
    @ResponseBody
    @GetMapping({"/asecond/bdself"})
    public R queryBook(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdself");
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        try {
            Integer bookId = Integer.valueOf(params.get("bookId").toString());
            String isbn = params.get("isbn").toString();
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());


            //根据bookid 查询 isbn
            rUserBook rUserBookTemp = tUserBookService.selectByPrimaryKey(bookId);
            if (rUserBookTemp != null) {
                param.put("isbn", isbn);
                BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
                if (bookInfTemp != null) {
                    status.put("is_exist", 2); //成功查阅到书籍信息
                    param.put("openId", openId);
                    UserInf userInf = tMemberService.listPublic(param).get(0);
                    rUserBookTemp = transferUtil.setRUserBook(rUserBookTemp);
                    bookInfTemp = transferUtil.setBookInf(bookInfTemp);
                    r.put("userBook", rUserBookTemp);
                    r.put("bookInf", bookInfTemp);
                    r.put("userInfo", userInf);

                } else status.put("is_exist", 1); //查阅到书籍信息失败
            } else
                status.put("is_exist", 0); //bookid 不存在

            r.put("status", status);

        } catch (Exception e) {
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
    public R setPrivacy(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdself/privacy");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        R r = new R();
        try {

            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            param.put("openId", openId);
            Integer bookId = Integer.valueOf(params.get("bookId").toString());
            Integer privacy = Integer.valueOf(params.get("privacy").toString());
            rUserBook searchBookTemp = new rUserBook();
            searchBookTemp.setPrivacy(privacy);
            searchBookTemp.setBookId(bookId);
            if (openId != null && bookId != null && privacy != null) {
                UserInf seMember = tMemberService.listPublic(param).get(0);
                if (seMember != null) {
                    params.put("ownerId", seMember.getUserId());
                    int i = tUserBookService.updateByPrimaryKeySelective(searchBookTemp);
                    if (i == 0) {
                        result.put("save_success", 0); //更新失败
                    } else {
                        result.put("save_success", 1); //更新成功
                    }
                }

            }
            r.put("data", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


    @ResponseBody
    @GetMapping({"asecond/bdself/deleteBook"})
    public R deleteBook(@RequestParam Map<String, Object> params) {
        System.out.println("asecond/bdself/deleteBook");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> type = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            param.put("openId", openId);
            UserInf seMember = tMemberService.listPublic(param).get(0); //查询用户信息（根据openId 查询
            Integer bookId = Integer.valueOf(params.get("bookId").toString());
            param.put("bookId", bookId);
            String isbn = tUserBookService.list(param).get(0).getIsbn();
            param.put("isbn", isbn);

            if (openId != null && bookId != null && seMember != null) {
                int i = tUserBookService.deleteByPrimaryKey(bookId);
                if (i == 0) {
                    result.put("save_success", 0); //删除失败
                } else {
                    result.put("save_success", 1); //删除成功
                    param.put("ownerId", seMember.getUserId());
                    //查询借阅的书籍和拥有的书籍
                    try {
                        List<Map<String, Object>> bookM = tUserBookService.findOwnerBookList(param);
                        if (bookM != null && bookM.size() > 1) {
                            type.put("isList", 1); //否则跳转到list
                        } else {
                            type.put("isList", 0); //无书籍或者只有一本书，跳转到首页
                        }

                    } catch (Exception e) {
                        type.put("isList", 0); //无书籍或者只有一本书，跳转到首页
                        System.out.println("null");
                    }
                    r.put("type", type);
                }


            }
            r.put("data", result);

        } catch (Exception e) {
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
    public R apply(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/privacy");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            String isbn = params.get("isbn").toString();
            String ownerId = params.get("userId").toString();
            String mode = params.get("mode").toString();
            Map<String, Object> param = new HashMap<>();
            params.put("openId", openId);
            param.put("ownerId", ownerId);
            param.put("isbn", isbn);
            if (openId != null && ownerId != null && isbn != null) {
                UserInf seMember = tMemberService.selectOne(params);
                rUserBook mrUserBook = tUserBookService.SelectByIsbn(param);
                if (seMember != null && mrUserBook != null) {
                    Integer userId = seMember.getUserId();
                    Integer bookId = mrUserBook.getBookId();

                    param.clear();
                    param.put("userId", userId);
                    param.put("bookId", bookId);
                    if (mode.equals("apply")) {
                        Integer usrBorrowState = Integer.valueOf(1);
                        param.put("usrBorrowState", usrBorrowState);
                        int i = tBookUserBorrowService.updateState(param);
                        if (i == 0) {
                            result.put("save_success", 0); //更新失败
                        } else {
                            result.put("save_success", 1); //更新成功
                        }
                    } else {
                        Integer usrBorrowState = Integer.valueOf(0);
                        param.put("usrBorrowState", usrBorrowState);
                        int i = tBookUserBorrowService.updateState(param);
                        if (i == 0) {
                            result.put("save_success", 0); //更新失败
                        } else {
                            result.put("save_success", 1); //更新成功
                        }
                    }

                }

            }
            r.put("data", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


    /**
     * asecond/otherself 详情界面
     * params:id ,mode ,isbn ,fid
     * res ：status:is_exist
     * userBook
     * bookInf
     * userInfo{ nickName,avatarUrl }
     * borrow(other)
     **/
    @ResponseBody
    @GetMapping({"/asecond/bdother"})
    public R queryOtherBook(@RequestParam Map<String, Object> params) {
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        System.out.println("/asecond/bdother");
        try {
            Integer borrowId = Integer.valueOf(params.get("borrowId").toString());
            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
            String isbn = params.get("isbn").toString();

            rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.selectByPrimaryKey(borrowId);
            param.put("isbn", isbn);
            BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
            param.clear();
            rUserBook rUserBook = tUserBookService.selectByPrimaryKey(rBookUserBorrowTemp.getBookId());
            param.put("userId", rUserBook.getOwnerId());
            UserInf fuserInf = tMemberService.listPublic(param).get(0);
            rBookUserBorrowTemp.setUsrBorrowStateS(transferUtil.usrBorrowState(rBookUserBorrowTemp.getUsrBorrowState()));
            bookInfTemp = transferUtil.setBookInf(bookInfTemp);
            param.clear();
            param.put("fid", ownerId);
            param.put("mid", rUserBook.getOwnerId());
            try {
                FriendInf friendInf = tFriendService.list3(param).get(0);
                if (friendInf != null) {
                    if(friendInf.getUid()==ownerId){
                        if(friendInf.getAliasu()!=null&&friendInf.getAliasu().length()!=0){
                            fuserInf.setNickName(friendInf.getAliasu());
                        }
                    }else{
                        if(friendInf.getAliasf()!=null&&friendInf.getAliasf().length()!=0){
                            fuserInf.setNickName(friendInf.getAliasf());
                        }
                    }
                    r.put("flag", true);
                } else {
                    r.put("flag", false);
                }
            } catch (Exception e) {
                r.put("flag", false);
            }
            r.put("borrow", true);
            r.put("userBook", rBookUserBorrowTemp);
            r.put("bookInf", bookInfTemp);
            r.put("fuserInfo", fuserInf);
            status.put("is_exist", 1);
        } catch (Exception e) {
            status.put("is_exist", 0);
        }
        r.put("status", status);
        return r;
    }


    /**
     * asecond/otherBook 详情界面
     * params:bookId
     * res ：status:is_exist
     * userBook
     * bookInf
     * userInfo{ nickName,avatarUrl }
     **/
    @ResponseBody
    @GetMapping({"/asecond/otherBook"})
    public R queryBookOther(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/otherBook");
        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        try {
            Integer bookId = Integer.valueOf(params.get("bookId").toString());
            String isbn = params.get("isbn").toString();
            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

            //根据bookid 查询 isbn
            rUserBook rUserBookTemp = tUserBookService.selectByPrimaryKey(bookId);
            if (rUserBookTemp != null) {
                param.put("isbn", isbn);
                BookInf bookInfTemp = tBookService.findbyIsbn(param).get(0);
                if (bookInfTemp != null) {
                    param.put("openId", openId);
                    UserInf userInf = tMemberService.listPublic(param).get(0);
                    rUserBookTemp = transferUtil.setRUserBook(rUserBookTemp);
                    bookInfTemp = transferUtil.setBookInf(bookInfTemp);
                    param.clear();
                    param.put("userId", ownerId);
                    UserInf fuserInf = tMemberService.listPublic(param).get(0);
                    param.clear();
                    param.put("bookId", bookId);
                    param.put("userId", userInf.getUserId());
                    try {

                        rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.listCurrent(param);
                        rBookUserBorrowTemp.setUsrBorrowStateS(transferUtil.usrBorrowState(rBookUserBorrowTemp.getUsrBorrowState()));

                        if (rBookUserBorrowTemp != null) {
                            System.out.println("notnull");
                            r.put("userBook", rBookUserBorrowTemp);
                            r.put("borrow", true);
                        } else {
                            System.out.println("null");
                            r.put("userBook", rUserBookTemp);
                            r.put("borrow", false);
                        }
                    } catch (Exception e) {
                        System.out.println("null1");
                        r.put("userBook", rUserBookTemp);
                        r.put("borrow", false);
                    }

                    param.clear();
                    param.put("fid", ownerId);
                    param.put("mid", userInf.getUserId());
                    try {
                        FriendInf friendInf = tFriendService.list3(param).get(0);
                        if (friendInf != null) {
                            if(friendInf.getUid()== userInf.getUserId()){
                                if(friendInf.getAliasu()!=null&&friendInf.getAliasu().length()!=0){
                                    fuserInf.setNickName(friendInf.getAliasu());
                                }
                            }else{
                                if(friendInf.getAliasf()!=null&&friendInf.getAliasf().length()!=0){
                                    fuserInf.setNickName(friendInf.getAliasf());
                                }
                            }
                            r.put("flag", true);
                        } else {
                            r.put("flag", false);
                        }
                    } catch (Exception e) {
                        r.put("flag", false);
                    }
                    r.put("fuserInfo", fuserInf);
                    r.put("bookInf", bookInfTemp);

                    r.put("userId", userInf.getUserId());
                    status.put("is_exist", 1);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            status.put("is_exist", 0);
        }

        r.put("status", status);
        return r;
    }


    /**
     * othershelf  加载书架
     * params：fid
     * res: data:IndexData
     * status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/othershelf")
    public R Findex(@RequestParam Map<String, Object> params) {
        System.out.println("/othershelf");
        IndexData data = new IndexData();
        R r = new R();

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        List<CategoryInf> cat_list = new ArrayList<>();
        List<book> bookFinal = new ArrayList<>();

        Integer bCatgId = Integer.valueOf(params.get("bCatgId").toString()); //分类
        Integer fid = Integer.valueOf(params.get("fid").toString());
        param.put("ownerId", fid);
        param.put("privacy", 0);

        if (bCatgId != 0) {
            //所有分类
            param.put("bCatgId", bCatgId);
        }
        try {
            //查询所有分类
            cat_list = tCateService.list();

            //添加分类-所有
            CategoryInf categoryInfTemp = new CategoryInf();
            categoryInfTemp.setCatgId(0);
            categoryInfTemp.setCatgName("所有");
            cat_list.add(0, categoryInfTemp);
            data.setCat_list(cat_list);

            List<book> bookM = new ArrayList<>();

            bookM = tUserBookService.ownershelf(param);

            for (book bookMtemp : bookM) {
                BookInf bookInf = tBookService.selectByPrimaryKey(bookMtemp.getIsbn());
                bookMtemp.setAuthor(bookInf.getAuthor());
                bookMtemp.setBookName(bookInf.getBookName());
                bookMtemp.setPicPath(bookInf.getPicPath());
                bookMtemp.setType(false);
                bookFinal.add(bookMtemp);
            }

            if (bookFinal != null && bookFinal.size() != 0) {
                data.setCount(bookFinal.size());
                data.setBooks(bookFinal);
            }
            result.put("isSuccess", 1);  //查寻成功
            r.put("data", data);


        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", 0); //查寻不到用户
        }
        r.put("status", result);
        return r;
    }


    /**
     * othershelf-list  加载书籍列表
     * params：isbn ,access_token
     * res: data:booklist
     * status:isSuccess
     **/

    @ResponseBody
    @GetMapping("/othershelf/list")
    public R findOtherList(@RequestParam Map<String, Object> params) {
        System.out.println("/othershelf/list");
        R r = new R();
        try {
            Integer fid = Integer.valueOf(params.get("fid").toString());
            String isbn = params.get("isbn").toString();
            Map<String, Object> result = new HashMap<>();

            if (fid != null) {
                Map<String, Object> param = new HashMap<>();
                param.put("userId", fid);
                param.put("ownerId", fid);
                param.put("isbn", isbn);
                try {

                    UserInf seMember = tMemberService.listPublic(param).get(0); //查询用户信息（fid (userId)查询）
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
            } else
                result.put("isSuccess", -1);  //fid == null
            r.put("status", result);


        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


}
