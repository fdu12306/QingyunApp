<?php
    session_start();
    // 判断用户是否登录
    if (isset($_SESSION['logged']) && $_SESSION['logged']) {
        // 注销登录状态
        session_unset();
        session_destroy();
        $response = array(
            'loggedout' => true
        );
    } 
    else {
        $response = array(
            'loggedout' => false
        );
    }
    echo json_encode($response);
?>