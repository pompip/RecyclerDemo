# RecyclerDemo
## FlowLayoutManager
* 动态加载和隐藏子布局.
* 主要重写onLayoutChildren,scrollVerticallyBy两个方法.
* 需要考虑的细节太多了.
* 向上滑动,如果过快不能正常工作,没时间完善.
* 代码如果丑陋,注定bug多.

## TextNumberItemAnimator
* 实现翻页时钟的样式
* 重写ItemSimpleAnimator 实现Item change动画.
* 自定义View,不通过RecyclerView应该能更节约资源.