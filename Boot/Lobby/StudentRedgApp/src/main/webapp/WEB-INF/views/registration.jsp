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

	<c:if test="${msg ne null}">
		<div class="alert alert-success" id="alId">${msg}</div>
	</c:if>

	<div class="container mt-5">

		<div class="h2 text-center text-warning">Student Registration
			Form</div>
		<div class="card">

			<div class="card-body">
				<div class="container">
					<form action="./saveAdmissionDetails" method="post"
						enctype="multipart/form-data">


						<div class="row mt-3">
							<div class="col-4">
								<label for="studentNameId" class="font-weight-bold">Student
									Name</label> <input type="text" class="form-control" name="studentName"
									id="studentNameId" maxlength="50">
							</div>
							<div class="col-4">
								<label for="emailId" class="font-weight-bold">Email ID </label>
								<input type="text" class="form-control" name="email"
									id="emailId" maxlength="50">
							</div>
							<div class="col-4">
								<label for="dobId" class="font-weight-bold">Date of
									Birth</label> <input type="date" class="form-control" name="dob"
									id="dobId">
							</div>

						</div>
						<div class="row mt-3">
							<div class="col-4">
								<label for="cgpaId" class="font-weight-bold">CGPA</label> <input
									type="text" class="form-control" name="cgpa" id="cgpaId">
							</div>
							<div class="col-4">
								<label for="yearOfAdmissionId" class="font-weight-bold">Year
									of Admission</label> <select name="yearOfAddmission"
									id="yearOfAdmissionId" class="form-control">
									<option value="0">-select-</option>
									<!-- Like this we are using scriptlets to populate the years to the dropdown. This is not recommended as it is a horrible thing to do! It will make the page slow. No one uses it. So should you! -->
									<!-- <c:forEach var="year" begin="2010"
													end="<%=java.time.Year.now().getValue()%>">
													<option value="${year}">${year}</option>
												</c:forEach> -->

									<c:forEach items="${dateList}" var="date">
										<option value="${date}">${date}</option>
									</c:forEach>

								</select>
							</div>
							<div class="col-4">
								<label for="branchId" class="font-weight-bold">Branch</label> <select
									name="branch" id="branchId" class="form-control">
									<option value="0">-select-</option>
									<c:forEach items="${branchList}" var="branch">
										<option value="${branch.branchId}">${branch.branchName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<hr>
						<!-- Address Details Section -->
						<div class="row mt-3">
							<div class="col-4">
								<label for="laneId" class="font-weight-bold">Lane</label> <input
									type="text" class="form-control" name="addresses[0].lane" id="laneId"
									maxlength="50">
							</div>



							<div class="col-4">
								<label for="stateId" class="font-weight-bold">State</label> <select
									name="addresses[0].state" id="stateId" class="form-control">
									<option value="0">-select-</option>
									<c:forEach items="${stateList}" var="state">
										<option value="${state.stateId}">${state.stateName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-4">
								<label for="cityId" class="font-weight-bold">City</label> <select
									name="addresses[0].state.cityId" id="cityId" class="form-control">
									<option value="0">-select-</option>
									<!-- You may dynamically populate the city dropdown based on the selected state using JavaScript or JSTL -->
								</select>
							</div>
							<div class="col-4">
								<label for="zipId" class="font-weight-bold">Zip Code</label> <input
									type="text" class="form-control" name="addresses[0].zip" id="zipId"
									maxlength="10">
							</div>
						</div>

						<hr>
						<!-- Course Details Section -->
						<div class="row mt-3">
							<div class="col-4">
								<label for="courseId" class="font-weight-bold">Course</label> <select
									name="courses[0].courseId" id="courseId" class="form-control"
									onchange="displayCourseFees(this.value)">
									<option value="0">-select-</option>
									<c:forEach items="${courseList}" var="course">
										<option value="${course.courseId}">${course.courseName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-4">
								<label for="feesId" class="font-weight-bold">Course Fees</label>
								<input type="text" class="form-control" name="fees" id="feesId"
									readonly>
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
			<div class="h3">All Students Dtls</div>

			<table class="table table-bordered table-striped mt-2">
				<thead>
					<tr>
						<th>Sl.#</th>
						<th>Email ID</th>
						<th>Date of Birth</th>
						<th>CGPA</th>
						<th>Year of Admsn</th>
						<th>Adress[Lane,City,State]</th>
						<th>Course Name</th>
						<th>Course Fee</th>
					</tr>

				</thead>

				<tbody>
					<c:forEach items="${studentList}" var="student" varStatus="counter">
            <tr>
                <td>${counter.count}</td>
                <td>${student.email}</td>
                <td><fmt:formatDate pattern="yyyy-MM-dd" value="${student.dob}" /></td> 
                <td>${student.cgpa}</td>
                <td>${student.yearOfAddmission}</td>

                <td>
                    <c:forEach items="${student.addresses}" var="address"> 
                        ${address.lane},${address.state.stateName}<br/> 
                    </c:forEach>
                </td> 

                <td>
                    <c:forEach items="${student.courses}" var="course"> 
                        ${course.courseName}<br/>  
                    </c:forEach>
                </td> 

                <td>
                    <c:forEach items="${student.courses}" var="course"> 
                        ${course.fees}<br/>
                    </c:forEach>
                </td> 
            </tr>
        </c:forEach>

				</tbody>

			</table>

		</div>

		<script type="text/javascript">
			$(document)
					.ready(
							function() {
								setTimeout(
										function() {
											document.getElementById("alId").style.display = 'none';
										}, 2000);		

										$('#stateId').on('change', function() {
            var stateId = $(this).val();
            if (stateId) {
                getCitiesList(stateId); // Calling the modified function
            } else {
                $('#cityId').empty(); 
            }
        });

							}
							);

							function getCitiesList(stateId) {
        $.ajax({
            type: 'GET',
            url: 'getCitiesByState', 
            data: {
                stateId: stateId // Using 'stateId' for consistency
            },
            success: function(resp) {
							console.log(resp); // Log the response to see what you're getting
							// data is the JSON string returned from the server
							//var cities = $.parseJSON(resp)
							//console.log(cities);
        // Populate the cities dropdown with the received data
        $.each(resp, function(index, city) {
            $('#cityId').append("<option value='" + city.cityId + "'>" + city.cityName + "</option>");
        });
}
        });
    }

			function validateForm() {
				console.log("yes");

				var sId = $('#seasonId').val();
				var cId = $('#cropNameId').val();
				var fnameId = $('#farmerNameId').val();
				var fathNameId = $('#fatherNameId').val();
				var adharId = $('#adharNoId').val();
				var addId = $('#addressId').val();
				var fcId = $('input[name="farmerCategory"]:checked').val();
				var status = true;
				if (sId == 0) {
					alert("Plz select a customer !!!");
					status = false;
				}
				if (cId == 0) {
					alert("Plz select a product !!!");
					status = false;
				}
				if (fnameId == "") {
					alert("Plz provide farmer name");
					status = false;
				}
				if (fathNameId == "") {
					alert("Plz provide father name");
					status = false;
				}
				if (adharId == "") {
					alert("Plz provide adhar id");
					status = false;
				} else if (!isValidAadhar(adharId)) {
					alert("Aadhar ID should be 12 digits");
					status = false;
				}
				if (addId == "") {
					alert("Plz provide address");
					status = false;
				}
				if (!fcId) {
					alert("Plz select farmer category");
					status = false;
				}

				return status;
			}
			function isValidAadhar(aadhar) {
				var aadharPattern = /^\d{14}$/;
				return aadharPattern.test(aadhar);
			}

			function displayCourseFees(courseId) {
				$.ajax({
					type : 'GET',
					url : 'getCourseFees',
					data : {
						courseId : courseId
					},
					success : function(resp) {
						$('#feesId').val(resp)
					}
				});
			}
		</script>
	</div>
</body>
</html>