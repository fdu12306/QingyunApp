<?php
     session_start();
     include './mysqlFunc.php';
      //用户已登录
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        $sold=getSoldByUserId($_SESSION['id']);
        $response=array(
            'state'=>3000,
            'data'=>count($sold)
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'state'=>3000,
            'data'=>0
        );
        echo json_encode($response);
        exit();
     }