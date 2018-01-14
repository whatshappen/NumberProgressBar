# NumberProgressBar
有进度值提示的进度条，可选择进度样式

# 水平进度： #

<P>使用方式：</P>

	<com.whatshappen.numberprogressbar.numberprogressbar.NumberHorizontalProgressBar
		android:id="@+id/num_pb" 
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:layout_marginTop="50dp"
		npb:percent_size="15sp"
		npb:progress_bar_height="3dp"
		npb:progress_bar_style="top"/> 


# 圆形进度： #

<P>使用方式：</P>
	
	<com.whatshappen.numberprogressbar.numberprogressbar.CircleProgressBar
		android:id="@+id/cpb" 
		android:layout_width="250dp"
		android:layout_height="250dp" 
		cpb:progress_bar_start_round="90"/> 


效果1：  
![img](https://github.com/whatshappen/NumberProgressBar/blob/master/screenshots/progressbar_top.jpg)  

效果2：  
![img](https://github.com/whatshappen/NumberProgressBar/blob/master/screenshots/progressbar_center.jpg)  

效果3：  
![img](https://github.com/whatshappen/NumberProgressBar/blob/master/screenshots/progressbar_bottom.jpg)  

#动态效果：#

水平进度条：  
![img](https://github.com/whatshappen/NumberProgressBar/blob/master/screenshots/progressbar.gif) 


 
圆形进度条：  
![img](https://github.com/whatshappen/NumberProgressBar/blob/master/screenshots/progressbar_cir.gif)  
## License
Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)  
<pre>
Copyright 2017 whatshappen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
