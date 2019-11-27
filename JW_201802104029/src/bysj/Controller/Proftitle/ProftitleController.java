package bysj.Controller.Proftitle;

import bysj.Daomain.Degree;
import bysj.Daomain.Department;
import bysj.Daomain.ProfTitle;
import bysj.Service.DegreeService;
import bysj.Service.DepartmentService;
import bysj.Service.ProfTitleService;
import bysj.Util.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/proftitle.ctl")
public class ProftitleController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //    GET, http://localhost:8080/proftitle.ctl 查询id=1的职称
    //    GET, http://localhost:8080/proftitle.ctl 查询所有的职称
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有职称对象，否则响应id指定的职称对象
        if(id_str == null){
            try {
                //向客户端响应所有信息
                responseProftiles(response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("列表失败，数据库操作异常");
            } catch(Exception e){
                e.printStackTrace();
                response.getWriter().println("列表失败，网络异常");
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                //响应为id的信息
                responseProftitle(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("查找失败，数据库操作异常");
            } catch(Exception e){
                response.getWriter().println("查找失败，网络异常");
            }
        }
    }

    //    POST, http://localhost:8080/proftitle.ctl 增加职称
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从请求对象中获得JSON类型的数据
        String proftitle_json= JSONUtil.getJSON(request);
        //将JSON字串转换为ProfTitle对象
        ProfTitle profTitleToAdd= JSON.parseObject(proftitle_json,ProfTitle.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加Profitile对象
            boolean add=ProfTitleService.getInstance().add(profTitleToAdd);
            if(add){
                //加入将要回应的数据信息
                resp.put("message", "添加成功");
            }else {
                resp.put("message","添加失败");
            }
        }catch(SQLException e){
            e.printStackTrace();
            resp.put("message","添加失败，数据库操作异常");
        } catch(Exception e){
            e.printStackTrace();
            resp.put("message", "添加失败，网络异常");
        }
        //响应到客户端
        response.getWriter().println(resp);
    }

    //    DELETE, http://localhost:8080/proftitle.ctl?id=1 删除id=1的职称
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从客户端读取参数id
        String id_str=request.getParameter("id");
        //将字符串转换为int类型
        int id=Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //删除
            boolean delete=ProfTitleService.getInstance().delete(id);
            if(delete){
                //响应
                resp.put("message","成功删除");
            }else {
                resp.put("message","删除失败");
            }
        }catch (SQLException e){
            e.printStackTrace();
            resp.put("message","删除失败，数据库操作异常");
        } catch(Exception e){
            e.printStackTrace();
            resp.put("message","删除失败，网络异常");
        }
        response.getWriter().println(resp);
    }

    //PUT, http://localhost:8080/proftitle.ctl 修改职称
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String proftitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Proftitle对象
        ProfTitle proftitleToAdd = JSON.parseObject(proftitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Proftitle对象
        try {
            boolean update=ProfTitleService.getInstance().update(proftitleToAdd);
            if(update){
                //加入数据信息
                message.put("statusCode", "200");
                message.put("message", "更新成功");
            }else {
                message.put("message","更新失败");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "更新失败，数据库操作异常");
        } catch(Exception e){
            e.printStackTrace();
            message.put("message", "更新失败，网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    //响应一个proftitle对象
    private void responseProftitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找proftitle
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String proftitle_json = JSON.toJSONString(profTitle);
        //响应
        response.getWriter().println(proftitle_json);
    }

    //响应所有职称对象
    private void responseProftiles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有系
        Collection<ProfTitle> proftitles = ProfTitleService.getInstance().findAll();
        String proftitles_json = JSON.toJSONString(proftitles);
        //响应
        response.getWriter().println(proftitles_json);
    }
}
