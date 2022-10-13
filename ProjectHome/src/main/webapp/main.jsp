<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String id = (String)session.getAttribute("id");
	String name = (String)session.getAttribute("name");

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
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="정보수정" onclick="javascript:window.location='modify.jsp'">&nbsp;&nbsp;
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
<title>Main Page</title>
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

function find_board(){

	$.ajax({
		url: 'list.do',
		type: 'POST',

		dataType: 'text',
		success: function(json){

			window.location.replace("list.jsp");
			
		}
	});
	
}

	//console.log("name : " + b_name);
	//window.location.replace(re);

</script>
<style>
	.info { font-size : 140%; font-weight: lighter; }
	.tt{ font-weight: bold; }
	.test2{ padding-top: 80px; font-size : 140%; }
	.test1{ padding-top: 80px; font-size : 200%; font-weight: lighter; }
</style>
</head>
<body>
	<div class="text-center">
		<p class="test1">RabbitTail</p>
		<c:choose>
		<c:when test="${not empty sessionScope.id}">
			<p class="test2"><%= name %>님 안녕하세요.</p>
			<c:if test="${grade==5}">
				<button type="button" class="btn btn-warning" onclick="javascript:window.location='mlist.do'">회원 목록</button>
			</c:if>
		</c:when>
		</c:choose>
    	
    				
	</div>
	
	
	

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>
</html>


<!-- 

<form id="board_button">
		<button type="button" id="notice" value="notice" onclick="find_board(notice)">공지사항</button>
		<button type="button" id="freeboard" value="freeboard" onclick="find_board(freeboard)">자유게시판</button>
		<button type="button" id="databox" value="databox" onclick="find_board(databox)">자료실</button>
	</form>


 -->