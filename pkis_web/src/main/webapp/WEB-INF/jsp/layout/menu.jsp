<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<sec:authentication var="principal" property="principal" />

<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<nav class="sidebar sidebar-offcanvas" id="sidebar">
	<ul class="nav">
		<c:forEach var="menu" items="${principal.menus}" varStatus="idx">
			<c:choose>
				<c:when test="${menu.CHILDREN != null && fn:length(menu.CHILDREN) gt 0}">
					<li class="nav-item">
						<a class="nav-link" data-toggle="collapse" href="#ui-basic<c:out value='${idx.index}'/>" aria-expanded="false" aria-controls="ui-basic">
							<i class="mdi mdi-circle-outline menu-icon"></i>
							<span class="menu-title"><c:out value="${menu.MENU_CNAME}"/></span>
            				<i class="menu-arrow"></i>
						</a>
						<div class="collapse" id="ui-basic<c:out value='${idx.index}'/>">
		            		<ul class="nav flex-column sub-menu">
		              			<c:forEach var="subMenu" items="${menu.CHILDREN}">									
									<li class="nav-item"> <a class="nav-link" href="<c:url value="${subMenu.MENU_URL}" />"><c:out value="${subMenu.MENU_CNAME}"/></a></li>
								</c:forEach>
		            		</ul>
		          		</div>
					</li>
				</c:when>
				<c:otherwise>
					<li class="nav-item">
						<a href="javascript:;" class="">
							<i class="mdi mdi-home menu-icon"></i>
							<span class="menu-title"><c:out value="${menu.MENU_CNAME}"/></span>
						</a>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</nav>
