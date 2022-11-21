<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	   
	   <title>Pagina dei Risultati</title>
	 </head>
	 
	<body class="d-flex flex-column h-100">
	 
		<!-- Fixed navbar -->
		<jsp:include page="../navbar.jsp"></jsp:include>
	 
	
		<!-- Begin page content -->
		<main class="flex-shrink-0">
		  <div class="container">
		  
		  		<div class="alert alert-success alert-dismissible fade show  ${successMessage==null?'d-none':'' }" role="alert">
				  ${successMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
				  ${errorMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
		  
		  
		  
		  		<div class='card'>
				    <div class='card-header'>
				        <h5>I miei annunci</h5> 
				    </div>
				    <div class='card-body'>
				    <a class="btn btn-primary " href="${pageContext.request.contextPath}/annuncio/insert">Inserisci un nuovo annuncio</a>
				    
				        <div class='table-responsive'>
				            <table class='table table-striped ' >
				                <thead>
				                    <tr>
			                         	<th>Testo annuncio</th>
				                        <th>Prezzo</th>
				                         <th>Data</th>
				                        <th>Aperto</th>
				                        <th>Azioni</th>
				                    </tr>
				                </thead>
				                <tbody>
				                	<c:forEach items="${annuncio_list_attribute }" var="annuncioItem">
										<tr>
											<td>${annuncioItem.testoAnnuncio }</td>
											<td>${annuncioItem.prezzo }</td>
											<td>${annuncioItem.data }</td>
											<c:if test = "${annuncioItem.aperto == true}">
         										<td>Aperto</td>
         										<td>
													<a class="btn  btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/annuncio/edit/${annuncioItem.id }">Modifica</a>  
													<a class="btn  btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/annuncio/delete/${annuncioItem.id }">Elimina</a>     
												</td>
     										</c:if>
     										<c:if test = "${annuncioItem.aperto == false}">
         										<td>Chiuso</td>
         										<td>Non ci sono azioni da fare per articoli chiusi</td>
     										</c:if>
											
										</tr>
									</c:forEach>
				                </tbody>
				            </table>
				        </div>
				   
					<!-- end card-body -->			   
			    </div>
			<!-- end card -->
			</div>	
		 
		   
		 <!-- end container -->  
		  </div>
		  
		</main>
		
		<!-- Footer -->
		<jsp:include page="../footer.jsp" />
		
	</body>
</html>