<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工列表</title>
<%
  pageContext.setAttribute("APP_PATH", request.getContextPath());
%>

<!-- web路径：
	不以/开始的相对路径，以当前资源的路径为基准，容易出现问题
	以/开始的相对路径，资源以服务器的路径为标准
	比如(http://localhost:3360):需要加上项目名
	http://localhost:3306/crud
 -->
<link
	href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="${APP_PATH}/static/js/jquery-2.0.0.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script
	src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
	<!-- 搭建显示页面 -->
	<div class="container">
	
		<!-- 标题 -->
		<div class="row">
			<div class="col-md-12">
				<h1>SSM-CRUD</h1>
			</div>
		</div>
		<!-- 按钮 -->
		<div class="row">
			<div class="col-md-4 col-md-offset-8">
				<button class="btn btn-success">新增</button>
				<button class="btn btn-danger">删除</button>
			</div>
		</div>
		
		<!-- 显示表格数据 -->
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover">
					<!-- 表头 -->
					<tr>
						<th>#</th>
						<th>empName</th>
						<th>gender</th>
						<th>email</th>
						<th>deptName</th>
						<th>操作</th>
					</tr>

					<!-- 表格显示内容 -->
					<c:forEach items="${pageInfo.list }" var="emp">
						<tr>
							<th>${emp.empId }</th>
							<th>${emp.emName }</th>
							<th>${emp.gender=="M"?"男":"女" }</th>
							<th>${emp.email }</th>
							<th>${emp.department.deptName }</th>
							<th>
								<button type="button" class="btn btn-primary btn-sm">
									修改 <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</button>
								<button type="button" class="btn btn-danger btn-sm">
									删除 <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
								</button>
							</th>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		
		<!-- 显示分页信息 -->
		<div class="row">
		
			<!-- 分页文字信息 -->
			<div class="col-md-6">当前${pageInfo.pageNum}页,总${pageInfo.pages}页,总${pageInfo.total}条记录</div>
			
			
			<!-- 分页条信息 -->
			<div class="col-md-6">
				<nav aria-label="Page navigation">
				<ul class="pagination">
					<!-- 首页按钮 -->				
					<li><a href="${APP_PATH}/emps?pn=1">首页</a></li>
					<!-- 向前箭头等于请求当前页码减1,且如果有上一页才显示-->
					<c:if test="${pageInfo.hasPreviousPage}">
						<li><a href="${APP_PATH}/emps?pn=${pageInfo.pageNum-1}"
							aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
					</c:if>
					
					<!-- 可供点击的12345页 -->
					<!-- var中的值相当于java中for (Employee e: employees)中的e-->
					<c:forEach items="${pageInfo.navigatepageNums}" var="pageNum">
						<!-- 如果是当前页码就高亮显示并且点击没有连接 -->
						<c:if test="${pageNum==pageInfo.pageNum}">
							<li class="active"><a href="#">${pageNum}</a></li>
						</c:if>
						<!-- 如果不是当前页码就不高亮显示且点击有连接，连接发送一个pn等于按钮数字的 请求-->
						<c:if test="${pageNum!=pageInfo.pageNum}">
							<li><a href="${APP_PATH}/emps?pn=${pageNum}">${pageNum}</a></li>
						</c:if>
					</c:forEach>
					
					<!-- 向后箭头等于请求当前页码加1,如果有下一页才显示 -->
					<c:if test="${pageInfo.hasNextPage}">
						<li><a href="${APP_PATH}/emps?pn=${pageInfo.pageNum+1}"
							aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</c:if>
					
					<!-- 末页 -->
					<li><a href="${APP_PATH}/emps?pn=${pageInfo.pages}">末页</a></li>
				</ul>
				</nav>
			</div>
		</div>
	</div>

</body>
</html>