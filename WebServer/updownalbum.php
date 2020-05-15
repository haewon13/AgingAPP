<?php
$target_dir = "./camera/";
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
$uploadOk = 1;
$imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

/*
// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
    $check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
    if($check !== false) {
        echo "File is an image - " . $check["mime"] . ".";
        $uploadOk = 1;
    } else {
        echo "File is not an image.";
        $uploadOk = 0;
    }
}
// Check if file already exists
if (file_exists($target_file)) {
    echo "Sorry, file already exists.";
    $uploadOk = 0;
}

// Check file size
if ($_FILES["fileToUpload"]["size"] > 5000000) {
    echo "Sorry, your file is too large.";
    $uploadOk = 0;
}*/

// Allow certain file formats
if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
&& $imageFileType != "gif" ) {
    echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.";
    $uploadOk = 0;
}
// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
                $filename = $_FILES["fileToUpload"]["name"];
                $imgurl = "http://203.255.176.79:13000/repos_camera/". $_FILES["fileToUpload"]["name"];
                $size = $_FILES["fileToUpload"]["size"];

                $conn = mysqli_connect("localhost", "root", "cjsdpdnjs", "test");

                $sql = "insert into imagetest(filename, imgurl, size) values('$filename','$imgurl','$size')";
                mysqli_query($conn,$sql);
                mysqli_close($conn);

        echo "<p>The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.</p>";

                #system("sudo whoami");
                chdir("/home/user01/mytrain");
                #echo getcwd()."\n";
                exec("/home/user01/venv/bin/python servercrop.py /var/www/html/camera/".$filename);
                exec("/home/user01/venv/bin/python test.py");
                chdir("/home/user01/var/www/html");
                #echo getcwd()."\n";


                /*if($imageFileType == "jpg"){
                echo "<br><img src=/save_camera/". basename( $_FILES["fileToUpload"]["name"])."_1.jpg width=400>";}*/

                exec("mv /var/www/html/camera/".basename($_FILES["fileToUpload"]["name"])." /var/www/html/repos_camera");

                $isfile = $_SERVER['DOCUMENT_ROOT']."/save_camera/".$filename."_1.jpg";
                #echo $isfile;
                clearstatcache();
                if(file_exists($isfile)){
                        http_response_code(200);
                        #echo "success";
                }
                else{
                        http_response_code(404);
                }

                #echo "<br><button type='button' onclick='history.back()'>돌아가기</button>";
    } else {
        echo "<p>Sorry, there was an error uploading your file.</p>";
                #echo "<br><button type='button' onclick='history.back()'>돌아가기</button>";
    }
}
?>
                       
