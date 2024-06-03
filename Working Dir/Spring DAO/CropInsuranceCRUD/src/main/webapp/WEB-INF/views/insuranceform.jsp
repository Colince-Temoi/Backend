<!-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> -->
<!DOCTYPE html>
<html>

<head>
	<meta charset="ISO-8859-1">
	<title>Crop Insurance Form</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
		integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-2.2.4.js"
		integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>

</head>


<body>
	<div class="container mt-5">

		<c:if test="${msg ne null}">
			<div class="alert alert-success" id="alId">${msg}</div>

		</c:if>

		<div class="h2 text-center text-danger">Crop Insurance Form</div>
		<div class="card">
			<div class="card-header bg-light h3 text-primary">Crop Insurance Form</div>

			<div class="card-body">
				<div class="container">
					<form action="./saveInsurance" method="post" enctype="multipart/form-data"
						onsubmit="return validateForm();">
						<input type="hidden" name="insId" id="insuranceId" value="${uinsurance.insuranceId}">

						<div class="row">
							<div class="col-6">
								<label for="seasonId" class="font-weight-bold">Season</label>
								<select name="season" id="seasonId" class="form-control"
									onchange="getCropBySeasonId(this.value)">
									<option value="0">-select-</option>
									<c:forEach items="${seasonList}" var="season">
										<option value="${season.seasonId}" <c:if
											test="${season.seasonId eq uinsurance.crop.season.seasonId}">
											selected='selected'
											</c:if> >${season.seasonName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-6">
								<label for="cropId" class="font-weight-bold">Crop Name</label>
								<select name="crop" id="cropId" class="form-control">
									<option value="0">-select-</option>
									<c:forEach items="${cropsList}" var="crop">
										<option value="${crop.cropId}" <c:if
											test="${crop.cropId eq uinsurance.crop.cropId}">selected='selected'
											</c:if>>${crop.cropName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="row mt-3">
							<div class="col-3">
								<label for="farmId" class="font-weight-bold">Farmer Name</label>
								<input type="text" class="form-control" name="farmerName" id="farmerNameId"
									value="${uinsurance.farmerName}">
							</div>
							<div class="col-3">
								<label for="aadhId" class="font-weight-bold">Aadhaar No</label>
								<input type="text" class="form-control" name="aadhaarNo" id="aadhaarNoId"
									value="${uinsurance.aadhaarNo}">
							</div>
							<div class="col-3">
								<label for="fathId" class="font-weight-bold">Father Name</label>
								<input type="text" class="form-control" name="fatherName" id="fatherNameId"
									value="${uinsurance.fatherName}">
							</div>
							<div class="col-3">
								<label for="adId" class="font-weight-bold">Complete Address</label>
								<input type="text" class="form-control" name="address" id="addressId"
									value="${uinsurance.address}">
							</div>
						</div>
						<label class="mt-3 font-weight-bold">Farmer Category:</label>
						<div class="form-group row mx-3">
							<div class="col-sm-10">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="farmerCategory" id="radioSmall"
										value="small" <c:if test='${uinsurance.farmerCategory eq "small"}'>checked
									</c:if>>
									<label class="form-check-label" for="radioSmall">
										Small
									</label>
								</div>

								<div class="form-check form-check-inline">
									<input <c:if test='${uinsurance.farmerCategory eq "medium"}'>checked
									</c:if> class="form-check-input" type="radio" name="farmerCategory" id="radioMedium"
									value="medium">
									<label class="form-check-label" for="radioMedium">
										Medium
									</label>
								</div>

								<div class="form-check form-check-inline">
									<input <c:if test='${uinsurance.farmerCategory eq "large"}'>checked
									</c:if> class="form-check-input" type="radio" name="farmerCategory" id="radioLarge"
									value="large">
									<label class="form-check-label" for="radioLarge">
										Large
									</label>
								</div>
							</div>
						</div>
						<div class="mt-2 text-center">

							<input type="submit" class="btn btn-success"> <input type="reset" value="Clear"
								class="btn btn-warning">
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
						<th>Season</th>
						<th>Crop Name</th>
						<th>Farmer Name</th>
						<th>Aadhaar No</th>
						<th>Father Name</th>
						<th>Address</th>
						<th>Farmer Category</th>
						<th>Action</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${insuranceList}" var="insurance" varStatus="counter">
						<tr>
							<td>${counter.count}</td>
							<td>${insurance.crop.season.seasonName}</td>
							<td>${insurance.crop.cropName}</td>
							<td>${insurance.farmerName}</td>
							<td>${insurance.aadhaarNo}</td>
							<td>${insurance.fatherName}</td>
							<td>${insurance.address} </td>
							<td>${insurance.farmerCategory}</td>
							<td><a class='btn btn-danger'
									href="./delInsurance?insId=${insurance.insuranceId}">Delete</a>&nbsp;
								<a class='btn btn-warning' href="./upInsurance?insId=${insurance.insuranceId}">Update</a>
							</td>
						</tr>
					</c:forEach>

				</tbody>

			</table>

		</div>




	</div>
	<script type="text/javascript">

		$(document).ready(function () {
			setTimeout(function () {
				document.getElementById("alId").style.display = 'none';
			}, 2000);
		});


		function validateForm() {
			var sId = $('#seasonId').val();
			var cId = $('#cropId').val();
			var farmerName = $('#farmerNameId').val();
			var aadhaarNo = $('#aadhaarNoId').val();
			var fatherName = $('#fatherNameId').val();
			var address = $('#addressId').val();
			var selectedRadio = $('input[name=farmerCategory]:checked');

			var status = true;

			if (sId == 0) {
				alert("Please select a season!!");
				status = false;
			}

			if (cId == 0) {
				alert("Please select a crop!!");
				status = false;
			}

			if (farmerName == "" || !/^[a-zA-Z0-9 ]{1,50}$/.test(farmerName)) {
				alert("Please provide a valid Farmer Name (up to 50 characters, no special characters).");
				status = false;
			}

			if (aadhaarNo == "" || !/^[0-9]{1,12}$/.test(aadhaarNo)) {
				alert("Please provide a valid Aadhaar Number (up to 12 digits).");
				status = false;
			}

			if (fatherName == "" || !/^[a-zA-Z0-9 ]{1,12}$/.test(fatherName)) {
				alert("Please provide a valid Father's Name (up to 12 alphanumeric characters).");
				status = false;
			}

			if (address == "" || !/^[a-zA-Z0-9 ]{1,250}$/.test(address)) {
				alert("Please provide a valid Address (up to 250 characters, no special characters).");
				status = false;
			}

			if (selectedRadio.length <= 0) {
				alert("Please select a category");
				status = false;
			}

			if (status) {
				$.ajax({
					type: 'GET',
					url: 'checkIfAadharExistsForCrop',
					data: {
						aadhaarNo: aadhaarNo,
						cropId: cId
					},
					async: false,
					success: function (resp) {
						if (resp.indexOf('1') !== -1) {
							var selectedCrop = $("#cropId option:selected").text();
							alert("Aadhaar duplicate found for " + selectedCrop);
							status = false;
						}
					}
				});
			}

			return status;
		}


		function getCropBySeasonId(sId) {

			$.ajax({
				type: 'GET',
				url: 'getCropsBySeasonId',
				data: {
					seasonId: sId

				},
				success: function (resp) {

					$('#cropId').html(resp);
				}
			});

		}



	</script>
</body>

</html>