package bysj.Controller.Degree;

import bysj.Daomain.Degree;
import bysj.Service.DegreeService;
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

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //    GET, http://localhost:8080/degree.ctl 查询id=1的学位
    //    GET, http://localhost:8080/degree.ctl 查询所有的学位
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                //返回所有的数据
                responseDegrees(response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("列表失败，数据库操作异常");
            } catch(Exception e){
                response.getWriter().println("列表失败，网络异常");
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                //读取id的那一行数据
                responseDegree(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("查找失败，数据库操作异常");
            } catch(Exception e){
                e.printStackTrace();
                response.getWriter().println("查找失败，网络异常");
            }
        }
    }

    //    POST, http://localhost:8080/degree.ctl 增加学位
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得request对象和获得参数的字符串
        String degree_json= JSONUtil.getJSON(request);
        //将JSON字串转换为Degree对象
        Degree degreeToAdd= JSON.parseObject(degree_json,Degree.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加Degree对象
            boolean add=DegreeService.getInstance().add(degreeToAdd);
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
            resp.put("message", "添加失败，网络异常");
        }
        //响应到客户端
        response.getWriter().println(resp);
    }

    //    DELETE, http://localhost:8080/degree.ctl?id=1 删除id=1的学位
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从客户端读取参数id
        String id_str=request.getParameter("id");
        //将字符串转换为int类型
        int id=Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //删除
            boolean delete=DegreeService.getInstance().delete(id);
            if(delete){
                //加入将要回应的数据信息
                resp.put("message", "成功删除");
            }else{
                resp.put("message","删除失败");
            }
        }catch (SQLException e){
            e.printStackTrace();
            resp.put("message", "删除失败，数据库操作异常");
        } catch(Exception e){
            e.printStackTrace();
            resp.put("message", "删除失败，网络异常");
        }
        response.getWriter().println(resp);
    }

    //PUT, http://localhost:8080/degree.ctl 修改学位
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Degree对象
        try {
            boolean update=DegreeService.getInstance().update(degreeToAdd);
            if(update){
                //加入数据信息
                message.put("message", "更新成功");
            }else{
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

    //响应一个学位对象
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        //响应
        response.getWriter().println(degree_json);
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);
        //响应
        response.getWriter().println(degrees_json);
    }
}
