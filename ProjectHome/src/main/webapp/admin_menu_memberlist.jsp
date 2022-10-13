<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="정보수정" onclick="javascript:window.location='modify.jsp'">
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

function submit_ajax(){
	var menu = $('#selectsearch2').val();
	var keyword = $('#keyword2').val();
	var re = 'search_member.do?s_menu='+menu+'&s_keyword='+keyword;
	window.location.replace(re);
	/*
	if(keyword==""){
		alert('검색어를 입력해주세요.');
	} else {
		window.location.replace(re);
	}*/
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
	.test1{  }
	.text-center{ padding-top: 80px; }
	.ss{ padding-top: 50px; }
</style>
</head>
<body>
	<p class="text-center">회원 목록</p>
	
	<div class="container">
        <p></p>
    </div>
    <br>
	<div class="container">   
	<br>
        <table class="table table table-hover">
            <thead class="thead-light">
              <tr>
              
              </tr>
              
              <tr>
                <th scope="col">ID</th>
                <th scope="col">가입 날짜</th>
                <th scope="col">등급</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="dto">
			<tr>
                <td>
					<a href="memberview.do?mId=${dto.mId}">${dto.mId}
					<!-- 정지상태면 정지중 표시하고싶은데 안됨 -->
					</a>
				</td>
				<td>${dto.mDate}</td>
				<td>${dto.mGrade}</td>
              </tr>
			</c:forEach>
            </tbody>
        </table>
    </div>
    <nav aria-label="Page navigation example">
		<ul class="ss pagination justify-content-center">
		<td>
			<form id ="search2">
				<select id="selectsearch2">
					<option value="mId">아이디</option>
					<option value="mName">이름</option>
				</select>
				<input type="text" id="keyword2" class="keyword2" />
				<button type="button" class="btn btn-warning" onclick="submit_ajax()">검색</button>
				
			</form>
		</td>
        </ul>
	</nav>
	<nav aria-label="Page navigation example">
		<ul class="pagination justify-content-center">
			<!-- 처음 -->
        	<c:choose>
			<c:when test="${(page.curPage - 1) < 1}">
            	<li class="page-item">
                	<a class="page-link" aria-label="Previous">
                  		<span aria-hidden="true">&laquo;</span>
                	</a>
            	</li>
            </c:when>
            <c:otherwise>
            	<li class="page-item">
                	<a class="page-link" href="mlist.do?page=1" aria-label="Previous">
                  		<span aria-hidden="true">&laquo;</span>
                	</a>
            	</li>
			</c:otherwise>
			</c:choose>
  
         	<!-- 이전 -->
			<c:choose>
			<c:when test="${(page.curPage - 1) < 1}">
				<li class="page-item">
            		<a class="page-link">Previous</a>
            	</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
            		<a class="page-link" href="mlist.do?page=${page.curPage - 1}">Previous</a>
            	</li>
			</c:otherwise>
			</c:choose>
            
            
            <!-- 개별페이지 -->
            <c:forEach var="fEach" begin="${page.startPage}" end="${page.endPage}" step="1">
				<c:choose>
				<c:when test="${page.curPage == fEach}">
					 <li class="page-item">
					  	<a class="page-link">${fEach}</a>
					 </li>
				</c:when>
					<c:otherwise>
            <li class="page-item">
            	<a class="page-link" href="mlist.do?page=${fEach}">${fEach}</a>
            </li>
            </c:otherwise>
				</c:choose>
			</c:forEach>


            <!-- 다음 -->
			<c:choose>
			<c:when test="${(page.curPage + 1) > page.totalPage}">
				<li class="page-item">
                	<a class="page-link">Next</a>
            	</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
                	<a class="page-link" href="mlist.do?page=${page.curPage + 1}">Next</a>
            	</li>
			</c:otherwise>
			</c:choose>
			
            <!-- 끝 -->
			<c:choose>
			<c:when test="${page.curPage == page.totalPage}">
				<li class="page-item">
                	<a class="page-link" aria-label="Next">
                  	<span aria-hidden="true">&raquo;</span>
                	</a>
            	</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
                	<a class="page-link" href="mlist.do?page=${page.totalPage}" aria-label="Next">
                  	<span aria-hidden="true">&raquo;</span>
                	</a>
            	</li>
			</c:otherwise>
			</c:choose>
            
            
        </ul>
    </nav>
	
	
	
	
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>
</html>


<!-- 

<table width="800" cellpadding="0" cellspacing="0" border="1">
		<tr>
			<td>ID</td>
			<td>가입 날짜</td>
			<td>등급</td>
		</tr>
		<c:forEach items="${list}" var="dto">
			<tr>
				<td>
					
					<a href="memberview.do?mId=${dto.mId}">
					${dto.mId}
					
					</a>
				</td>
				<td>${dto.mDate}</td>
				<td>${dto.mGrade}</td>
			</tr>
		</c:forEach>
	</table>

 -->