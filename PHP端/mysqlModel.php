<?php
    include './databaseConfig.php';

    //执行增、删、改操作
    function dml($sql, $params = []) {
        $conn = mysqli_connect(HOST, USER, PWD, DBNAME) or die("连接或选择数据库失败！");
        // 使用预处理语句
        $stmt = mysqli_prepare($conn, $sql);
        if ($stmt === false) {
            // 输出错误信息并中止脚本执行
            die("预处理语句创建失败！" . mysqli_error($conn));
        }
        if (count($params) > 0) {
            // 获取参数类型字符串
            $types = '';
            foreach ($params as $param) {
                if (is_int($param)) {
                    $types .= 'i';
                } elseif (is_float($param)) {
                    $types .= 'd';
                } else {
                    $types .= 's';
                }
            }
            // 将参数绑定到查询语句中
            mysqli_stmt_bind_param($stmt, $types, ...$params);
        }
        mysqli_stmt_execute($stmt);
        $affected_rows = mysqli_affected_rows($conn);
        $id = mysqli_insert_id($conn);
        mysqli_stmt_close($stmt);
        mysqli_close($conn);
        return !empty($id) ? $id : $affected_rows;
    }

    //查询多条数据
    function select($sql, $params = []) {
        $conn = mysqli_connect(HOST, USER, PWD, DBNAME) or die("连接或选择数据库失败！");
        // 使用预处理语句
        $stmt = mysqli_prepare($conn, $sql);
        if ($stmt === false) {
            // 输出错误信息并中止脚本执行
            die("预处理语句创建失败！" . mysqli_error($conn));
        }
        if (count($params) > 0) {
            // 获取参数类型字符串
            $types = '';
            foreach ($params as $param) {
                if (is_int($param)) {
                    $types .= 'i';
                } elseif (is_float($param)) {
                    $types .= 'd';
                } else {
                    $types .= 's';
                }
            }
            // 将参数绑定到查询语句中
            mysqli_stmt_bind_param($stmt, $types, ...$params);
        }
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);
        $arr = array();
        if ($result && mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                $arr[] = $row;
            }
        }
        mysqli_stmt_close($stmt);
        mysqli_close($conn);
        return $arr;
    }


    //查询单条数据
    function get($sql, $params = []) {
    $conn = mysqli_connect(HOST, USER, PWD, DBNAME) or die("连接或选择数据库失败！");
    // 使用预处理语句
    $stmt = mysqli_prepare($conn, $sql);
    if ($stmt === false) {
        // 输出错误信息并中止脚本执行
        die("预处理语句创建失败！" . mysqli_error($conn));
    }
    if (count($params) > 0) {
        // 获取参数类型字符串
        $types = '';
        foreach ($params as $param) {
            if (is_int($param)) {
                $types .= 'i';
            } elseif (is_float($param)) {
                $types .= 'd';
            } else {
                $types .= 's';
            }
        }
        // 将参数绑定到查询语句中
        mysqli_stmt_bind_param($stmt, $types, ...$params);
    }
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);
    $row = null;
    if ($result && mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
    }
    mysqli_stmt_close($stmt);
    mysqli_close($conn);
    return $row;
}

    //执行统计
    function total($sql, $params = []) {
        $conn = mysqli_connect(HOST, USER, PWD, DBNAME) or die("连接或选择数据库失败！");
        // 使用预处理语句
        $stmt = mysqli_prepare($conn, $sql);
        if ($stmt === false) {
            // 输出错误信息并中止脚本执行
            die("预处理语句创建失败！" . mysqli_error($conn));
        }
        if (count($params) > 0) {
            // 获取参数类型字符串
            $types = '';
            foreach ($params as $param) {
                if (is_int($param)) {
                    $types .= 'i';
                } elseif (is_float($param)) {
                    $types .= 'd';
                } else {
                    $types .= 's';
                }
            }
            // 将参数绑定到查询语句中
            mysqli_stmt_bind_param($stmt, $types, ...$params);
        }
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);
        $row = 0;
        if ($result && mysqli_num_rows($result) > 0) {
            $row = mysqli_num_rows($result);
        }
        mysqli_stmt_close($stmt);
        mysqli_close($conn);
        return $row;
    }
