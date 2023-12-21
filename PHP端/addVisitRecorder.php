<?php
     session_start();
     include './mysqlFunc.php';
     if(isset($_SESSION['logged']) && $_SESSION['logged']){
        $productId=isset($_POST['productId'])?$_POST['productId']:'';
        $userId=$_SESSION['id'];
        $visitTime=date('Y-m-d H:i:s');
        $visitRecorder=getRecorderByUserIdAndProductId($userId,$productId);
        if($visitRecorder!=null){
            // 将日期时间字符串转换为Unix时间戳
            $timestamp1 = strtotime($visitTime);
            $timestamp2 = strtotime($visitRecorder['visitTime']);
            // 计算两个时间戳之间的差值
            $interval = abs($timestamp1 - $timestamp2);
            // 两次时间间隔大于一天则产生访问记录
            if($interval>24*60*60){
                insertVisitRecorder($userId,$productId,$visitTime);
            }
        }
        else{
            insertVisitRecorder($userId,$productId,$visitTime);
        }
     }