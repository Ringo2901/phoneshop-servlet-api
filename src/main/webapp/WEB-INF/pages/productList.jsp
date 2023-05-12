<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
      <input name="query" value="${param.query}">
      <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
            Description
            <tags:sortLinkUp sort="DESCRIPTION" order="ASC"/>
            <tags:sortLinkDown sort="DESCRIPTION" order="DESC"/>
        </td>
        <td class="price">
            Price
            <tags:sortLinkUp sort="PRICE" order="ASC"/>
            <tags:sortLinkDown sort="PRICE" order="DESC"/>
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
        </td>
        <td class="price">
            <a href=""
                onclick='window.open("${pageContext.servletContext.contextPath}/products/${product.id}/priceHistory", "_blank", "scrollbars=0,resizable=0,height=500,width=400");'>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>
