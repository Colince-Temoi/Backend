<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="ISO-8859-1">
                <title>EmployeeServletCrud</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
                    integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
                    crossorigin="anonymous">
                <link rel="stylesheet" href="css/bootstrap/bootstrap.css">
            </head>

            <body>
                <div class="container mt-5">
                    <div class="h3 text-danger text-center">
                        Registration Form
                    </div>
                    <div class="card">
                        <div class="card-header h3  bg-light text-primary">
                            Registration Form
                        </div>
                        <div class="card-body">
                            <!-- Its a good practice to put a form inside a container -->
                            <div class="container">
                                <form action="./saveEmp" method="post">
                                    <!-- One row conists 12 columns -->
                                    <div class="row">
                                        <div class="col-4">
                                            <label for="eid" class="text-secondary font-weight-bold">Emp Name</label>
                                            <input class="form-control" type="text" id="enameid" name="ename">
                                        </div>
                                        <div class="col-2">
                                            <label for="esalid" class="text-secondary font-weight-bold">Emp
                                                Salary</label>
                                            <input class="form-control" type="text" id="esalid" name="esal">
                                        </div>
                                        <div class="col-2">
                                            <label for="ehiredateid" class="text-secondary font-weight-bold">Hire
                                                date</label>
                                            <input class="form-control" type="date" id="ehiredateid" name="ehiredate">
                                        </div>
                                        <div class="col-4 form-group">
                                            <label for="emplomenttypeid" class="text-primary">Employment Type</label>
                                            <!-- For radio button make sure they go inside  div -->
                                            <div>
                                                <input type="radio" name="emplomenttype" id="emplomenttypeid"
                                                    value="permanent">&nbsp;&nbsp;
                                                Permanent
                                                <input type="radio" name="emplomenttype" id="emplomenttypeid"
                                                    value="contract">&nbsp;&nbsp;Contract
                                            </div>
                                        </div>
                                        <div class="col-lg-2 form-group">
                                            <label for="departmentid" class="text-primary">Department</label>
                                            <select class="form-control" name="department" id="departmentid">
                                                <!-- We will use this 0 to perform some validations
                                        If 0 means you have not selected -->


                                                <option value="0">-Select-</option>
                                                <c:forEach items="${deptlist}" var="dept">
                                                    <option value="${dept.deptId}">${dept.deptId}||${dept.name}</option>

                                                </c:forEach>

                                            </select>
                                        </div>
                                    </div>
                                    <div class=" text-center mt-3">
                                        <!-- <div class="btn-group"> -->
                                        <button type="submit" class="btn btn-success">Save</button>
                                        <button type="reset" class="btn btn-warning">Reset</button>
                                        <!-- </div> -->
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="mt-2">
                        <div class="h3">All Emp Data</div>
                    </div>
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
                            <c:forEach items="${emplist}" var="emp" varStatus="counter">
                                <tr>
                                    <!-- This will act as our Serial number -->
                                    <td>${counter.count}</td>
                                    <td>${emp.employeeId}</td>
                                    <td>${emp.name}</td>
                                    <td>${emp.salary}</td>
                                    <td>
                                        <fmt:formatDate pattern="dd-MM-yyyy" value="${emp.hireDate}" />
                                    </td>
                                    <td>${emp.employmentType}</td>
                                    <td>${emp.departments.name}</td>
                                    <td><a class="btn btn-danger" href="./deleteEmp?empId=${emp.employeeId}">Del</a>
                                        <a class="btn btn-warning" href="./updateEmp?empId=${emp.employeeId}">Update</a>
                                    </td>
                                </tr>
                            </c:forEach>

                        </tbody>

                    </table>

                </div>

            </body>

            </html>