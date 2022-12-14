<%@page import="com.study.jsp.dto.BDto" %>
<%@page import="com.study.jsp.dao.BDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% 
	String id = (String)session.getAttribute("id");
	
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
				<option value="bTitle">??????</option>
				<option value="bMem">?????????</option>
		</select>
        <input id="keyword" class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="search_ajax()">Search</button>&nbsp;&nbsp;
      </form>
      
    <c:choose>
	<c:when test="${not empty sessionScope.id}">
      <form id="nav_frm" class="form-inline my-2 my-lg-0">
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="????????????" onclick="logout()">&nbsp;&nbsp;
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="????????????" onclick="javascript:window.location='modify.jsp'"> &nbsp;&nbsp;
      	<c:if test="${grade==5}">
			<button type="button" class="btn btn-outline-danger my-2 my-sm-0" onclick="javascript:window.location='admin_menu.jsp'">????????????</button>
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
<script src="http://code.jquery.com/jquery.js" type="text/javascript"></script>
<script>
function search_ajax(){
	var menu = $('#selectsearch').val();
	var keyword = $('#keyword').val();
	var re = 'search_view.do?s_menu='+menu+'&s_keyword='+keyword;
	if(keyword==""){
		alert('???????????? ??????????????????.');
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



// ?????????????????? ?????? ??? ????????? ???????????? ??????
</script>
</head>
<body>

	<div class="container">
        <div class="Pb-1">
        	<c:if test="${not empty sessionScope.id}">
        		<button type="button" class="btn float-right btn-warning" onclick="javascript:window.location='write_view.do'">?????????</button>
        	</c:if>
        </div>
        <p></p>
    </div>
    <br>
	<div class="container">   
	<br>
		<div class="wrap">
              
        </div>
        <table class="table table table-hover">
        
            <thead class="thead-light">
              
              
              <tr>
                <th scope="col">??????</th>
                <th scope="col">??????</th>
                <th scope="col">??????</th>
                <th scope="col">??????</th>
                <th scope="col">??????</th>
                <th scope="col">??????</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="dto">
			<tr>
                <th scope="row">${dto.bId}</th>
                <td>${dto.bMem}</td>
                <td scope="col">
                	<c:forEach begin="1" end="${dto.bIndent}">-</c:forEach>
                    <a href="content_view.do?bId=${dto.bId}">${dto.bTitle} </a>
                    <c:if test="${dto.c_number!=0}">(${dto.c_number})</c:if>
                    <c:set var="now" value="<%=new java.util.Date()%>" /><!-- ???????????? -->
                    <fmt:formatDate value="${now}" pattern="yyMMddhh" var="today" /><!-- ??????????????? ????????? -->
                    <fmt:formatDate  value="${dto.bDate}" pattern="yyMMddhh" var="dtoDate" /><!-- ????????? ??????????????? ????????? -->
                    <c:if test="${today - dtoDate le 1}"><!-- 1???????????? new ?????? -->
                    <span class="badge badge-info">new!</span>
                    </c:if>
                </td>
                
                
                
                
                <td>${dto.bDate}</td>
				<td>${dto.bHit}</td>
				<td>${dto.bGood}</td>
              </tr>
			</c:forEach>
            </tbody>
        </table>
    </div>
	
	<nav aria-label="Page navigation example">
		<ul class="pagination justify-content-center">
			<!-- ?????? -->
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
                	<a class="page-link" href="list.do?page=1" aria-label="Previous">
                  		<span aria-hidden="true">&laquo;</span>
                	</a>
            	</li>
			</c:otherwise>
			</c:choose>
  
         	<!-- ?????? -->
			<c:choose>
			<c:when test="${(page.curPage - 1) < 1}">
				<li class="page-item">
            		<a class="page-link">Previous</a>
            	</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
            		<a class="page-link" href="list.do?page=${page.curPage - 1}">Previous</a>
            	</li>
			</c:otherwise>
			</c:choose>
            
            
            <!-- ??????????????? -->
            <c:forEach var="fEach" begin="${page.startPage}" end="${page.endPage}" step="1">
				<c:choose>
				<c:when test="${page.curPage == fEach}">
					 <li class="page-item">
					  	<a class="page-link">${fEach}</a>
					 </li>
				</c:when>
					<c:otherwise>
            <li class="page-item">
            	<a class="page-link" href="list.do?&page=${fEach}">${fEach}</a>

            </li>
            </c:otherwise>
				</c:choose>
			</c:forEach>


            <!-- ?????? -->
			<c:choose>
			<c:when test="${(page.curPage + 1) > page.totalPage}">
				<li class="page-item">
                	<a class="page-link">Next</a>
            	</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
                	<a class="page-link" href="list.do?page=${page.curPage + 1}">Next</a>
            	</li>
			</c:otherwise>
			</c:choose>
			
            <!-- ??? -->
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
                	<a class="page-link" href="list.do?page=${page.totalPage}" aria-label="Next">
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
			<td>??????</td>
			<td>??????</td>
			<td>??????</td>
			<td>??????</td>
			<td>??????</td>
			<td>??????</td>
		</tr>
		<c:forEach items="${list}" var="dto">
			<tr>
				<td>${dto.bId}</td>
				<td>${dto.bMem}</td>
				<td>
					<c:forEach begin="1" end="${dto.bIndent}">-</c:forEach>
					<a href="content_view.do?bId=${dto.bId}">${dto.bTitle}
					<c:if test="${dto.c_number!=0}">(${dto.c_number})</c:if>
					</a>
				</td>
				<td>${dto.bDate}</td>
				<td>${dto.bHit}</td>
				<td>${dto.bGood}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="6">
			<c:choose>
			<c:when test="${not empty sessionScope.id}">
				<button type="button" onclick="javascript:window.location='write_view.do'">?????????</button>
			</c:when>
			<c:otherwise>
				<button type="button" onclick="javascript:window.location='login.jsp'">?????????</button>
			</c:otherwise>
			</c:choose>
		</tr>
	</table>
	




	<tr>
		<td colspan="6">
			
			<c:choose>
			<c:when test="${(page.curPage - 1) < 1}">
				[ &lt;&lt; ]
			</c:when>
			<c:otherwise>
				<a href="list.do?page=1">[ &lt;&lt; ]</a>
			</c:otherwise>
			</c:choose>
			
			
			<c:choose>
			<c:when test="${(page.curPage - 1) < 1}">
				[ &lt; ]
			</c:when>
			<c:otherwise>
				<a href="list.do?page=${page.curPage - 1}">[ &lt; ]</a>
			</c:otherwise>
			</c:choose>
			
			
			<c:forEach var="fEach" begin="${page.startPage}" end="${page.endPage}" step="1">
				<c:choose>
				<c:when test="${page.curPage == fEach}">
					[ ${fEach} ] &nbsp;
				</c:when>
					<c:otherwise>
						<a href="list.do?page=${fEach}">[ ${fEach} ]</a> &nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			
			<c:choose>
			<c:when test="${(page.curPage + 1) > page.totalPage}">
				[ &gt; ]
			</c:when>
			<c:otherwise>
				<a href="list.do?page=${page.curPage + 1}">[ &gt; ]</a>
			</c:otherwise>
			</c:choose>
			
			
			<c:choose>
			<c:when test="${page.curPage == page.totalPage}">
				[ &gt;&gt; ]
			</c:when>
			<c:otherwise>
				<a href="list.do?page=${page.totalPage}">[ &gt;&gt; ]</a>
			</c:otherwise>
			</c:choose>
		</td>
	</tr>
	

		
	
	
	
	
	
	 <form id ="search">
		<select id="selectsearch">
				<option value="bTitle">??????</option>
				<option value="bMem">?????????</option>
			</select>
		<input type="text" id="keyword" class="keyword" /><button type="button" onclick="search_ajax()">??????</button>
	
	</form>
	
	totalCount : ${page.totalCount}<br>
	listCount : ${page.listCount}<br>
	totalPage : ${page.totalPage}<br>
	curPage : ${page.curPage}<br>
	pageCount : ${page.pageCount}<br>
	startPage : ${page.startPage}<br>
	endPage : ${page.endPage}<br> 
	

	-->
	