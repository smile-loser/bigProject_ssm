package dyz.ssmUnion.controller;


import dyz.ssmUnion.daomain.Administrators;
import dyz.ssmUnion.daomain.PageBean;
import dyz.ssmUnion.daomain.User;
import dyz.ssmUnion.service.AdministratorsService;
import dyz.ssmUnion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/11 20:39
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    AdministratorsService as;

    @Autowired
    private UserService us;

    /**
     * 增加用户，增加后跳到最后一页
     * @param user
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/addUser")
    public void addUser(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {


        //1.设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        us.addUser(user);
        //2.获得总数，为了跳转到最后一页
        Integer lastPage = us.getTotalPage(new HashMap<String,String[]>());
        response.sendRedirect(request.getContextPath() + "/list.html?currentPage="+lastPage);
    }

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //        验证码图片高/宽
        int width = 100;
        int height = 50;
//        创建验证码对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);/*BufferedImage.TYPE_INT_RGB对象三色*/

//        保存验证码让登录页面提交时能够让服务器识别
        StringBuilder sd = new StringBuilder();
//        美化图片
        //填充粉色背景色
        Graphics g = image.getGraphics();
        g.setColor(Color.pink);
        g.fillRect(0, 0, width, height);/*填充*/
        //画框
        g.setColor(Color.BLUE);
        g.drawRect(0, 0, width - 1, height - 1);
        //随机写四个字母
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random ran = new Random();
        for (int i = 0; i < 4; i++)//打印字母
        {
            int index = ran.nextInt(str.length());//0-n不包括n
            char ch = str.charAt(index);
            sd.append(ch);
            g.drawString(ch + "", width / 5 + width / 5 * i, height / 2 - 3 + i * 5);
        }

//        sd转化字符串设置成session让服务器识别
        String s = sd.toString();
        HttpSession session = request.getSession();
        session.setAttribute("checkCode_session", s);

        for (int i = 0; i < 4; i++) {
            int x1 = ran.nextInt(width);
            int x2 = ran.nextInt(width);
            int y1 = ran.nextInt(width);
            int y2 = ran.nextInt(width);
            g.drawLine(x1, y1, x2, y2);
        }


//        输出图片到页面
        ImageIO.write(image, "jpg", response.getOutputStream());/*jpg是输出这个图片格式*/

    }


    @RequestMapping("/delSelected")
    public void delSelected(HttpServletRequest request, HttpServletResponse response,int currentPage,String name,String address,String email) throws IOException {
        //1.获取所有selectedId
        String[] ids = request.getParameterValues("selectedId");
        if (ids != null) {
            us.delSelectedUsers(ids);
        }
        System.out.println(address);
        //2.决定要跳转到哪页
      /*  Integer currentPage=Integer.parseInt(request.getParameter("currentPage"));*/
        System.out.println(address+name+email);
        int nowPages = us.getTotalPage(null);
        if(currentPage>nowPages)
        {
            currentPage=nowPages;
        }

        //重定向后字符串在浏览器编码不同
        String encoder = "utf-8";


        if((name!=null&&!name.equals("undefined"))||(address!=null&&!address.equals("undefined"))||(email!=null&&!email.equals("undefined")))
        {
            //因为字符&会被URLEncoder转换，所以不能直接转换整个otherParam
            String otherParam="&";
            otherParam=(name!=null&&!name.equals("undefined"))?(otherParam+="name="+URLEncoder.encode(name,encoder)+"&"):otherParam;
            otherParam=(address!=null&!address.equals("undefined"))?(otherParam+="address="+URLEncoder.encode(address,encoder)+"&"):otherParam;
            otherParam=(email!=null&&!email.equals("undefined"))?(otherParam+="email="+URLEncoder.encode(email,encoder)):otherParam;

            response.sendRedirect(request.getContextPath()+"/list.html?currentPage="+currentPage+otherParam);
        }
        else{
            response.sendRedirect(request.getContextPath()+"/list.html?currentPage="+currentPage);
        }
    }


    @RequestMapping("/delUser")
    public void delUser(HttpServletRequest request, HttpServletResponse response,int id,int currentPage,String name,String address,String email) throws IOException {


        us.delUser(id);
        //防止因为删除导致这一页是空的，这种情况只会出现在删除最后一页的元素，这种情况跳到之前最后一页前面一页就好
        Integer nowPages = us.getTotalPage(null);
        if(currentPage>nowPages)
        {
            currentPage=nowPages;
        }
        if(name!=null||address!=null||email!=null)
        {
            String otherParam="&";
            otherParam=(name!=null)?(otherParam+="name="+name+"&"):otherParam;
            otherParam=(address!=null)?(otherParam+="address="+address+"&"):otherParam;
            otherParam=(email!=null)?(otherParam+="email="+email+"&"):otherParam;

            response.sendRedirect(request.getContextPath()+"/list.html?currentPage="+currentPage+otherParam);
        }
        else{
            response.sendRedirect(request.getContextPath()+"/list.html?currentPage="+currentPage);
        }
    }


    @RequestMapping("/findUserByPage")
    public  @ResponseBody PageBean<User> findUserByPage(int currentPage, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("这里是controller list.xml发来申请");

        Map<String, String[]> allParam = request.getParameterMap();/*里面有currentPage,rows如何是通过查询按钮还有值可能为空的name, address,email */
        //防止因为删除导致这一页是空的，这种情况只会出现在删除最后一页的元素，这种情况跳到之前最后一页前面一页就好
        Integer nowPages = us.getTotalPage(null);
        if(currentPage>nowPages)
        {
            currentPage=nowPages;
        }
        return us.findUsersByPage(currentPage, allParam);
    }



    /**
     * 修改user信息时充当中转站
     * @param id
     * @return
     */
    @RequestMapping("/findUserById")
    public @ResponseBody User findUserById(int id)
    {
        return us.findUserById(id);
    }

    @RequestMapping("/updateUser")
    public void updateUser(@RequestBody(required=false) String body, User user,HttpServletRequest request, HttpServletResponse response) throws IOException {
        //有问题 .address
        //没问题  email qq gender age name id

        System.out.println(body);
        us.updateUser(user);
        response.sendRedirect(request.getContextPath()+"/list.html?currentPage=1");
    }

    /**
     * ajax
     * @param ad
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/login")
    public void login(Administrators ad,HttpServletRequest request,HttpServletResponse response,String checkCode) throws IOException {
        //1.设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //判断验证码是否正确
        HttpSession session=request.getSession();

        System.out.println(checkCode);
        System.out.println(session.getAttribute("checkCode_session"));


        String checkCode_session=(String)session.getAttribute("checkCode_session");
        if(checkCode_session.equalsIgnoreCase(checkCode))
        {
            Administrators user=as.findIt(ad);
            if(user==null)
            {
                //给出信号,账户不存在
                response.getWriter().write("账号不存在");
            }
            else{
                if(user.getPassword().equals(ad.getPassword()) )
                {
                    //设置参数跳过拦截
                    session.setAttribute("isSuccessLogin",true);
                    //跳转list页面
                    response.getWriter().write("jump");
                }

                else{
                    //给出信号，密码错误
                    response.getWriter().write("密码错误");
                }
            }
        }
        else{
            response.getWriter().write("验证码错误");
        }


    }

}
