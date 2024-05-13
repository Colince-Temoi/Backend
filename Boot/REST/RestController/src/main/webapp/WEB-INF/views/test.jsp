<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
      integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
      crossorigin="anonymous"
    />
    <script
      src="https://code.jquery.com/jquery-2.2.4.js"
      integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI="
      crossorigin="anonymous"
    ></script>
  </head>

  <body>
    <div class="container mt-3">
      <form action="">
        <input type="button" value="send Json Data" onclick="sendJsonData()" />
      </form>
    </div>

    <!-- Scripting Area -->
    <script type="text/javascript">
      function sendJsonData() {
        // console.log("Js is working...");
        const employeeList = [
          {
            emp_id: 1,
            emp_name: "John Doe",
            sal: 50000,
            hire_date: "2022-01-15",
            department: {
              dep_id: 101,
              dept_name: "HR",
            },
          },
          {
            emp_id: 2,
            emp_name: "Jane Smith",
            sal: 60000,
            hire_date: "2021-11-20",
            department: {
              dep_id: 102,
              dept_name: "Finance",
            },
          },
          {
            emp_id: 3,
            emp_name: "David Johnson",
            sal: 55000,
            hire_date: "2023-03-10",
            department: {
              dep_id: 103,
              dept_name: "Marketing",
            },
          },
          {
            emp_id: 4,
            emp_name: "Emily Brown",
            sal: 52000,
            hire_date: "2020-09-05",
            department: {
              dep_id: 104,
              dept_name: "IT",
            },
          },
        ];

        console.log(typeof employeeList, employeeList);

        $.ajax({
          type: "POST",
          url: "saveData",
          data: {
            jsonData: JSON.stringify(employeeList),
          },
          success: function (resp) {
            alert(resp);
            // $("#qtyId").val(resp);
          },
        });
      }
    </script>
  </body>
</html>
