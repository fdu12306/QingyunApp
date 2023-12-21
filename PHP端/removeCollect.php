<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['productId'])?$_POST['productId']:'';
     if(removeCollect($_SESSION['id'],$productId)){
        $response=array(
            'state'=>200,
            'message'=>"移出收藏成功！"
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'message'=>"移出收藏失败！"
        );
        echo json_encode($response);
        exit();
     }