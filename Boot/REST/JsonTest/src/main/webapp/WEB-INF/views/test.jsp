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

        let employee = {
          emp_id: 1234,
          emp_name: "John Doe",
          sal: 50000,
          hire_date: "2022-01-15",
          department: {
            dep_id: 456,
            dept_name: "Engineering",
          },
        };

        console.log(typeof employee, employee);

        $.ajax({
          type: "POST",
          url: "saveData",
          data: {
            jsonData: JSON.stringify(employee),
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
