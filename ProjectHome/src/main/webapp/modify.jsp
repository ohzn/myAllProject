<%@page import="com.study.jsp.dto.MDto" %>
<%@page import="com.study.jsp.dao.MDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<% request.setCharacterEncoding("UTF-8"); %>
<%
	String id = (String)session.getAttribute("id");
	MDao dao = MDao.getInstance();
	MDto dto = dao.getMember(id);
%>
<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>

<meta charset="UTF-8">
 <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgb(169, 216, 0);">
    <a class="navbar-brand" href="main.jsp">RabbitTail</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
  
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link" id="Notice" data-value="Notice" href="#">Notice<span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href='list.do'>FreeBoard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="databox" data-value="databox" href="#">DataBox</a>
        </li>
      </ul>
      <form id ="search" class="form-inline my-2 my-lg-0">
      	<select id="selectsearch" class="form-select" aria-label="Default select example">
				<option value="bTitle">제목</option>
				<option value="bMem">작성자</option>
		</select>
        <input id="keyword" class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="search_ajax()">Search</button>&nbsp;&nbsp;
      </form>
    <c:choose>	  
	<c:when test="${not empty sessionScope.id}">
      <form id="nav_frm" class="form-inline my-2 my-lg-0">
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="로그아웃" onclick="logout()">&nbsp;&nbsp;
      	<c:if test="${grade==5}">
			<button type="button" class="btn btn-outline-danger my-2 my-sm-0" onclick="javascript:window.location='admin_menu.jsp'">회원관리</button>
		</c:if>
      </form>
    </c:when>
	<c:otherwise>
        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="javascript:window.location='login.jsp'">Login</button>
	</c:otherwise>
	</c:choose>	      
    
	
    </div>
</nav>
<title>Modify Page</title>
<script src="http://code.jquery.com/jquery.js"></script>
<script>
function search_ajax(){
	var menu = $('#selectsearch').val();
	var keyword = $('#keyword').val();
	var re = 'search_view.do?s_menu='+menu+'&s_keyword='+keyword;
	if(keyword==""){
		alert('검색어를 입력해주세요.');
	} else {
		window.location.replace(re);
	}

}

function form_check(){
	
	if($('#mPw').val().length == 0){
		alert("비밀번호는 필수사항입니다.");
		$('#mPw').focus();
		return;
	}
	
	if($('#mPw').val() != $('#mPw_check').val()){
		alert("비밀번호가 일치하지 않습니다.");
		$('#mPw').focus();
		return;
	}
	
	if($('#mEmail').val().length == 0){
		alert("이메일은 필수사항입니다.");
		$('#mEmail').focus();
		return;
	}
	
	submit_ajax();
}

function submit_ajax(){
	var queryString = $("#reg_frm").serialize();
	$.ajax({
		url: 'modify.do',
		type: 'POST',
		data: queryString,
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("main.jsp");
			} else {
				alert(result.desc);
			}
		}
	});
}

function delete_check(){
	if(!confirm("탈퇴시 되돌릴 수 없습니다. 정말 탈퇴하시겠습니까?")){
		alert("탈퇴를 취소했습니다.");
		window.location.replace("modify.jsp");
	} else {
		go_goodbye();
	}
}

function logout(){
	var queryString = $("#nav_frm").serialize();
	$.ajax({
		url: 'logout.do',
		type: 'POST',
		data: queryString,
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("login.jsp");
			} else {
				alert(result.desc);
			}
		}
	});
}

function go_goodbye(){
	var queryString = $("#bye").serialize();
	$.ajax({
		url: 'mem_delete.do',
		type: 'POST',
		data: queryString ,
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("main.jsp");
			} else {
				alert(result.desc);
			}
		}
	});
}
</script>
<style>
	.test1{  }
	.test2{ padding-top: 80px; }
</style>
</head>
<body>

	<div class="container-fluid">
        <div class="row">
            <div class="col test1"></div>
            <div class="col-5 test2">
				<form id="reg_frm">
	            	<div class="form-group">
	                	<label for="mId">ID<span style="color: orangered;">*</span></label>
	                	<input type="text" class="form-control" id="mId" name="mId" placeholder="UserId" value="<%= dto.getmId() %>" readonly/>
	              	</div>
	            	<div class="form-group">
	              		<label for="mPw">Password<span style="color: orangered;">*</span></label>
	              		<input type="password" class="form-control" id="mPw" name="mPw" placeholder="Password">
	            	</div>
	            	<div class="form-group">
	              		<label for="mPw_check">Password_Check<span style="color: orangered;">*</span></label>
	              		<input type="password" class="form-control" id="mPw_check" name="mPw_check" placeholder="Password">
	            	</div>
	            	<div class="form-group">
	                	<label for="mName">이름<span style="color: orangered;">*</span></label>
	                	<input type="text" class="form-control" id="mName" name="mName" placeholder="UserName" value="<%= dto.getmName() %>" readonly/>
	              	</div>
	            	<div class="form-group">
	                	<label for="mAddress">Email address<span style="color: orangered;">*</span></label>
	                	<input type="text" class="form-control" id="mEmail" name="mEmail" placeholder="Enter email" value="<%= dto.getmEmail() %>">
	             	</div>
	             	<div class="form-group">
	                	<label for="mAddress">Address</label>
	                	<input type="text" class="form-control" id="mAddress" name="mAddress" value="<%= dto.getmAddress() %>">
	             	</div>
	             	<small id="joinhelp" class="form-text text-muted"><span style="color: orangered;">*</span>는 필수 사항입니다.</small>
	             	<p></p>
	  				<button type="button" class="btn btn-success btn-block mb-4" onclick="form_check()">회원정보 수정</button>
	        	</form>
	        	<br>
	        	<br>
	        	<hr class="my-2">
	        	<form id ="bye">
	        		<div class="form-group">
	              		<label for="mPw">Password<span style="color: orangered;">*</span></label>
	              		<input type="password" class="form-control" id="mPw" name="mPw" placeholder="Password">              		
	            	</div>
	            	<button type="button" class="btn btn-danger btn-block mb-4" onclick="delete_check()">회원 탈퇴</button>
				</form>
            </div>
            <div class="col test1"></div>
        </div>   
    </div>
	
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>
</html>