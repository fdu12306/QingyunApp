<?php
     session_start();
     include './mysqlFunc.php';
     $id=isset($_POST['id'])? $_POST['id'] : '';
     $productName = isset($_POST['productName']) ? $_POST['productName'] : '';
     $description = isset($_POST['description']) ? $_POST['description'] : '';
     $campus = isset($_POST['campus']) ? $_POST['campus'] : '';
     $category = isset($_POST['category']) ? $_POST['category'] : '';
     $price = isset($_POST['price']) ? $_POST['price'] : '';
     $product=getProductById($id);
     //修改了图片
     if (isset($_FILES['image'])) {
         //删除原有图片
         $dir="./upload/";
         $imgName=basename($product['imagePath']);
         $imgPath=$dir.$imgName;
         unlink($imgPath);
         //添加图片
         $image=$_FILES['image'];
         $ext=pathinfo($image['name'],PATHINFO_EXTENSION);
         $filename=uniqid().'.'.$ext;
         $upload_dir='http://192.168.40.115:8082/';
         move_uploaded_file($image['tmp_name'],$dir.$filename);
         $imagePath=$upload_dir.$filename;
         modifyProductDataWithImage($id,$productName,$description,$campus,$category,$price,$imagePath);
     }
     else{
        modifyProductDataExceptImage($id,$productName,$description,$campus,$category,$price);
     }
     //判断价格是否发生改变，如果发生改变，发送相应消息
     if($product['price']!=$price){
        $collects=getCollectByProductId($id);
        if(!empty($collects)){
            $time=date('Y-m-d H:i:s');
            foreach($collects as $collect){
                if($price<=$collect['priceThreshold']){
                    $remark="您收藏的商品".$productName."当前的价格为¥".$price."，已低于您设置的期望价格¥".$collect['priceThreshold'];
                    insertMessage($collect['userId'],$id,$time,$remark);
                }
            }
        }
     }
     $response=array(
        'state'=>200,
        'message'=>"修改成功！"
    );
    echo json_encode($response);
    exit();
