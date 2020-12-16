package dyz.ssmUnion.exceptionResolver;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/13 11:51
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    /**
     * 处理异常业务逻辑，当捕捉到异常时运行这个方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e 捕捉的异常对象，要从它身上获得信息
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mv=new ModelAndView();
        System.out.println("cndijdsjknksdjhkjsdkjhdsjjjjjjjjjjjjjjjjj");
        mv.setViewName("error");
        return mv;
    }
}
