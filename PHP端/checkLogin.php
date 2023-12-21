<?php
    session_start();
    include './mysqlFunc.php';
    //用户已登录
    if(isset($_SESSION['logged'])&&$_SESSION['logged']){
        $username=$_SESSION['username'];
        $response=array(
            'logged'=>true,
            'username'=>$username
        );
        echo json_encode($response);
        exit();
    }
    //未登录
    else{
        $response=array(
            'logged'=>false
        );
        echo json_encode($response);
        exit();
    }