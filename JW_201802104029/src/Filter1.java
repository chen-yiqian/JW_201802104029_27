import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;

@WebFilter(filterName = "Filter1",urlPatterns = "/*")
public class Filter1 implements Filter {
    public void destroy() {
    }
    //重写Filter中声明的方法
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //将ServletRequest强制转换HttpServletRequest
        HttpServletRequest request=(HttpServletRequest)req;
        //定义请求路径（HttpServletRequest接口中有getRequestURI()方法）
        String path=request.getRequestURI();
        //创建Calenda对象
        Calendar cal = Calendar.getInstance();
        //定义时间日期
        String time = cal.get(Calendar.YEAR) + " 年 " +
                (cal.get(Calendar.MONTH) + 1) + " 月 " +
                cal.get(Calendar.DATE) + "日" +
                cal.get(Calendar.HOUR_OF_DAY) + ": " +
                cal.get(Calendar.MINUTE);
        System.out.println("Fi;ter1 begin");
        System.out.println(path+" @ "+time);
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter1 ends");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
