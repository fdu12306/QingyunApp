<?php
    session_start();
    include './mysqlFunc.php';
    //接收前端发送的登录信息
    $studentId = isset($_POST['studentId']) ? $_POST['studentId'] : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';
    //根据用户名判断用户是否存在
    $userData=getUserDataByStudentId($studentId);
    if($userData==null){
        $response = array(
            'state' => 4003,
            'message' => '学生号'.$studentId.'不存在'
        );
        echo json_encode($response);
        exit();
    }
    // 哈希加盐处理密码
    $salt = $userData['salt']; // 随机生成一个盐值
    $hashed_password = hash('sha256', $password . $salt); // 将密码和盐值拼接后进行哈希加密
    if($hashed_password!=$userData['password']){
        $response = array(
            'state' => 4004,
            'message' => '密码不正确'
        );
        echo json_encode($response);
        exit();
    }
    $_SESSION['id'] = $userData['id'];
    $_SESSION['studentId'] = $userData['studentId'];
    $_SESSION['username']=$userData['username'];
    $_SESSION['logged'] = true;
    $response = array(
        'state' => 200,
        'message' => '登录成功'
    );
    echo json_encode($response);

?>
