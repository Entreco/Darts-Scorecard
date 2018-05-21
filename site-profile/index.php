<!DOCTYPE html>
<html lang="en" >

<head>
  <meta charset="UTF-8">
  <title>Darts Scorecard</title>
    <link rel="stylesheet" href="css/style.css">
    <?php 
        include 'profile.php';
        $url = $_SERVER['REQUEST_URI'];
        $profile = new Profile(end(explode('/', $url)));
    ?>
</head>

<body>

  <div id="pricing-table" class="clear">
   
   
    <div class="plan" id="most-popular">
        <h3><?php echo $profile->{'name'}(); ?><span>D16</span></h3>
        <a class="signup" href="">Download</a>        
        <ul>
            <li><b>98.67</b> Average</li>
            <li><b>3</b> 100+</li>
            <li><b>--</b> 140+</li>
            <li><b>--</b> 180's</li>
			<li><b>Download</b> here</li>			
        </ul>    
    </div>
   
</div>
  

    <script  src="js/index.js"></script>

</body>

</html>
