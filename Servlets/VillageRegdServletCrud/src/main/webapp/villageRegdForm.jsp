<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

			<!DOCTYPE html>
			<html>

			<head>
				<meta charset="UTF-8">
				<title>Insert title here</title>
				<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
					integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
					crossorigin="anonymous">
				<script src="https://code.jquery.com/jquery-2.2.4.min.js"
					integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>

			</head>


			<body>
				<div class="container mt-5">
					<div class="h2 text-center text-danger">Village Regd. Form</div>
					<div class="card">
						<div class="card-header bg-light h3 text-primary">Village Form</div>

						<div class="card-body">
							<div class="container">
								<form action="./saveVlg" method="post" enctype="multipart/form-data">
									<input type="hidden" name="vId" value="${v.villageId}">
									<div class="row">
										<div class="col-4">
											<label for="vnameId" class="font-weight-bold">Village
												Name</label> <input type="text" class="form-control" name="vName"
												id="vnameId" value="${v.name}">
										</div>
										<div class="col-2">
											<label for="popId" class="font-weight-bold">Population</label> <input
												type="text" class="form-control" name="vPop" id="popId"
												value="${v.population}">
										</div>


										<div class="col-4">
											<label for="countyId" class="font-weight-bold">County</label>
											<select name="county" id="countyId" class="form-control"
												onchange="getAllConstituenciesByCountyId(this.value)">
												<option value="0">-select-</option>
												<c:forEach items="${countyList}" var="county">
													<option value="${county.countyId}">${county.name}</option>
												</c:forEach>
											</select>
										</div>


										<div class="col-4">
											<label for="constituencyId" class="font-weight-bold">Constituency</label>
											<select name="constituency" id="constituencyId" class="form-control">
												<option value="0">-select-</option>

											</select>
										</div>
										<div class="col-4">
											<label for="adocId" class="font-weight-bold">Upload Auth
												Doc</label> <input type="file" class="form-control" name="adoc"
												id="adocId">
										</div>
									</div>
									<div class="mt-2 text-center">

										<input type="submit" class="btn btn-success"> <input type="reset"
											class="btn btn-warning">
									</div>
								</form>
							</div>
						</div>
					</div>

					<div class="mt-2">
						<div class="h3">All Village Data</div>
						<!-- We are not saving things to the Server end, we are retriving the things hance for the form we require  to make its method attribute as GET -->

						<!-- <div class="container"> -->
						<form method="get" action="countyFilter">
							<div class="row">
								<div class="col-3">
									<!-- <label for="countyfilterId" class="font-weight-bold">Filter By County</label> -->
									<select name="countyfilter" id="countyfilterId" class="form-control"
										onchange="getAllConstituenciesByCountyId(this.value)">
										<option value="0">-select-</option>
										<c:forEach items="${countyList}" var="county">
											<option value="${county.countyId}">${county.name}</option>
										</c:forEach>
									</select>
								</div>
								<input type="submit" class="btn btn-success" value="Search" class="form-control">
							</div>
						</form>


						<table class="table table-bordered table-striped mt-2">
							<thead>
								<tr>
									<th>Sl.#</th>
									<th>Village Id</th>
									<th>Village Name</th>
									<th>Population</th>
									<th>Constituency Name</th>
									<th>County Name</th>
									<th>Doc. Path</th>
								</tr>

							</thead>

							<tbody>
								<c:forEach items="${villageList}" var="vlg" varStatus="counter">
									<tr>
										<td>${counter.count}</td>
										<td>${vlg.villageId}</td>
										<td>${vlg.name}</td>
										<td>${vlg.population}</td>
										<td>${vlg.constituency.name}</td>
										<td>${vlg.constituency.county.name}</td>
										<td><a href="./downloadFile?fileName=${vlg.authDoc}">${vlg.authDoc}</a></td>
									</tr>
								</c:forEach>

							</tbody>

						</table>

						<div>
							<c:forEach items="${pageList}" var="pn">
								<a class="btn btn-success" href="./getRegdForm?pageNo=${pn-1}">${pn}</a>
							</c:forEach>


						</div>


					</div>




				</div>

				<!-- Scripting Area -->
				<script type="text/javascript">
					function getAllConstituenciesByCountyId(countyId) {
						// console.log("Js is working..." + countyId);
						$.ajax({
							type: 'GET',
							url: 'getConstituenciesByCountyId',
							data: {
								countyId: countyId
							},
							success: function (resp) {
								// alert(resp);
								$('#constituencyId').html(resp);
							}
						});
					}
				</script>
			</body>

			</html>