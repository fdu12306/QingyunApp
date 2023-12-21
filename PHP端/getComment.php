<?php
     session_start();
     include './mysqlFunc.php';
      //获取商品id
    $productId = isset($_POST['productId']) ? $_POST['productId'] : '';
    $comments=getCommentByProductId($productId);
    if(isset($_SESSION['logged']) && $_SESSION['logged']){
        foreach ($comments as $key => $comment) {
            if ($_SESSION['id'] == $comment['userId']) {
                // 修改原数组中的元素
                $comments[$key]['isOwner'] = 1;
            }
        }
    }
    $response=array(
        'state'=>200,
        'data'=>$comments
    );
    echo json_encode($response);
    exit();