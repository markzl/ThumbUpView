# ThumbUpView
仿即刻点赞效果
## step1:
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
## step2:
Add the dependency in your moudle build.gradle:
```
	dependencies {
	        implementation 'com.github.markzl:ThumbUpView:0.0.2'
	}
```
## step3:
Use `ThumbUpView` in your layout:
```
    <com.markzl.android.thumbupview.ThumbUpView
        android:id="@+id/thumbUpView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:gravity="center"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:like_count="91"
        app:like_count_text_color="#cccccc"
        app:like_count_text_size="15sp"
        app:like_view_middle_padding="4dp"
        app:like_view_shining_visible="false"
        app:like_view_src_selected="@drawable/ic_messages_like_selected"
        app:like_view_src_shining="@drawable/ic_messages_like_selected_shining"
        app:like_view_src_unSelected="@drawable/ic_messages_like_unselected" />
```
