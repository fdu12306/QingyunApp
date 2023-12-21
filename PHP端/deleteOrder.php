<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['productId'])?$_POST['productId']:'';
     $userId=$_SESSION['id'];
     if(deleteOrder($userId,$productId)){
        $response=array(
            'state'=>200,
            'message'=>"删除成功！"
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'message'=>"删除失败！"
        );
        echo json_encode($response);
        exit();
     }