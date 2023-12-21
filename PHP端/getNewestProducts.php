<?php
    include './mysqlFunc.php';
    $newestProducts=getNewestProducts();
    $response=array(
        'state'=>200,
        'data'=>$newestProducts
    );
    echo json_encode($response);