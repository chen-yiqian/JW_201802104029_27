import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//过滤器名称和请求
@WebFilter(filterName = "Filter0",urlPatterns = "/*")
public class Filter0 implements Filter {
    public void destroy() {
    }
    //重写Filter中声明的方法
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter0 - encodimg begins");
        //将ServletRequest强制转换HttpServletRequest
        HttpServletRequest request=(HttpServletRequest)req;
        //将ServletResponse强制转换HttpServletResponse
        HttpServletResponse response=(HttpServletResponse) resp;
        //定义请求的相对路径
        String path=request.getRequestURI();
        //if语句判断是否包含/login
        if(path.contains("/login")){
            System.out.println("请求包含/login，故不处理编码");
        }else {
            String method=request.getMethod();
            if(method.equals("PUT")||method.equals("POST")){
                request.setCharacterEncoding("UTF-8");
            }
            response.setContentType("text/html;charset =UTF-8");
        }
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter0 - encoding ends");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
