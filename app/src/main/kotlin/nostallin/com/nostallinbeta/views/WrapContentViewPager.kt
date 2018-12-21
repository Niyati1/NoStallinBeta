package nostallin.com.nostallinbeta.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * [ViewPager] subclass that respects the wrap_content attribute
 */
class WrapContentViewPager : ViewPager {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0

        /*
         * Determine the height of the largest child and
         * use that height as the height of the ViewPager
         */
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) {
                height = h
            }
        }

        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}