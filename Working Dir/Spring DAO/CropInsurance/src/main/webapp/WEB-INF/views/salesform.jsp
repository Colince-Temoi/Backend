<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sales Form</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
	integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-2.2.4.js"
	integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI="
	crossorigin="anonymous"></script>

</head>


<body>
	<div class="container mt-5">
	
		<c:if test="${msg ne null}">
			<div class="alert alert-success" id="alId">${msg}</div>
		
		</c:if>
		
		<div class="h2 text-center text-danger">Crop Insurance Application Form</div>
		<div class="card">
			<div class="card-header bg-light h3 text-primary">Insurance Form</div>

			<div class="card-body">
				<div class="container">
					<form action="./saveSales" method="post"
						enctype="multipart/form-data" onsubmit="return validateForm();">	
						<input type="hidden" name="salesId" id="salesId" value="${usales.salesId}">
											
						<div class="row">
							<div class="col-4">
								<label for="blockNameId" class="font-weight-bold">Customer Name</label>
								<select name="customer" id="customerId" class="form-control"
									onchange="getPanchayatByBlockId(this.value)">
									<option value="0">-select-</option>
									<c:forEach items="${customerList}" var="cust">									
										<option value="${cust.customerId}" 	<c:if test="${cust.customerId eq usales.customer.customerId}">selected='selected'</c:if> >${cust.customerName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-4">
								<label for="blockNameId" class="font-weight-bold">Product Name</label>
								<select name="product" id="productId" class="form-control"
									onchange="getProductQntyByProductId(this.value)">
									<option value="0">-select-</option>
									<c:forEach items="${productList}" var="prod">
										<option value="${prod.productId}" <c:if test="${prod.productId eq usales.product.productId}">selected='selected'</c:if>>${prod.productName}</option>
									</c:forEach>
								</select>
								
							</div>
							<div class="col-2">
								<label id="qid">Aval Qnty</label>
								<input type="text" name="qn" id="qnId" readonly="readonly" class="form-control">
							</div>
							<div class="col-2">
								<label for="popId" class="font-weight-bold">Quantity
									Doc</label> <input type="text" class="form-control" name="salesQnty"
									id="salesQntyId" value="${usales.salesQnty}">
							</div>

							
							
						</div>
						<div class="mt-2 text-center">

							<input type="submit" class="btn btn-success"> <input
								type="reset" class="btn btn-warning">
						</div>
					</form>
				</div>
			</div>
		</div>

		<div class="mt-2">
			<div class="h3">All Sales Dtls</div>
			
			<table class="table table-bordered table-striped mt-2">
				<thead>
					<tr>
						<th>Sl.#</th>
						<th>Customer Name</th>
						<th>Product Name</th>
						<th>Sales Date</th>
						<th>Qnty</th>
						<th>Unit Price</th>
						<th>Total Price</th>	
						<th>Action</th>					
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${salesList}" var="sales" varStatus="counter">
						<tr>
							<td>${counter.count}</td>
							<td>${sales.customer.customerName}</td>
							<td>${sales.product.productName}</td>
							<td>${sales.salesDate}</td>
							<td>${sales.salesQnty}</td>	
							<td>${sales.product.unitPrice}</td>
							<td>${sales.salesQnty*sales.product.unitPrice} </td>
							<td><a class='btn btn-danger' href="./delSales?sId=${sales.salesId}">Del</a>&nbsp;
							<a class='btn btn-info' href="./upSales?sId=${sales.salesId}">Update</a></td>						
						</tr>
					</c:forEach>

				</tbody>

			</table>
			
		</div>




	</div>
	<script type="text/javascript">
	
	$(document).ready(function(){
		setTimeout(function(){
			document.getElementById("alId").style.display = 'none';
			}, 2000);
	});
	
	
	function validateForm(){
		var cId=$('#customerId').val();
		var pId=$('#productId').val();
		var sQnty=$('#salesQntyId').val();
		
		
		var status=true;
		if(cId==0){
			alert("Plz select a customer name!!");
			status=false;
		}
		if(pId==0){
			alert("Plz select a product!!");
			status=false;
		}
		if(sQnty==""){
			alert("plz provide some qnty");
			status=false;
		}else{
			
			var aQnty=Number($('#qnId').val());
			var sqn=Number(sQnty);
			if(sqn>aQnty){
				alert("Sales qnty must be same or less than available qnty!!");
				status=false;
				
			}
		}
		return status;
	}
	
	function getProductQntyByProductId(pId) {
			
		$.ajax({
				type : 'GET',
				url : 'getProductQntyByProductId',
				data : {
					productId : pId
					
				},
				success : function(resp) {
					
					$('#qnId').val(resp);
				}
			});

		}
	
	
		
	</script>
</body>

</html>