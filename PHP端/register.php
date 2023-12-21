<?php
   session_start();
   include './mysqlFunc.php';
   //接收前端发送的注册信息
   $username=isset($_POST['username'])?$_POST['username']:'';
   $studentId = isset($_POST['studentId']) ? $_POST['studentId'] : '';
   $password = isset($_POST['password']) ? $_POST['password'] : '';
   //检查学生号是否重复
   if(!checkStudentId($studentId)){
      $response = array(
         'state' => 4000,
         'message' => '学生号'.$studentId.'已经注册过了'
     );
     echo json_encode($response);
     exit();
   }

   //用哈希加盐对密码进行加密
   //生成随机盐
   $salt=bin2hex(random_bytes(16));
   $hashed_password=hash('sha256',$password.$salt);
   if(!insertUserData($username,$studentId,$hashed_password,$salt)){
      $response = array(
         'state' => 5000,
         'message' => '用户数据插入数据库出现问题，请联系系统管理员'
     );
     echo json_encode($response);
     exit();
   }
   $response = array(
      'state' => 200,
      'message' => '注册成功'
  );
  echo json_encode($response);

   
  
