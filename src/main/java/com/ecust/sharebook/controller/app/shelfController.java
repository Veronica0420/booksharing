package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.shelf.CatgBook;
import com.ecust.sharebook.pojo.util.shelf.IndexData;
import com.ecust.sharebook.pojo.util.shelf.book;
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

    @Autowired
    private TMessageService tMessageService;
    @Autowired
    private optService toptService;


    /**
     * shelfIndex  加载书架
     * params：access_token
     * res: data:IndexData 1
     * status:isSuccess 1 0
     **/

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

            param.put("openId", openId);

            try {
                UserInf me = tMemberService.listPublic(param).get(0); //查询用户信息（根据openId 查询 userId）
                if (me != null) {
                    Integer ownerId = me.getUserId();
                    param.clear();

                    if (bCatgId != 0) {
                        //所有分类
                        param.put("bCatgId", bCatgId);
                    }

                    param.put("ownerId", ownerId);
                    //查询所有分类
                    cat_list = tCateService.list();

                    //添加分类-所有
                    CategoryInf categoryInfTemp = new CategoryInf();
                    categoryInfTemp.setCatgId(0);
                    categoryInfTemp.setCatgName("所有");
                    cat_list.add(0, categoryInfTemp);
                    data.setCat_list(cat_list);

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
                    r.put("myUserId",me.getUserId());

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
                    if (mode.equals("owner")||mode.equals("other")) {
                        if(mode.equals("owner")){
                            param.put("ownerId", userId);
                        }else {
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
                    } else if(mode.equals("borrow")){
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
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            Integer borrowId = Integer.valueOf(params.get("borrowId").toString());
            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
            String isbn = params.get("isbn").toString();

            rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.selectByPrimaryKey(borrowId);
            BookInf bookInfTemp = tBookService.selectByPrimaryKey(isbn);
            param.put("openId", openId);
            UserInf userInf = tMemberService.listPublic(param).get(0);
            param.clear();
            rUserBook rUserBook = tUserBookService.selectByPrimaryKey(rBookUserBorrowTemp.getBookId());
            param.put("userId", rUserBook.getOwnerId());
            UserInf fuserInf = tMemberService.listPublic(param).get(0);
            rBookUserBorrowTemp = transferUtil.setBorrow(rBookUserBorrowTemp);
            bookInfTemp = transferUtil.setBookInf(bookInfTemp);
            r.put("userBook", rBookUserBorrowTemp);
            r.put("bookInf", bookInfTemp);
            r.put("userInfo", userInf);
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
                    status.put("is_exist", 2); //成功查阅到书籍信息
                    param.put("openId", openId);
                    UserInf userInf = tMemberService.listPublic(param).get(0);
                    rUserBookTemp = transferUtil.setRUserBook(rUserBookTemp);
                    bookInfTemp = transferUtil.setBookInf(bookInfTemp);
                    param.clear();
                    param.put("userId", ownerId);
                    UserInf fuserInf = tMemberService.listPublic(param).get(0);
                    r.put("fuserInfo", fuserInf);
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


    /**
     * asecond/bdother/opt  申请借阅,取消申请,归还
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/opt"})
    public R opt(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdother/opt");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            //id值   apply :bookId ; other:  borrowId
            Integer id = Integer.valueOf(params.get("id").toString());
            String mode = params.get("mode").toString();
            String time = params.get("time").toString();
            Integer fid = Integer.valueOf(params.get("fid").toString());

            Map<String, Object> param = new HashMap<>();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(time);

            param.put("openId", openId);
            UserInf seMember = tMemberService.listPublic(param).get(0);
            param.clear();

            if (mode.equals("apply")) {

                //insert rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBookId(id);
                rBookUserBorrows.setBorrowTime(date);
                rBookUserBorrows.setUserId(seMember.getUserId());
                //update rUserBook
                rUserBook rUserBooks = new rUserBook();
                rUserBooks.setBookId(id);
                rUserBooks.setBorrowState(1); //待处理

                //insert message
                MessageInf message = new MessageInf();
                message.setmType(0);//申请
                message.setSenderId(seMember.getUserId());  //我
                message.setReceiverId(fid);
                message.setDateTime(date);

                Boolean re = toptService.appplyBook(rBookUserBorrows, rUserBooks, message);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }


            } else if (mode.equals("cancel")) { //取消申请

                //update rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBorrowId(id);
                rBookUserBorrows.setUsrBorrowState(5);//取消申请


                Boolean re = toptService.cancelApply(rBookUserBorrows);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }

            } else {
                //return

                //update rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBorrowId(id);
                rBookUserBorrows.setUsrBorrowState(3);//归还中

                //insert message
                MessageInf message = new MessageInf();
                message.setmType(1);//归还
                message.setSenderId(seMember.getUserId());  //我
                message.setReceiverId(fid);
                message.setDateTime(date);
                message.setmBorrowId(id);

                Boolean re = toptService.returnBook(rBookUserBorrows, message);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }


            }


            r.put("data", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


}
