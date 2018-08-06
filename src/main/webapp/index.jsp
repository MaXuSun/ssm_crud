<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
				<table class="table table-hover" id="emps_table">
					<!-- 表头 -->
					<thead>
						<tr>
							<th>#</th>
							<th>empName</th>
							<th>gender</th>
							<th>email</th>
							<th>deptName</th>
							<th>操作</th>
						</tr>
					</thead>

					<!-- 表格显示内容 -->
					<tbody>
						
					</tbody>
				</table>
			</div>
		</div>

		<!-- 显示分页信息 -->
		<div class="row">

			<!-- 分页文字信息 -->
			<div class="col-md-6" id="page_info_area"></div>


			<!-- 分页条信息 -->
			<div class="col-md-6" id="page_nav_area"></div>
		</div>
	</div>
	<script type="text/javascript">
		//1.页面加载完成后，直接发送一个ajax请求，要到分页数据
		$(function() {
			//去首页
			$.ajax({
				url : "${APP_PATH}/emps",
				date : "pn=10",
				type : "get",
				success : function(result) {
					//console.log(result);
					//1.解析并显示员工数据
					build_emps_table(result)
					//2.解析并显示分页信息
					build_page_info(result);
					//3.解析显示分页条数据
					build_page_nav(result)
				}
			});
		});
		
		function to_page(pn){
			$.ajax({
				url : "${APP_PATH}/emps",
				data : "pn=" + pn,
				type : "get",
				success : function(result) {
					//console.log(result);
					//1.解析并显示员工数据
					build_emps_table(result)
					//2.解析并显示分页信息
					build_page_info(result);
					//3.解析显示分页条数据
					build_page_nav(result)
				}
			});
		}
		
		function build_emps_table(result) {
			//首先清空之前缓存的数据
			$("#emps_table tbody").empty();
			
			var emps = result.extend.pageInfo.list;
			$.each(emps, function(index, item) {
				var empIdTd = $("<td></td>").append(item.empId);
				var empNameTd = $("<td></td>").append(item.emName);
				var genderTd = $("<td></td>").append(item.gender == "M"?"男":"女");
				var emailTd = $("<td></td>").append(item.email);
				var deptNameTd = $("<td></td>").append(item.department.deptName);
				/**
					<button type="button" class="btn btn-primary btn-sm">
									修改 <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</button>
				*/
				var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm")
					.append($("<span></span>").addClass("glyphicon glyphicon-pencil"))
					.append("编辑");
				var delBtn = $("<button></button>").addClass("btn btn-danger btn-sm")
				.append($("<span></span>").addClass("glyphicon glyphicon-trash"))
				.append("删除");
				var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
				//append方法执行完成后还是返回原来的元素
				$("<tr></tr>").append(empIdTd)
					.append(empNameTd)
					.append(genderTd)
					.append(emailTd)
					.append(deptNameTd)
					.append(btnTd)
					.appendTo("#emps_table tbody");
			});
		}
		//解析显示分页信息
		function build_page_info(result){
			//同样清空前面的
			$("#page_info_area").empty();
			
			$("#page_info_area").append("当前"+result.extend.pageInfo.pageNum+"页,总"+result.extend.pageInfo.pages+"页,总"+result.extend.pageInfo.total+"条记录");
		}
		//解析显示分页条，点击分页要能去下一页
		function build_page_nav(result) {
			//同样清空前面的
			$("#page_nav_area").empty();
			
			//page_nav_area
			var ul=$("<ul></ul>").addClass("pagination");
			
			//构建首页前页元素
			var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href","#"));
			var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
			if(result.extend.pageInfo.hasPreviousPage == false){
				prePageLi.addClass("disabled");
				firstPageLi.addClass("disabled");
			}else{
				//为元素添加点击翻页的事件
				firstPageLi.click(function(){
					to_page(1);
				});
				prePageLi.click(function(){
					to_page(result.extend.pageInfo.pageNum-1);
				});
			}
			
			
			//构建首页前页元素
			var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
			var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href","#"));
			if(result.extend.pageInfo.hasNextPage == false){
				nextPageLi.addClass("disabled");
				lastPageLi.addClass("disabled");
			}else{
				//为元素添加点击翻页的事件
				lastPageLi.click(function(){
					to_page(result.extend.pageInfo.pages);
				});
				nextPageLi.click(function(){
					to_page(result.extend.pageInfo.pageNum+1);
				});
			}
			
			
			
			//添加首页和前一页的提示
			ul.append(firstPageLi).append(prePageLi);
			//1,2,3……遍历给ul中添加页码提示
			$.each(result.extend.pageInfo.navigatepageNums,function(index,item){
				var numLi = $("<li></li>").append($("<a></a>").append(item));
				if(result.extend.pageInfo.pageNum == item){
					numLi.addClass("active");
				}
				numLi.click(function(){
					to_page(item);
				});
				ul.append(numLi);
			});
			//添加下一页和末页提示
			ul.append(nextPageLi).append(lastPageLi);
			
			var navEle = $("<nav></nav>").append(ul);
			navEle.appendTo("#page_nav_area");
		}
	</script>
</body>
</html>