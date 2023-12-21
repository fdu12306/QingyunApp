<?php
     session_start();
     include './mysqlFunc.php';
     //获取艺术品id
     $productId=isset($_POST['id'])?$_POST['id']:'';
      //用户已登录
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        //判断是否已经收藏了商品
        if(!selectCollect($_SESSION['id'],$productId)){
            $response=array(
                'state'=>4007,
                'message'=>"您已收藏该商品！"
            );
            echo json_encode($response);
            exit();
        }
        //添加进收藏
        else{
            //加入收藏
            if(!insertCollect($_SESSION['id'],$productId)){
                $response=array(
                    'state'=>5000,
                    'message'=>"数据导入数据库失败，请联系系统管理员！"
                );
                echo json_encode($response);
                exit();
            }
            else{
                $response=array(
                    'state'=>200,
                    'message'=>"加入收藏成功！"
                );
                echo json_encode($response);
                exit();
            }
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