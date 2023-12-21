<?php
    include './mysqlFunc.php';
    //接收前端发送的登录信息
    $input = isset($_POST['input']) ? $_POST['input'] : '';
    $product=searchByProductName($input);
    $response = array(
        'state' => 200,
        'data' => $product
    );
    echo json_encode($response);
    exit();