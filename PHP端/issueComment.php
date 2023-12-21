<?php
     session_start();
     include './mysqlFunc.php';
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        $productId = isset($_POST['productId']) ? $_POST['productId'] : '';
        $content = isset($_POST['content']) ? $_POST['content'] : '';
        $issueTime=date('Y-m-d H:i:s');
        if(insertComment($_SESSION['id'],$productId,$content,$issueTime)){
            $response = array(
                'state' => 200,
                'message' => "发布成功！"
            );
            echo json_encode($response);
        }
        else{
            $response = array(
                'state' => 4000,
                'message' => "发布失败！"
            );
            echo json_encode($response);
        }
     }
     else{
        $response = array(
            'state' => 3000,
            'message' => "请登录后发布！"
        );
        echo json_encode($response);
     }