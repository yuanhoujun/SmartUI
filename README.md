# Smart UI
**Smart UI**主要提供了Android开发过程中经常用到的一些UI控件，该库会持续开发，力求解决Android开发过程中的UI痛点
问题！目前**Smart UI**已经提供了如下控件供大家使用:

### RoundedTextView
**RoundedTextView**是一个圆形的TextView控件，可以作为展示新消息提示之类的通知控件。
该控件有如下属性:
```xml
<me.foji.smartui.RoundedTextView
    android:layout_width="30dp"
    android:layout_height="30dp"
    android:text="10"
    android:textSize="14dp"
    android:textColor="@android:color/white"
    app:fillColor="#ff0000"
    app:radius="15dp"
    app:autoTextSize="true" />
```

### TabBar
**TabBar**是一个组合控件，通常与**TabBarGroup**配合使用，使用方法类似RadioButton和RadioGroup，该控件支持的属性
较多，具体如下：
```xml
<me.foji.smartui.TabBarGroup
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal"
        android:background="#fcfcfc"
        app:checkedButton="@+id/tab_bar_home"
        android:id="@+id/main_tab_bar">
        <me.foji.smartui.TabBar
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            app:imageDrawable="@drawable/selector_tab_bar_home"
            app:textColor="@drawable/tab_bar_textcolor"
            app:text="@string/home"
            app:textSize="12dp"
            android:id="@+id/tab_bar_home"/>
        <me.foji.smartui.TabBar
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            app:imageDrawable="@drawable/selector_tab_bar_discover"
            app:textColor="@drawable/tab_bar_textcolor"
            app:text="@string/discover"
            app:textSize="12dp"
            android:id="@+id/tab_bar_discover"/>
        <me.foji.smartui.TabBar
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            app:imageDrawable="@drawable/selector_tab_bar_message"
            app:textColor="@drawable/tab_bar_textcolor"
            app:text="@string/message"
            app:textSize="12dp"
            app:badgeVisible="true"
            app:badgeText="3"
            app:badgeRadius="8dp"
            app:badgeFillColor="#ff0000"
            app:badgeTextColor="@android:color/white"
            app:badgeTextSize="10dp"
            app:badgeMarginRight="-3dp"
            app:badgeMarginTop="0dp"
            android:id="@+id/tab_bar_message"/>
        <me.foji.smartui.TabBar
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            app:imageDrawable="@drawable/selector_tab_bar_mine"
            app:textColor="@drawable/tab_bar_textcolor"
            app:text="@string/mine"
            app:textSize="12dp"
            android:id="@+id/tab_bar_mine"/>
    </me.foji.smartui.TabBarGroup>
```
具体使用方法请参照例子[sample]()

如果你有更多很有意思的控件，欢迎Fork这个仓库，推送Pull Request