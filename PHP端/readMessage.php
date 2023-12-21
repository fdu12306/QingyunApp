<?php
     session_start();
     include './mysqlFunc.php';
     //获取消息id
     $id=isset($_POST['id'])?$_POST['id']:'';
     if(readMessage($id)){
        $response=array(
            'state'=>200,
            'message'=>"标记成功！"
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'message'=>"标记失败！"
        );
        echo json_encode($response);
        exit();
     }