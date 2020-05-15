<?php
$target_dir1 = "./album/";
$target_dir2 = "./repos_album/";
$target_file1 = $target_dir1 . basename($_FILES["fileToUpload"]["name"]);
$target_file2 = $target_dir2 . basename($_FILES["fileToUpload2"]["name"]);
$uploadOk = 1;
$imageFileType1 = strtolower(pathinfo($target_file1,PATHINFO_EXTENSION));
$imageFileType2 = strtolower(pathinfo($target_file2,PATHINFO_EXTENSION));


// Allow certain file formats
if($imageFileType1 != "jpg" && $imageFileType1 != "png" && $imageFileType1 != "jpeg"
&& $imageFileType1 != "gif" ) {
    echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.";
    $uploadOk = 0;
}
// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file1) 
        && move_uploaded_file($_FILES["fileToUpload2"]["tmp_name"], $target_file2)) {
                $filename1 = $_FILES["fileToUpload"]["name"];
                $filename2 = $_FILES["fileToUpload2"]["name"];
                $imgurl1 = "http://203.255.176.79:13000/repos_album/". $_FILES["fileToUpload"]["name"];
                $imgurl2 = "http://203.255.176.79:13000/repos_album/". $_FILES["fileToUpload2"]["name"];
                $size = $_FILES["fileToUpload"]["size"];

                $conn = mysqli_connect("localhost", "root", "cjsdpdnjs", "test");

                $sql = "insert into imagealbum(past_filename, past_imgurl, present_filename, present_imgurl) values('$filename1','$imgurl1','$filename2', $imgurl2)";
                mysqli_query($conn,$sql);
                mysqli_close($conn);

                echo "<p>The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.</p>";

                #system("sudo whoami");
                chdir("/home/user01/mytrain");
                #echo getcwd()."\n";
                exec("/home/user01/venv/bin/python test2.py");
                exec("/home/user01/venv/bin/python test.py");
                chdir("/home/user01/var/www/html");
      

                /*if($imageFileType == "jpg"){
                echo "<br><img src=/save_camera/". basename( $_FILES["fileToUpload"]["name"])."_1.jpg width=400>";}*/

                exec("mv /var/www/html/album/".basename($_FILES["fileToUpload"]["name"])." /var/www/html/repos_album");

                $isfile = $_SERVER['DOCUMENT_ROOT']."/save_camera/".$filename."_1.jpg";
                #echo $isfile;
                clearstatcache();
                if(file_exists($isfile)){
                        http_response_code(200);
                }
                else{
                        http_response_code(404);
                }

    } else {
        echo "<p>Sorry, there was an error uploading your file.</p>";
    }
}
?>
                       
