<?php
    include './mysqlFunc.php';
    $category=isset($_POST['category'])?$_POST['category']:'';
    $products=getCategory($category);
    $response=array(
        'state'=>200,
        'data'=>$products
    );
    echo json_encode($response);