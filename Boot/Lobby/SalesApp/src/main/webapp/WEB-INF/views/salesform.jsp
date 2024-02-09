<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

			<!DOCTYPE html>
			<html>

			<head>
				<meta charset="UTF-8">
				<title>Sales Form</title>
				<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
					integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
					crossorigin="anonymous">

				<script src="https://code.jquery.com/jquery-2.2.4.min.js"
					integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>

			</head>

			<body>
				<!-- Testing to see if saleToUpdate is coming on clicking update button -->
				<!-- ${saleToUpdate} -->

				<div class="container mt-5">

					<c:if test="${message ne null}">
						<div class="alert alert-success" id="alertId">
							${message}
						</div>
					</c:if>
					<div class="h2 text-center text-danger">Customer Sales Form</div>
					<div class="card">
						<div class="card-header bg-light h3 text-primary">Sales Form</div>
						<div class="card-body">
							<div class="container">
								<!-- If  onsubmit == false form is not valid and hence will not be submitted!-->
								<form action="./saveSale" method="post" enctype="multipart/form-data"
									onsubmit="return validateForm()">
									<input type="hidden" name="salesId" value="${saleToUpdate.salesId}">
									<div class="row">
										<div class="col-4">
											<label for="customerId" class="font-weight-bold">Customer</label>
											<select name="customer" id="customerId" class="form-control">
												<option value="0">-select-</option>
												<c:forEach items="${customerList}" var="customer">
													<option value="${customer.customerId}" <c:if
														test="${customer.name == saleToUpdate.customer.name}">
														selected='selected'</c:if>>${customer.name}</option>
												</c:forEach>
											</select>
										</div>
										<div class="col-4">
											<label for="productId" class="font-weight-bold">Product</label>
											<select name="product" id="productId" class="form-control"
												onchange="getQuantityByProductId(this.value)">
												<option value="0">-select-</option>
												<c:forEach items="${productList}" var="product">
													<option value="${product.productId}" <c:if
														test="${product.name == saleToUpdate.product.name}">
														selected='selected'</c:if>>${product.name}</option>
												</c:forEach>
											</select>
										</div>

										<div class="col-2">
											<label for="qtyId" class="font-weight-bold">Available Quantity</label>
											<input type="text" readonly="readonly" class="form-control"
												name="productStock" id="qtyId"
												value="${saleToUpdate.product.productStock}" />
										</div>

										<div class=" col-2">
											<label for="noOfUnitsId" class="font-weight-bold">Number of Units</label>
											<input type="text" class="form-control" name="noOfUnits" id="noOfUnitsId"
												value="${saleToUpdate.noOfUnits}" />
										</div>
									</div>

									<div class=" text-center mt-3">
										<!-- <div class="btn-group"> -->
										<input type="submit" value="${saleToUpdate eq null ?'Submit':'update'}"
											class="btn btn-success"> <input type="reset" class="btn btn-warning">
										<!-- </div> -->
									</div>

								</form>

							</div>
						</div>
					</div>
					<div class="mt-2">
						<div class="h3">All Sales Data</div>

						<table class="table table-bordered table-striped mt-2">
							<thead>
								<tr>
									<th>Sl.#</th>
									<th>Customer Name</th>
									<th>Sale Id</th>
									<th>Product Name</th>
									<th>Sale Date</th>
									<th>Quantity</th>
									<th>Action</th>
								</tr>

							</thead>

							<tbody>
								<c:forEach items="${salesList}" var="sale" varStatus="counter">
									<tr>
										<td>${counter.count}</td>
										<td>${sale.customer.name}</td>
										<td>${sale.salesId}</td>
										<td>${sale.product.name}</td>
										<td>
											<fmt:formatDate pattern="dd-MM-yyyy" value="${sale.salesDate}" />
										</td>
										<td>${sale.noOfUnits}</td>
										<td><a href="./delSale?sid=${sale.salesId}"
												class="btn btn-danger">Delete</a>&nbsp;&nbsp;<a
												href="./getSaleToUpdateById?sid=${sale.salesId}"
												class="btn btn-warning ">Update</a>
										</td>
									</tr>
								</c:forEach>

							</tbody>

						</table>

					</div>

				</div>

				<!-- Scripting Area -->
				<script type="text/javascript">


					$("#alertId").show();
					setTimeout(function () { $("#alertId").hide(); }, 1000);

					function validateForm() {
						//  Get the default values associated with each input field
						var c_name = $('#customerId').val(); // 0
						var p_name = $('#productId').val(); // 0
						var inputtedUnits = Number($('#noOfUnitsId').val()); // 0 or ""

						// console.log(inputtedUnits);

						var status = true;

						// Validation logic
						if (c_name == 0) {
							alert("Please enter name!");
							status = false;
						}
						if (p_name == 0) {
							alert("Please Choose a Product!");
							status = false;
						}
						if (inputtedUnits == "") {
							alert("Please Enter the number of Units you want to sell!");
							status = false;
						} else {
							//  check that the inputted untits are not > max required
							var requiredAvailableStock = Number($('#qtyId').val());

							if (inputtedUnits > requiredAvailableStock) {
								alert("You cant sell more than is required! Try Selling less or same as the required!")
								status = false;
							}
						}
						// status = true;
						// console.log(inputtedUnits);
						return status;
					}


					function getQuantityByProductId(productId) {
						// console.log("Js is working..." + productId);
						$.ajax({
							type: 'GET',
							url: 'getQuantityByProductId',
							data: {
								productId: productId
							},
							success: function (resp) {
								// alert(resp);
								$('#qtyId').val(resp);
							}
						});
					}
				</script>

			</body>

			</html>