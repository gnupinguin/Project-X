<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
       <link rel="stylesheet" href="css/database.css" type="text/css">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="js/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
      <title>Quotations</title>
   </head>
   <#--<style>-->
      <#--body{-->
      <#--background: url(images/database.jpg);-->
      <#--background-size: cover;-->
      <#--font-family: "Montserrat",sans-serif;-->
      <#--font-size: 16px;-->
      <#--background-position: center center;-->
      <#--background-repeat: no-repeat;-->
      <#--background-attachment: fixed;-->
      <#--font-family: "Montserrat",sans-serif;-->
      <#--font-size: 32px;-->
      <#--}-->
   <#--</style>-->
<body>
   <div class="container">
  <h1 align="center">MY COLLECTION</h1><br>

       <#list quotes as quote>
           <div class="media">
               <div class="media-body">
                   <p>${quote.text}</p>
                   <h4 class="media-heading" align="right">${quote.author}</h4>
               </div>
           </div>
           <hr>
       </#list>

</body>
</html>
