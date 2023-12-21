<?php
     session_start();
     include './mysqlFunc.php';
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        //接收前端发送的注册信息
        $productName = isset($_POST['productName']) ? $_POST['productName'] : '';
        $description = isset($_POST['description']) ? $_POST['description'] : '';
        $campus = isset($_POST['campus']) ? $_POST['campus'] : '';
        $category = isset($_POST['category']) ? $_POST['category'] : '';
        $price = isset($_POST['price']) ? $_POST['price'] : '';
        //存储艺术品图片
        $image=$_FILES['image'];
        $ext=pathinfo($image['name'],PATHINFO_EXTENSION);
        $filename=uniqid().'.'.$ext;
        //图片存储路径
        $dir="./upload/";
        $upload_dir='http://192.168.40.115:8082/';
        move_uploaded_file($image['tmp_name'],$dir.$filename);
        $imagePath=$upload_dir.$filename;
        $issueTime=date('Y-m-d H:i:s');
        //将对应的信息存入数据库
        if(!insertProduct($_SESSION['id'],$productName,$campus,$category,$price,$description,$issueTime,$imagePath,0)){
            $response = array(
                'state' => 5000,
                'message' => "数据导入数据库失败，请联系系统管理员！"
            );
            echo json_encode($response);
            exit();
        }
        $response = array(
            'state' => 200,
            'message' => "发布成功！"
        );
        echo json_encode($response);
     }
     else{
        $response = array(
            'state' => 3000,
            'message' => "请登录后发布！"
        );
        echo json_encode($response);
     }
     



