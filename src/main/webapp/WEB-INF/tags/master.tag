<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<tags:header pageTitle="ProductList"/>
  <main>
    <jsp:doBody/>
  </main>
<tags:footer/>
</html>