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

		</head>


		<body>



			<div class="container mt-5">
				<div class="h2 text-center text-danger">
					Employee Regd. Form
				</div>
				<div class="card">
					<div class="card-header bg-light h3 text-primary">Emp Form</div>

					<div class="card-body">
						<div class="container">
							<form action="./saveEmp" method="post">
								<input type="hidden" name="empId" value="${e.employeeId}">
								<div class="row">
									<div class="col-4">
										<label for="enameId" class="font-weight-bold">Emp Name</label>
										<input type="text" class="form-control" name="eName" id="enameId" value="${e.name}">
									</div>
									<div class="col-2">
										<label for="esalId" class="font-weight-bold">Salary</label>
										<input type="text" class="form-control" name="eSal" id="esalId" value="${e.salary}">
									</div>
									<div class="col-2">
										<label for="ehdateId" class="font-weight-bold">Hire Date</label> 
										<input type="date" class="form-control" name="ehDate" id="ehdateId" value=<fmt:formatDate pattern="yyyy-MM-dd" value="${e.hireDate}"/>>
									</div>
									<div class="col-4">
										<label for="ehdateId" class="font-weight-bold">Employment Type</label>
										<div>
										
											
											<input type="radio" name="empType" id="empTypeIdc"
												value="contractual"  <c:if test="${e.employmentType == 'contractual'}">checked='checked'</c:if> >&nbsp;&nbsp;Contractual
											<input type="radio" name="empType" id="empTypeIdp"
												value="permanent" <c:if test="${e.employmentType == 'permanent'}">checked='checked'</c:if>>&nbsp;&nbsp;Permanent
										</div>
									</div>
									<div class="col-4">
										<label for="deptnameId" class="font-weight-bold">Department Id</label>
										<select name="deptId" id="deptnameId" class="form-control">
											<option value="0">-select-</option>
											
											<c:forEach items="${deptList}" var="dept">
												<option  value="${dept.deptId}" <c:if test="${dept.deptId == e.departments.deptId}">selected='selected'</c:if>  >${dept.deptId} | ${dept.getName()}
												</option>
											</c:forEach>
										</select>
									</div>
									
								</div>
								<div class="mt-2 text-center">
									
									
										
										
									
									<input type="submit" value="${e eq null ?'save':'update'}" class="btn btn-success">
									<input type="reset" class="btn btn-warning">
								</div>
							</form>
						</div>
					</div>
				</div>

				<div class="mt-2">
					<div class="h3">All Emp Data</div>
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Sl.#</th>
								<th>Emp Id</th>
								<th>Name</th>
								<th>Salary</th>
								<th>Hire Date</th>
								<th>Emp Type</th>
								<th>Department</th>
								<th>Action</th>
							</tr>

						</thead>

						<tbody>
								<c:forEach items="${allEmps}" var="emp" varStatus="counter">
									<tr>
										<td>${counter.count}</td>
										<td>${emp.employeeId}</td>
										<td>${emp.name}</td>
										<td>${emp.salary}</td>										
										<td><fmt:formatDate pattern="dd-MM-yyyy" value="${emp.hireDate}"/></td>
										<td>${emp.employmentType}</td>
										<td>${emp.departments.name}</td>
										<td>
											<a class="btn btn-danger" href="./deleteEmp?empId=${emp.employeeId}">Del</a>
											<a class="btn btn-warning" href="./updateEmp?empId=${emp.employeeId}">Update</a>
										</td>
									</tr>
								</c:forEach>

						</tbody>

					</table>



				</div>




			</div>

		</body>

		</html>