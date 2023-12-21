<?php
    session_start();
    include './mysqlFunc.php';
    //获取艺术品id
    $id = isset($_POST['id']) ? $_POST['id'] : '';
    //根据艺术品id获取信息
    $product=getProductById($id);
    if($product==null){
        $response=array(
            'state'=>5001,
            'message'=>'获取商品数据异常，请联系系统管理员！'
        );
        echo json_encode($response);
        exit();
    }
    else{
        $response=array(
            'state'=>200,
            'data'=>$product
        );
        echo json_encode($response);
        exit();
    }