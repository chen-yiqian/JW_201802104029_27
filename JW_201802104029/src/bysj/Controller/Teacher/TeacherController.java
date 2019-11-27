package bysj.Controller.Teacher;

import bysj.Daomain.Teacher;
import bysj.Daomain.Teacher;
import bysj.Service.TeacherService;
import bysj.Service.TeacherService;
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

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //    GET, http://localhost:8080/teacher.ctl 查询id=1的教师
    //    GET, http://localhost:8080/teacher.ctl 查询所有的教师
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有职称对象，否则响应id指定的职称对象
        if(id_str == null){
            try {
                //向客户端响应所有信息
                responseTeachers(response);
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
                //响应id的那一行数据
                responseTeacher(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("查找失败，数据库操作异常");
            } catch(Exception e){
                e.printStackTrace();
                response.getWriter().println("查找失败，网络异常");
            }
        }
    }

    //    POST, http://localhost:8080/teacher.ctl 增加教师
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从请求中获得JSON类型的数据
        String teacher_json= JSONUtil.getJSON(request);
        //将JSON字串转换为Teacher对象
        Teacher teacherToAdd=JSON.parseObject(teacher_json,Teacher.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加Teacher对象
            boolean add=TeacherService.getInstance().add(teacherToAdd);
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

    //    DELETE, http://localhost:8080/teacher.ctl?id=1 删除id=1的教师
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从客户端读取参数id
        String id_str=request.getParameter("id");
        //将字符串转换为int类型
        int id=Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            boolean delete=TeacherService.getInstance().delete(id);
            if(delete){
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

    //PUT, http://localhost:8080/teacher.ctl 修改教师
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Teacher对象
        try {
            boolean update=TeacherService.getInstance().update(teacherToAdd);
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

    //响应一个老师对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找老师
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        //响应
        response.getWriter().println(teacher_json);
    }

    //响应所有老师对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有老师
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers);
        //响应
        response.getWriter().println(teachers_json);
    }
}
