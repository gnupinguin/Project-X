<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
      <title>Quotations</title>
   </head>
   <style>
      body{
      background: url(images/database.jpg);
      background-size: cover;
      font-family: "Montserrat",sans-serif;
      font-size: 16px;
      background-position: center center;
      background-repeat: no-repeat;
      background-attachment: fixed;
      font-family: "Montserrat",sans-serif;
      font-size: 32px;
      }
   </style>
<body>
   <div class="container">
  <h1 align="center">MY COLLECTION</h1><br>



    <#list quotes?keys as quotestext>
        <div class="media">
            <div class="media-body">
                <p>${quotestext}</p>
                <h4 class="media-heading" align="right">${quotes[quotestext]}</h4>
            </div>
        </div>
        <hr>
    <#--${key} = ${quotes[key]}-->
    </#list>


</body>
</html>
