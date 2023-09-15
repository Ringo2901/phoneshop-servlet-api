<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced Search">
    <h1>
        Advanced search
    </h1>
    <form method="post" action="${pageContext.servletContext.contextPath}/products/advancedSearch">
        <p>
            Description: <input type="text" name="description" value="${param.description}">
                            <c:choose>
                                <c:when test="${not empty param.searchMethod}">

                                      <c:choose>
                                          <c:when test="${param.searchMethod == 'ALL_WORDS'}">
                                             <input type="radio" name="searchMethod" value="ALL_WORDS" checked>All worlds
                                             <input type="radio" name="searchMethod" value="ANY_WORD">Any world
                                          </c:when>
                                          <c:otherwise>
                                             <input type="radio" name="searchMethod" value="ALL_WORDS">All worlds
                                             <input type="radio" name="searchMethod" value="ANY_WORD" checked>Any world
                                          </c:otherwise>
                                      </c:choose>
                                </c:when>
                                <c:otherwise>
                                     <input type="radio" name="searchMethod" value="ALL_WORDS" checked>All worlds
                                     <input type="radio" name="searchMethod" value="ANY_WORD">Any world
                               </c:otherwise>
                            </c:choose>
        </p>
        <p>
            Min price: <input type="text" name="minimumPrice"
                              value="${param.minimumPrice}">
            <c:if test="${not empty errorsMap['minPrice']}">
        <div class="error">${errorsMap['minPrice']}</div>
        </c:if>
        </p>
        <p>
            Max price: <input type="text" name="maximumPrice"
                              value="${param.maximumPrice}">
            <c:if test="${not empty errorsMap['maxPrice']}">
        <div class="error">${errorsMap['maxPrice']}</div>
        </c:if>
        </p>
        <button>
            Search
        </button>
    </form>
    <c:if test="${not empty productList}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${productList}">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
                    </td>
                    <td class="price">
                        <a href=""
                           onclick='window.open("${pageContext.servletContext.contextPath}/products/${product.id}/priceHistory", "_blank", "scrollbars=0,resizable=0,height=400,width=360");'>
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/></a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <p>
        <tags:recentlyView products="${recentProducts}"/>
    </p>
</tags:master>
