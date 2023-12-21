<?php
    include './mysqlModel.php';

    //检查学生号是否重复函数 4000
    function checkStudentId($studentId) {
        $sql = "SELECT * FROM user WHERE studentId = ?";
        $params = array($studentId);
        $result = total($sql, $params);
        return ($result == 0);
    }

    //将用户数据插入数据库 5000
    function insertUserData($username,$studentId, $password, $salt) {
        $sql = "INSERT INTO user (username,studentId, password, salt) VALUES (?,?, ?, ?)";
        $params = array($username,$studentId, $password, $salt);
        $result = dml($sql, $params);
        return ($result > 0);
    }

    //根据学生号获取用户数据 4003
    function getUserDataByStudentId($studentId) {
        $sql = "SELECT * FROM user WHERE studentId = ?";
        $params = array($studentId);
        return get($sql, $params);
    }

    //插入商品
    function insertProduct($userId, $productName, $campus, $category, $price, $description, $issueTime, $imagePath,$soldState) {
        $sql = "INSERT INTO product (userId, productName, campus, category, price, description, issueTime, imagePath, soldState,deleteState) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,0)";
        $params = array($userId, $productName, $campus, $category, $price, $description, $issueTime, $imagePath,$soldState);
        $result = dml($sql, $params);
        return ($result > 0);
    }

    //获取最新发布的艺术品 
    function getNewestProducts(){
        $sql="select * from product order by issueTime desc limit 9";
        return select($sql);
    }

    //获取商品数据
    function getProductById($id){
        $sql="select * from product where id = ?";
        $params=array($id);
        return get($sql,$params);
    }

    //获取某一类商品
    function getCategory($category){
        $sql="select * from product where category= ?";
        $params=array($category);
        return select($sql,$params);
    }
    
    //根据商品名字搜索
    function searchByProductName($input) {
        $sql = "SELECT * FROM product WHERE productName LIKE ? or description LIKE ?";
        $params = array("%$input%","%$input%");
        return select($sql, $params);
    }    

    //查找收藏
    function selectCollect($userId, $productId) {
        $sql = "SELECT * FROM collect WHERE userId=? AND productId=?";
        $params = array($userId, $productId);
        $result = total($sql, $params);
        if ($result > 0) {
            return false;
        }
        return true;
    }

    //添加收藏
    function insertCollect($userId, $productId) {
        $sql = "INSERT INTO collect (userId, productId, priceThreshold) VALUES (?, ?, 0)";
        $params = array($userId, $productId);
        $result = dml($sql, $params);
        return ($result>0);
    }

    //修改商品状态
    function modifyProduct($productId,$soldTime){
        $sql="Update product set soldTime=?, soldState=1 where id=?";
        $params=array($soldTime,$productId);
        $result=dml($sql,$params);
        return $result>0;
    }

    //加入订单
    function insertOrder($userId,$productId,$orderTime){
        $sql="Insert into orders (userId,productId,orderTime) values (?,?,?)";
        $params=array($userId,$productId,$orderTime);
        $result=dml($sql,$params);
        return $result>0;
    }

    //添加通知消息
    function insertMessage($userId,$productId,$time,$remark){
        $sql="Insert into message (userId,productId,time,remark,isRead) values (?,?,?,?,0)";
        $params=array($userId,$productId,$time,$remark);
        $result=dml($sql,$params);
        return $result>0;
    }

    //根据商品Id获取收藏
    function getCollectByProductId($productId){
        $sql="Select * from collect where productId=?";
        $params=array($productId);
        return select($sql,$params);
    }

    //获取用户的收藏
    function getCollectByUserId($userId){
        $sql="Select p.id,p.productName,p.price,p.imagePath,c.priceThreshold from product p join collect c on p.id=c.productId where c.userId=?";
        $params=array($userId);
        return select($sql,$params);
    }

    //移出收藏
    function removeCollect($userId,$productId){
        $sql="Delete from collect where userId=? and productId=?";
        $params=array($userId,$productId);
        $result=dml($sql,$params);
        return $result>0;
    }

     //设计价格下限
     function setPriceThreshold($userId,$productId,$priceThreshold){
        $sql="Update collect set priceThreshold=? where userId=? and productId=?";
        $params=array($priceThreshold,$userId,$productId);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取已发布商品
    function getIssueByUserId($userId){
        $sql="Select * from product where userId=? and soldState=0";
        $params=array($userId);
        return select($sql,$params);
    }

     //获取已卖出商品
     function getSoldByUserId($userId){
        $sql="Select * from product where userId=? and soldState=1 and deleteState=0";
        $params=array($userId);
        return select($sql,$params);
    }

    //获取已购买商品
    function getOrderByUserId($userId){
        $sql="Select * from orders where userId=?";
        $params=array($userId);
        return select($sql,$params);
    }

    //删除商品
    function deleteProduct($id){
        $sql="Delete from product where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }

    //修改商品，包括图片
    function modifyProductDataWithImage($id,$productName,$description,$campus,$category,$price,$imagePath){
        $sql="Update product set productName=?,campus=?,category=?,price=?,description=?,imagePath=? where id=?";
        $params=array($productName,$campus,$category,$price,$description,$imagePath,$id);
        $result=dml($sql,$params);
        return $result>0;
    }

     //修改商品，不包括图片
     function modifyProductDataExceptImage($id,$productName,$description,$campus,$category,$price){
        $sql="Update product set productName=?,campus=?,category=?,price=?,description=? where id=?";
        $params=array($productName,$campus,$category,$price,$description,$id);
        $result=dml($sql,$params);
        return $result>0;
    }

    //删除出售记录
    function deleteSold($id){
        $sql="Update product set deleteState=1 where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取订单信息
    function getOrderDataByUserId($userId){
        $sql="Select o.orderTime,p.* from orders o join product p on o.productId=p.id where o.userId=?";
        $params=array($userId);
        return select($sql,$params);
    }

    //删除订单
    function deleteOrder($userId,$productId){
        $sql="Delete from orders where userId=? and productId=?";
        $params=array($userId,$productId);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取消息
    function getMessageByUserId($userId){
        $sql="Select * from message where userId=? order by time desc";
        $params=array($userId);
        return select($sql,$params);
    }

    //标记已读
    function readMessage($id){
        $sql="Update message set isRead=1 where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }

     //删除消息
     function deleteMessage($id){
        $sql="Delete from message where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }

    //发布评论
    function insertComment($userId,$productId,$content,$issueTime){
        $sql="Insert into comment (userId,productId,content,issueTime,deleteState) values (?,?,?,?,0)";
        $params=array($userId,$productId,$content,$issueTime);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取商品的评论
    function getCommentByProductId($productId){
        $sql="Select c.*,u.username from comment c join user u on c.userId=u.id where c.productId=? order by c.issueTime desc";
        $params=array($productId);
        return select($sql,$params);
    }

    //删除评论
    function deleteComment($id){
        $sql="Update comment set deleteState=1 where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取未删除的评论
    function getCommentByUserId($userId){
        $sql="Select * from comment where userId=? and deleteState=0 order by issueTime desc";
        $params=array($userId);
        return select($sql,$params);
    }

    //删除商品的评论
    function deleteCommentByProductId($productId){
        $sql="Update comment set deleteState=1 where productId=?";
        $params=array($productId);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取最新的历史记录
    function getRecorderByUserIdAndProductId($userId,$productId){
        $sql="Select * from visitrecorder where userId=? and productId=? order by visitTime desc limit 1";
        $params=array($userId,$productId);
        return get($sql,$params);
    }

    //插入历史记录
    function insertVisitRecorder($userId,$productId,$visitTime){
        $sql="Insert into visitrecorder (userId,productId,visitTime) values (?,?,?)";
        $params=array($userId,$productId,$visitTime);
        $result=dml($sql,$params);
        return $result>0;
    }

    //获取浏览记录
    function getBrowserByUserId($userId){
        $sql="Select v.*,p.productName,p.imagePath,p.price from visitrecorder v join product p on v.productId=p.id where v.userId=? order by v.visitTime desc";
        $params=array($userId);
        return select($sql,$params);
    }

    //删除历史记录
    function deleteBrowser($id){
        $sql="Delete from visitrecorder where id=?";
        $params=array($id);
        $result=dml($sql,$params);
        return $result>0;
    }