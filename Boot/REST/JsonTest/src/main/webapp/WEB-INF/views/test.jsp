<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <title>InteropTest - Test 3</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  </head>
  <body>
    <h1>Test 3: Sending JSON Data to Server</h1>
    <button id="sendDataBtn">Send JSON Data</button>

    <script>
      // Step 4: Define a JavaScript array of product object literals
      var products = [
        {
          id: 1,
          title: "Smart Watch",
          price: 199.99,
          category: "electronics",
          description: "A high-tech smartwatch with fitness tracking.",
        },
        {
          id: 2,
          title: "Gold Necklace",
          price: 120.0,
          category: "jewelery",
          description: "An elegant gold necklace with a dainty pendant.",
        },
        // Add more products here...
      ];

      function sendJsonData() {
        // Step 5: Make an AJAX call to send the product data to the server
        $.ajax({
          url: "/JsonTestApp/product",
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify(products),
          success: function (response) {
            // Display a message when the data is sent successfully
            console.log(response);
            alert("Data sent successfully!");
          },
          error: function (jqXHR, textStatus, errorMessage) {
            // Handle error, if any
            console.error("Error sending data:", errorMessage);
          },
        });
      }

      $(document).ready(function () {
        $("#sendDataBtn").click(sendJsonData);
      });
    </script>
  </body>
</html>

<!-- <!DOCTYPE html>
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

    <script type="text/javascript">
      function sendJsonData() {
        // console.log("Js is working...");

        const productList = [
          {
            id: 1,
            title: "Smart Watch",
            price: 199.99,
            category: "electronics",
            description:
              "A high-tech smartwatch with fitness tracking, heart rate monitor, and smart notifications.",
          },
          {
            id: 2,
            title: "Gold Necklace",
            price: 120.0,
            category: "jewelery",
            description:
              "A elegant gold necklace with a dainty pendant, perfect for everyday wear.",
          },
          {
            id: 3,
            title: "Comfortable T-Shirt",
            price: 24.99,
            category: "clothing",
            description:
              "Soft and comfortable cotton t-shirt in a trendy solid color.",
          },
          {
            id: 4,
            title: "Bluetooth Speaker",
            price: 79.99,
            category: "electronics",
            description:
              "Portable Bluetooth speaker with great sound quality and long battery life.",
          },
          {
            id: 5,
            title: "Pair of Earrings",
            price: 69.99,
            category: "jewelery",
            description:
              "A stylish pair of gold hoop earrings for a touch of glamour.",
          },
        ];

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
        console.log(typeof productList, productList);

        $.ajax({
          type: "POST",
          url: "/JsonTestApp/saveData",
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
</html> -->
