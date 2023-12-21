<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['id'])?$_POST['id']:'';
      //用户已登录
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        //判断商品是否已售出
        $product=getProductById($productId);
        $soldState=$product['soldState'];
        if($soldState==1){
            $response=array(
                'state'=>4007,
                'message'=>"该商品已售出！"
            );
            echo json_encode($response);
            exit();
        }
        //加入购买
        else{
            $time=date('Y-m-d H:i:s');
            //修改商品状态
            if(!modifyProduct($productId,$time)){
                $response=array(
                    'state'=>4007,
                    'message'=>"修改商品状态失败！"
                );
                echo json_encode($response);
                exit();
            }
            //加入订单
            if(!insertOrder($_SESSION['id'],$productId,$time)){
                $response=array(
                    'state'=>4007,
                    'message'=>"加入订单失败！"
                );
                echo json_encode($response);
                exit();
            }
            //添加通知消息，通知发布者商品已被购买
            $remark="您发布的商品".$product['productName']."已被用户".$_SESSION['username']."购买！";
            if(!insertMessage($product['userId'],$productId,$time,$remark)){
                $response=array(
                    'state'=>4007,
                    'message'=>"生成通知消息失败！"
                );
                echo json_encode($response);
                exit();
            }
            //添加通知消息，通知收藏者商品已被购买
            $collects=getCollectByProductId($productId);
            if(!empty($collects)){
                foreach($collects as $collect){
                    $remark="您收藏的商品".$product['productName']."已经被其他用户购买";
                    insertMessage($collect['userId'],$productId,$time,$remark);
                }
            }
            $response=array(
                'state'=>200,
                'message'=>"购买成功！"
            );
            echo json_encode($response);
            exit();
        }
    }
    //未登录
    else{
        $response=array(
            'state'=>3000,
            'message'=>"请登录后再操作！"
        );
        echo json_encode($response);
        exit();
    }