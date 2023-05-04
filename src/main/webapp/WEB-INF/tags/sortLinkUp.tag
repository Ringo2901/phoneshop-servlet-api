<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?sort=${sort}&order=${order}&query=${param.query}">
    <img src="${sort eq param.sort and order eq param.order ? pageContext.request.contextPath.concat('/images/arrowUpBold.png') : pageContext.request.contextPath.concat('/images/arrowUp.png')}" width="25" height="25">
</a>
