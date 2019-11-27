package bysj.Controller.School;

import bysj.Daomain.ProfTitle;
import bysj.Daomain.School;
import bysj.Daomain.Teacher;
import bysj.Service.ProfTitleService;
import bysj.Service.SchoolService;
import bysj.Service.TeacherService;
import bysj.Util.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.protocol.x.XMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //    GET, http://localhost:8080/school.ctl 查询id=1的学院
    //    GET, http://localhost:8080/school.ctl 查询所有的学院
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
        if(id_str == null){
            try {
                responseSchools(response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("列表失败，数据库操作异常");
            } catch(Exception e){
                response.getWriter().println("列表失败，网络异常");
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseSchool(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("查找失败，数据库操作异常");
            } catch(Exception e){
                response.getWriter().println("查找失败，网络异常");
            }
        }
    }

    //    POST, http://localhost:8080/school.ctl 增加学院
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从请求对象中获得JSON类型的数据
        String school_json= JSONUtil.getJSON(request);
        //将JSON字串转换为School对象
        School schoolToAdd= JSON.parseObject(school_json,School.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加School对象
            boolean add=SchoolService.getInstance().add(schoolToAdd);
            if (add){
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

    //    DELETE, http://localhost:8080/school.ctl?id=1 删除id=1的学院
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从客户端读取参数id
        String id_str=request.getParameter("id");
        //将字符串转换为int类型
        int id=Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //删除
            boolean delete=SchoolService.getInstance().delete(id);
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

    //PUT, http://localhost:8080/school.ctl 修改学院
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加School对象
        try {
            boolean update=SchoolService.getInstance().update(schoolToAdd);
            if(update){
                //加入数据信息
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

    //响应一个学院对象
    private void responseSchool(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);
        //响应
        response.getWriter().println(school_json);
    }

    //响应所有学院对象
    private void responseSchools(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools);
        //响应
        response.getWriter().println(schools_json);
    }
}
