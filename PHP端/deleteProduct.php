<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['productId'])?$_POST['productId']:'';
     $product=getProductById($productId);
     if(deleteProduct($productId)){
        //添加商品删除的消息
        $remark="您收藏的商品".$product['productName']."已被发布者删除！";
        $collects=getCollectByProductId($productId);
        $time=date('Y-m-d H:i:s');
        if(!empty($collects)){
            foreach($collects as $collect){
                //添加商品被删除消息
                insertMessage($collect['userId'],$productId,$time,$remark);
                //移出收藏
                removeCollect($collect['userId'],$productId);
            }
        }
        //删除评论
        deleteCommentByProductId($productId);
        $response=array(
            'state'=>200,
            'message'=>"删除成功！"
        );
        echo json_encode($response);
        exit();
     }
     else{
        $response=array(
            'message'=>"删除失败！"
        );
        echo json_encode($response);
        exit();
     }