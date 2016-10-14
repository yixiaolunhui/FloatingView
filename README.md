# FloatingView
悬浮view，之前写的https://github.com/dalong982242260/SuspendedBall  有点问题，现在换成方式实现。



##效果图

![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating1.gif?raw=true)
![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating1_2.gif?raw=true)
![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating2_1.gif?raw=true)
![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating2_2.gif?raw=true)
![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating2_3.gif?raw=true)
![image](https://github.com/dalong982242260/FloatingView/blob/master/img/floating3_1.gif?raw=true)


##说明
|name|format|description|
|:---:|:---:|:---:|
| dl_duration | long |设置归位时间
| dl_percent | float |设置显示百分比
| dl_scale | float |设置按下view缩放值


##使用方式

xml：
      <com.dalong.floatview.FloatingView
            android:id="@+id/floatingView"
            app:dl_duration="100"
            app:dl_scale="1.4"
            app:dl_percent="0.8"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
            
java：
            floatingView=(FloatingView)findViewById(R.id.floatingView);
            View view= LayoutInflater.from(this).inflate(R.layout.item_view,null);
            ImageView image= (ImageView) view.findViewById(R.id.test_img);
    
            floatingView.addFloatingView(view)
                    .setFloatingDuration(200L)
                    .setFloatingPercent(0.5f)
                    .setFloatingScale(1f);
    
            Glide.with(this)
                    .load("http://img2.imgtn.bdimg.com/it/u=2679297709,1421413045&fm=21&gp=0.jpg")
                    .placeholder(R.mipmap.ic_launcher)
                    .into(image);