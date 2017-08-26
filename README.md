# CircleProgress
一个简单的圆形进度（带进度动画）A simple round of progress with animation


![](https://github.com/sunheihei/CircleProgress/blob/master/app/src/main/res/drawable/demopic.png)



#XML

```
    <com.custom.sun.circleprogresslibrary.CircleProgressView
        android:id="@+id/circleprogress"
        android:layout_width="200dp"
        android:layout_height="200dp"

        app:FirstColor="#00FF00"
        app:SecondColor="#FFFF00"
        app:ThirdColor="#FF0000"


        app:TopText="This is TitleText"
        app:TopTextSize="15sp"
        app:BottomText="This is BottomText"
        app:BottomTextSize="10sp"

        app:StartArcMode="1"
        app:MainTextSize="50sp"
        app:MainTextColor="@android:color/black"
        app:MainTextStroke="5px"
        app:BgCircleColor="@android:color/darker_gray"
        app:ProgressCircleStorke="10dp"/>
	
```


#Activity

```

  mCircleProgressView.SetProgress(65);
        mCircleProgressView.SetAnimation(true);
        //动画时长和起始数值
        mCircleProgressView.SetAnimationDuration(2000,0);

```

支持三种颜色，数字滚动动画

#Use
```
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

```
 compile 'com.github.sunheihei:CircleProgress:v1.0.1'
```
