<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['productId'])?$_POST['productId']:'';
     $priceThreshold=isset($_POST['priceThreshold'])?$_POST['priceThreshold']:'';
     if(setPriceThreshold($_SESSION['id'],$productId,$priceThreshold)){
        $response=array(
            'state'=>200,
            'message'=>"设置成功！"
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'message'=>"设置失败！"
        );
        echo json_encode($response);
        exit();
     }