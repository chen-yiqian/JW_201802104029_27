package bysj.Dao;

import bysj.Daomain.Degree;
import bysj.Util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {
	private static DegreeDao degreeDao= new DegreeDao();
	public static DegreeDao getInstance(){
		return degreeDao;
	}

	public Degree find(Integer id)throws SQLException{
		//定义Degree类型的变量
		Degree degree = null;
		//获得连接对象
		Connection connection= JdbcHelper.getConn();
		//创建sql语句
		String finddegree_sql="select * from degree where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement=connection.prepareStatement(finddegree_sql);
		//为预编译语句赋值
		preparedStatement.setInt(1,id);
		//创建ResultSet对象，执行预编译语句
		ResultSet resultSet=preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if(resultSet.next()){
			//获得数据库的信息
		    degree=new Degree(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
		//关闭
		JdbcHelper.close(preparedStatement,connection);
		return degree;
	}

	public Collection<Degree> findAll() throws SQLException {
		//创建一个HashSet对象，并把它加到set
		Set degrees=new HashSet<Degree>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet= statement.executeQuery("select * from degree");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()){
			//获得数据库的信息，并用于在网页中显示
			Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//添加到集合中
			degrees.add(degree);
		}
		return degrees;
	}

	public boolean update(Degree degree) throws SQLException {
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement preparedStatement=connection.prepareStatement("update degree set description =?,no=?,remarks=? where id=?");
		//为预编译的语句参数赋值
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		//执行预编译对象的executeUpdate()方法，获取修改记录的行数
		int affectedRowNum=preparedStatement.executeUpdate();
		//关闭
		JdbcHelper.close(preparedStatement,connection);
        System.out.println("修改了"+affectedRowNum+"条");
		return affectedRowNum>0;
	}

	public boolean add(Degree degree) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//添加预编译语句
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO degree (no,description,remarks) VALUES (?,?,?)");
		//为预编译语句赋值
		preparedStatement.setString(1,degree.getNo());
		preparedStatement.setString(2,degree.getDescription());
		preparedStatement.setString(3,degree.getRemarks());
		//执行预编译对象的executeUpdate()方法，获得增加行数
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum + "条记录");
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String deleteDegree_sql="DELETE FROM degree WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt=connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate()方法，获得删除行数
		int affectedRowNum=pstmt.executeUpdate();
		//关闭
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}
}

