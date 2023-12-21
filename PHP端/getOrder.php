<?php
     session_start();
     include './mysqlFunc.php';
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        $userId=$_SESSION['id'];
        $order=getOrderDataByUserId($userId);
        $response=array(
            'state'=>200,
            'data'=>$order
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'state'=>3000,
            'message'=>"请登录后再查看！"
        );
        echo json_encode($response);
        exit();
     }
