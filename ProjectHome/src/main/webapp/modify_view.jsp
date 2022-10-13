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
      <form id="reg_frm" class="form-inline my-2 my-lg-0">
      	<input type="button" class="btn btn-outline-success my-2 my-sm-0" value="로그아웃" onclick="submit_ajax()">&nbsp;&nbsp;
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
<script type="text/javascript" src="./naver-editor/js/service/HuskyEZCreator.js" charset="utf-8"></script>
<script src="http://code.jquery.com/jquery.js"></script>
<script>
    function form_check() {
    	oEditors.getById["bContent"].exec("UPDATE_CONTENTS_FIELD", []);
    	
    	submit_ajax();
    }
    
    function it_check(){
    	if($('#bTitle').val().length == 0){
    		alert("제목은 필수사항입니다.");
    		$('#bTitle').focus();
    		return;
    	}
    	
    	form_check();
    }

    
    function submit_ajax(){
    	var queryString = $("#mod_frm").serialize();
    	$.ajax({
    		url: 'modify_ok.do',
    		type: 'POST',
    		data: queryString,
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
</script>
<style>
	#bTitle { width: 100%; }
	#bContent { width: 100%; }
	.info { font-size : 140%; font-weight: lighter; }
	.smallinfo { font-weight: lighter; }
	.tt{ font-weight: bold; }
	.test2{ padding-top: 80px; }
</style>
</head>
<body>
<div class="container-fluid">
		
        <div class="row">
            <div class="col test1"></div>
            
            <div class="col-5 test2">
				<form id="mod_frm">
				<input type="hidden" id="bId" name="bId" value="${content_view.bId}">
	            	<div class="form-group">
	                	<label for="mId" class="tt">제목</label>
	                	<p class="info"><input type="text" id="bTitle" name="bTitle" value="${content_view.bTitle}"></p>
	              	</div>
	              	<hr>
	              	<p class="smallinfo text-right pr-3 text-secondary">
					no.${content_view.bId} / ${content_view.bDate} / 조회수 ${content_view.bHit} / 추천수 ${content_view.bGood}
					</p>
					<hr>
	            	<div class="form-group">
	                	<label for="mId" class="tt">내용</label>
	                	<textarea id="bContent" name="bContent" rows="10" ></textarea>
	                	<script type="text/javascript">
							var oEditors = [];
							nhn.husky.EZCreator.createInIFrame({
    							oAppRef: oEditors,
    							elPlaceHolder: "bContent",
    							sSkinURI: "./naver-editor/SmartEditor2Skin.html",
    							fCreator: "createSEditor2"
							});
						</script>
	              	</div>
	              	<hr>
	             	<p></p>
	             	<button type="button" class="btn btn-warning mb-4" onclick="javascript:window.location='list.do?page=<%= session.getAttribute("cpage") %>'">글 목록</button>
	  				<button type="button" class="btn btn-warning mb-4" onclick="javascript:window.location='content_view.do?bId=${content_view.bId}'">수정 취소</button>
	  				
	        		<button type="button" class="btn btn-success mb-4 float-right" onclick="it_check()">글 수정</button>
	        	</form>
            </div>
            
            <div class="col test1"></div>
        </div>   
</div>
    
    

<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>
</html>

<!-- 
	<table width="800" cellpadding="0" cellspacing="0" border="1">
		<form id="reg_frm">
			<input type="hidden" name="bId" value="${content_view.bId}">
			<tr>
				<td> 번호 </td>
				<td> ${content_view.bId}</td>
			</tr>
			<tr>
				<td> 히트 </td>
				<td> ${content_view.bHit}</td>
			</tr>
			<tr>
				<td> 이름 </td>
				<td>${content_view.bMem}</td>
			</tr>
			<tr>
				<td> 제목 </td>
				<td><input type="text" id="bTitle" name="bTitle" value="${content_view.bTitle}"></td>
			</tr>
			<tr>
				<td> 내용 </td>
				<td>
					<textarea name="bContent" id="bContent" rows="10" cols="100">${content_view.bContent}</textarea>
					<script type="text/javascript">
					var oEditors = [];
					nhn.husky.EZCreator.createInIFrame({
    					oAppRef: oEditors,
    					elPlaceHolder: "bContent",
    					sSkinURI: "./naver-editor/SmartEditor2Skin.html",
    					fCreator: "createSEditor2"
					});
					</script>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="button" onclick="it_check()">수정완료</button>
					<button type="button" onclick="javascript:window.location='content_view.do?bId=${content_view.bId}'">취소</button>
					<button type="button" onclick="javascript:window.location='list.do?page=<%= session.getAttribute("cpage") %>'">목록</button>
				</td>
			</tr>
		</form>
	</table>
 -->