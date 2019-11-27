package bysj.Dao;

import bysj.Daomain.Department;
import bysj.Daomain.School;
import bysj.Service.SchoolService;
import bysj.Util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class DepartmentDao {
	private static DepartmentDao departmentDao = new DepartmentDao();

	public static DepartmentDao getInstance() {
		return departmentDao;
	}

	public Collection<Department> findAll() throws SQLException {
		//创建一个HashSet对象，并把它加到set
		Set departments = new HashSet<Department>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from department");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()) {
			School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
			//获得数据库的信息，并用于在网页中显示
			Department department = new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					school);
			//添加到集合中
			departments.add(department);
		}
		return departments;
	}

	public Department find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteDepartment_sql = "SELECT * FROM department where id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDepartment_sql);
		//为预编译语句赋值
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		Department department = null;
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()) {
			School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
			department = new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					school);
		}
		//关闭资源
		JdbcHelper.close(resultSet, preparedStatement, connection);
		return department;
	}

	public boolean update(Department department) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		PreparedStatement preparedStatement = connection.prepareStatement("update department set description = ?,no=?,remarks=?,school_id=? where id=?");
		preparedStatement.setString(1, department.getDescription());
		preparedStatement.setString(2, department.getNo());
		preparedStatement.setString(3, department.getRemarks());
		preparedStatement.setInt(4, department.getSchool().getId());
		preparedStatement.setInt(5, department.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement, connection);
		System.out.println("修改了" + affectedRowNum + "条");
		return affectedRowNum > 0;
	}

	public boolean add(Department department) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDepartment_sql = "insert into department(no,description,remarks,school_id) values " + "(?,?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDepartment_sql);
		//为预编译参数赋值
		pstmt.setString(1, department.getNo());
		pstmt.setString(2, department.getDescription());
		pstmt.setString(3, department.getRemarks());
		pstmt.setInt(4, department.getSchool().getId());
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		//关闭
		JdbcHelper.close(pstmt, connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteDepartment_sql = "DELETE FROM department WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDepartment_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1, id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement, connection);
		return affectedRows > 0;
	}
}

