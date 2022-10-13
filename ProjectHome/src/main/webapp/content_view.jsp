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
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery.js"></script>

<script type="text/javascript" charset="utf-8">
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
	var queryString = $("#reg_frm").serialize();
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

function go_delete(){
	if(!confirm("글을 삭제합니다.")){
		alert("삭제를 취소했습니다.");
	} else {
		alert("삭제되었습니다.")
		window.location.replace("delete.do?bId=${content_view.bId}");
	}
}

function go_good(){
	$.ajax({
		url: 'good.do',
		type: 'POST',
		data: 
			{ bId : ${content_view.bId} },
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("content_view.do?bId=${content_view.bId}");
			} else {
				alert(result.desc);
			}
		}
	});
}

function go_comment(){
	var queryString = $("#com_frm").serialize();
	$.ajax({
		url: 'comment.do',
		type: 'POST',
		data: queryString ,
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("content_view.do?bId=${content_view.bId}");
			} else {
				alert(result.desc);
			}
		}
	});
}

function delete_comment(number){
	$.ajax({
		url: 'del_comment.do',
		type: 'POST',
		data: 
			{ rId : number ,
			  bId : ${content_view.bId} },
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("content_view.do?bId=${content_view.bId}");
			} else {
				alert(result.desc);
			}
		}
	});
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
	.info { font-weight: lighter; }
	.title{ font-size : 200%; font-weight: bold; }
	.writer{ font-size : 120%; }
	.test2{ padding-top: 80px; }
	.content { padding : 20px; }
	#rContent {
    	width: 100%;
    	height: 4em;
    	resize: none;
  	}
  	.comment{ font-size : 120%; font-weight: bold; }
</style>
</head>
<body>
 <div class="container-fluid">
        <div class="row">
            <div class="col test1"></div>
 
            <div class="col-6 test2">
           		<div class="form-group">
            		<label for="mId" class="title pl-3">${content_view.bTitle}</label>
            		<p class="writer pl-3">${content_view.bMem}</p>
            		<hr>
					<p class="info text-right pr-3 text-secondary">
					no.${content_view.bId} / ${content_view.bDate} / 조회수 ${content_view.bHit} / 추천수 ${content_view.bGood}
					</p>
					<hr>
					<div class="content">
						${content_view.bContent}
					</div>
					<hr>
					<div>
						<button type="button" class="btn btn-success" onclick="javascript:window.location='list.do?page=<%= session.getAttribute("cpage") %>'">글 목록</button>
					
	           			<c:if test="${id==content_view.bMem}">
						<button type="button" class="btn btn-success" onclick="javascript:window.location='modify_view.do?bId=${content_view.bId}'">글 수정</button>
						<button type="button" class="btn btn-danger float-right" onclick="go_delete()">글 삭제</button>
						</c:if>
						<c:if test="${grade==5}">
							<button type="button" class="btn btn-danger  float-right" onclick="go_delete()">글 삭제</button>
						</c:if>
						<c:if test="${id!=content_view.bMem}">
							<button type="button" id="good" class="btn btn-info" onclick="go_good()">추천</button>
						</c:if>
						
						<c:choose>
						<c:when test="${not empty sessionScope.id}">
							<button type="button" class="btn btn-info" onclick="javascript:window.location='reply_view.do?bId=${content_view.bId}'">답변</button>
						</c:when>
						</c:choose>
           			</div>
           			<hr>
           			<div>
           				<form id="com_frm">
						<input type="hidden" name="bId" value="${content_view.bId}">
						<input type="hidden" name="bMem" value="${id}">
						<textarea id="rContent" name="rContent"></textarea>
						<button type="button" class="btn btn-success" onclick="go_comment()">댓글달기</button>
						</form>
		           		<hr>	
           			</div>
           			<div>
           				<c:forEach items="${list}" var="dtos">
           				<input type="hidden" id="comment_Id" name="rId" value="${dtos.rId}">
							<div class="comment pl-3">${dtos.rMem}
								<c:if test="${id==dtos.rMem || grade==5}">
									<td>
										<button type="button" class="nomal btn btn-sm btn-danger" onclick="delete_comment(${dtos.rId})">삭제</button>
									</td>
								</c:if>
							</div>
							<div class="pl-5 pt-3">
								<p id="comment">${dtos.rContent}</p>
							</div>
							<hr>
						</c:forEach>
           			</div>
           			
           		</div>
           		
           		
            </div>
            
            
            
            <div class="col test1"></div>
        </div>  
    </div>



	
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>

<!-- 
<table width="800" cellpadding="0" cellspacing="0" border="1">
	
		<tr>
			<td> 번호 </td>
			<td> ${content_view.bId}</td>
		</tr>
		<tr>
			<td> 작성일 </td>
			<td> ${content_view.bDate}</td>
		</tr>
		<tr>
			<td> 히트 </td>
			<td> ${content_view.bHit}</td>
		</tr>
		<tr>
			<td> 추천 </td>
			<td> ${content_view.bGood}</td>
		</tr>
		<tr>
			<td> 이름 </td>
			<td> ${content_view.bMem}</td>
		</tr>
		<tr>
			<td> 제목 </td>
			<td> ${content_view.bTitle}</td>
		</tr>
		<tr>
			<td> 내용 </td>
			<td> ${content_view.bContent}</td>
		</tr>
		<tr>
			<td colspan="2">

				
				
				<c:if test="${id==content_view.bMem}">
					<button type="button" onclick="javascript:window.location='modify_view.do?bId=${content_view.bId}'">수정</button>
					<button type="button" onclick="go_delete()">삭제</button>
				</c:if>
				<c:if test="${grade==5}">
					<button type="button" onclick="go_delete()">삭제</button>
				</c:if>
				<c:if test="${id!=content_view.bMem}">
					<button type="button" id="good" onclick="go_good()">추천</button>
				</c:if>
				
				<button type="button" onclick="javascript:window.location='list.do?page=<%= session.getAttribute("cpage") %>'">목록</button>
				<c:choose>
				<c:when test="${not empty sessionScope.id}">
					<button type="button" onclick="javascript:window.location='reply_view.do?bId=${content_view.bId}'">답변</button>
				</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<form id="com_frm">
				<input type="hidden" name="bId" value="${content_view.bId}">
				<input type="hidden" name="bMem" value="${id}">
				<textarea id="rContent" name="rContent" rows="10"></textarea>
				<button type="button" onclick="go_comment()">댓글달기</button>
				</form>
			</td>
		</tr>
		<c:forEach items="${list}" var="dtos">
			<input type="hidden" id="comment_Id" name="rId" value="${dtos.rId}">
			<tr>
				<td>${dtos.rMem}</td>
				<td id="comment">${dtos.rContent}</td>
				<c:if test="${id==dtos.rMem || grade==5}">
					<td>
						<button type="button" class="nomal" onclick="delete_comment(${dtos.rId})">삭제</button>
					</td>
				</c:if>
			</tr>
			
		</c:forEach>
	</table>


 -->
