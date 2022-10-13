<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String id = (String)session.getAttribute("id");
	String grade = (String)session.getAttribute("grade");
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

      </form>
    </c:when>
	<c:otherwise>
        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="javascript:window.location='login.jsp'">Login</button>
	</c:otherwise>
	</c:choose>
	
    </div>
</nav>
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery.js"></script>
<script>
function member_delete(){
	if(!confirm("정말 탈퇴시키겠습니까? 되돌릴 수 없습니다.")){
		alert("강제 탈퇴를 취소했습니다.");
	} else {
		alert("탈퇴되었습니다.")
		window.location.replace("mdelete.do?mId=${member_view.mId}");
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
</script>
<style>
	.info { font-size : 140%; font-weight: lighter; }
	.tt{ font-weight: bold; }
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
	                	<label for="mId" class="tt">회원 ID</label>
	                	<p class="info">${member_view.mId}</p>
	              	</div>
	              	<hr>
	            	<div class="form-group">
	                	<label for="mName" class="tt">회원 이름</label>
	                	<p class="info">${member_view.mName}</p>
	              	</div>
	              	<hr>
	            	<div class="form-group">
	                	<label for="mAddress" class="tt">Email address</label>
	                	<p class="info">${member_view.mEmail}</p>
	             	</div>
	             	<hr>
	             	<div class="form-group">
	                	<label for="mAddress" class="tt">가입 일자</label>
	                	<p class="info">${member_view.mDate}</p>
	             	</div>
	             	<hr>
	             	<div class="form-group">
	                	<label for="mAddress" class="tt">등급</label>
	                	<p class="info">${member_view.mGrade}</p>
	             	</div>
	             	<hr>
	             	<c:if test="${member_view.mBlack!=null}">
						<div class="form-group">
	                		<label for="mAddress" class="tt">정지 기한</label>
	                		<p class="info">${member_view.mBlack} 까지</p>
	             		</div>
	             		<hr>
					</c:if>
	             	<p></p>
	             	<button type="button" class="btn btn-success mb-4" onclick="javascript:window.location='mlist.do?page=<%= session.getAttribute("cpage") %>'">회원 목록</button>
	  				<button type="button" class="btn btn-success mb-4" onclick="">회원 등업</button>
	        		<button type="button" class="btn btn-danger mb-4 float-right" onclick="member_delete()">회원 강제 탈퇴</button>
	        		
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

<!-- 
<table width="800" cellpadding="0" cellspacing="0" border="1">
	
		
		<tr>
			<td colspan="2">
				<button type="button" onclick="">회원 정지</button>
				<button type="button" id="good" onclick="">회원 등업</button>
				<c:if test="${grade==5}">
					<button type="button" onclick="member_delete()">회원 강제 탈퇴</button>
				</c:if>
				<button type="button" onclick="javascript:window.location='mlist.do?page=<%= session.getAttribute("cpage") %>'">목록</button>
			</td>
		</tr>
	</table>
 -->