package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.swipe

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs

class RecyclerTouchListener(
    private val activity: Activity,
    private val view: RecyclerView
) : RecyclerView.OnItemTouchListener,
    OnActivityTouchListener {

    companion object {
        private const val TAG = "RecyclerTouchListener"
        private const val DEFAULT_VALUE_ZERO_FLOAT = 0.0f
        private const val DEFAULT_VALUE_ONE_FLOAT = 1.0f
        private const val DEFAULT_VALUE_ONE_AND_HALF_FLOAT = 1.5f
        private const val ANIMATION_CLOSE = 150L
        private const val ANIMATION_DURATION = 300L
        private const val DEFAULT_VALUE_ZERO = 0
        private const val DEFAULT_VALUE_ONE = 1
        private const val DEFAULT_VALUE_MINUS_ONE = -1
        private const val SHIFT_VALUE = 4
    }

    private var listViewChords = IntArray(2)
    private var rect: Rect = Rect()
    private val unSwipeRows: List<Int>

    /*
     * independentViews are views on the foreground layer which when clicked, activity "independent" from the foreground
     * ie, they are treated separately from the "row click" action
     */
    private val independentViews: List<Int>
    private val unClickableRows: List<Int>
    private var optionViews: List<Int>
    private val ignoredViewTypes: Set<Int>

    // Cached ViewConfiguration and system-wide constant values
    private val touchSlop: Int
    private val minFlingVel: Int
    private val maxFlingVel: Int
    private val animationClose: Long = ANIMATION_CLOSE
    private val animationDuration: Long = ANIMATION_DURATION
    private val animateType: Animation = Animation.CLOSE
    private var bgWidth = DEFAULT_VALUE_ONE

    // Transient properties
    private var mDismissAnimationRefCount = DEFAULT_VALUE_ZERO
    private var touchedX: Float = DEFAULT_VALUE_ZERO_FLOAT
    private var touchedY: Float = DEFAULT_VALUE_ZERO_FLOAT
    private var isFgSwiping: Boolean = false
    private var swipingSlop: Int = DEFAULT_VALUE_ZERO
    private var velocityTracker: VelocityTracker? = null
    private var touchedPosition: Int = DEFAULT_VALUE_ZERO
    private var touchedView: View? = null
    private var paused: Boolean = false
    private var bgVisible: Boolean = false
    private var fgPartialViewClicked: Boolean = false
    private var bgVisiblePosition: Int = DEFAULT_VALUE_ZERO
    private var bgVisibleView: View? = null
    private var isRViewScrolling: Boolean = false
    private var heightOutsideRView: Int = DEFAULT_VALUE_ZERO
    private var screenHeight: Int = DEFAULT_VALUE_ZERO

    // Foreground view (to be swiped), Background view (to show)
    private var fgView: View? = null
    private var bgView: View? = null

    //view ID
    private var bgViewID: Int = DEFAULT_VALUE_ZERO
    private var fgViewID: Int = DEFAULT_VALUE_ZERO
    private val fadeViews: ArrayList<Int>
    private var rowClickListener: OnRowClickListener? = null
    private var bgClickListener: OnSwipeOptionsClickListener? = null

    // user choices
    private var clickable = false
    private var swipeable = false

    init {
        val viewConfiguration = ViewConfiguration.get(view.context)
        touchSlop = viewConfiguration.scaledTouchSlop
        minFlingVel = viewConfiguration.scaledMinimumFlingVelocity shl SHIFT_VALUE
        maxFlingVel = viewConfiguration.scaledMaximumFlingVelocity
        bgVisible = false
        bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
        bgVisibleView = null
        fgPartialViewClicked = false
        unSwipeRows = ArrayList()
        unClickableRows = ArrayList()
        ignoredViewTypes = HashSet()
        independentViews = ArrayList()
        optionViews = ArrayList()
        fadeViews = ArrayList()
        isRViewScrolling = false

        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING)
                isRViewScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }
        })
    }

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     *
     * @param enabled Whether or not to watch for gestures.
     */
    fun setEnabled(enabled: Boolean) {
        paused = !enabled
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, motionEvent: MotionEvent): Boolean {
        return handleTouchEvent(motionEvent)
    }


    override fun onTouchEvent(rv: RecyclerView, motionEvent: MotionEvent) {
        handleTouchEvent(motionEvent)
    }

    fun setSwipe(
        foregroundID: Int, backgroundID: Int, listener: OnSwipeOptionsClickListener
    ): RecyclerTouchListener {
        this.swipeable = true
        require(!(fgViewID != DEFAULT_VALUE_ZERO && foregroundID != fgViewID))
        { "foregroundID does not match previously set ID" }
        fgViewID = foregroundID
        bgViewID = backgroundID
        this.bgClickListener = listener

        if (activity is RecyclerTouchListenerHelper)
            (activity as RecyclerTouchListenerHelper).setOnActivityTouchListener(this)

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels

        return this
    }

    fun setSwipeOptionViews(vararg viewIds: Int): RecyclerTouchListener {
        this.optionViews = viewIds.toList()
        return this
    }

    private fun isIndependentViewClicked(motionEvent: MotionEvent): Boolean {
        for (i in independentViews.indices) {
            touchedView?.let {
                val rect = Rect()
                val x = motionEvent.rawX.toInt()
                val y = motionEvent.rawY.toInt()
                it.findViewById<View>(independentViews[i]).getGlobalVisibleRect(rect)
                if (rect.contains(x, y)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getOptionViewID(motionEvent: MotionEvent): Int {
        for (i in optionViews.indices) {
            touchedView?.let {
                val rect = Rect()
                val x = motionEvent.rawX.toInt()
                val y = motionEvent.rawY.toInt()
                it.findViewById<View>(optionViews[i]).getGlobalVisibleRect(rect)
                if (rect.contains(x, y)) {
                    return optionViews[i]
                }
            }
        }
        return DEFAULT_VALUE_MINUS_ONE
    }

    private fun getIndependentViewID(motionEvent: MotionEvent): Int {
        for (i in independentViews.indices) {
            touchedView?.let {
                val rect = Rect()
                val x = motionEvent.rawX.toInt()
                val y = motionEvent.rawY.toInt()
                it.findViewById<View>(independentViews[i]).getGlobalVisibleRect(rect)
                if (rect.contains(x, y)) {
                    return independentViews[i]
                }
            }
        }
        return DEFAULT_VALUE_MINUS_ONE
    }

    private fun closeVisibleBG(swipeCloseListener: OnSwipeListener?) {
        if (bgVisibleView == null) {
            Log.e(TAG, "No rows found for which background options are visible")
            return
        }
        ObjectAnimator.ofFloat(bgVisibleView, View.TRANSLATION_X, DEFAULT_VALUE_ZERO_FLOAT).apply {
            duration = animationClose
            addListener(object : RecyclerSwipeAnimatorListener() {
                override fun onAnimationEnd(animator: Animator?) {
                    swipeCloseListener?.onSwipeOptionsClosed()
                    removeAllListeners()
                }
            })
            start()
        }

        animateFadeViews(bgVisibleView, animationClose)
        bgVisible = false
        bgVisibleView = null
        bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
    }

    private fun animateFadeViews(downView: View?, duration: Long) {
        fadeViews.forEach { item ->
            downView?.let {
                it.findViewById<View>(item).animate()
                    .alpha(DEFAULT_VALUE_ONE_FLOAT).duration = duration
            }
        }
    }

    private fun animateFG(downView: View?, animateType: Animation) {
        when (animateType) {
            Animation.OPEN -> {
                ObjectAnimator.ofFloat(
                    fgView, View.TRANSLATION_X, -bgWidth.toFloat()
                ).apply {
                    duration = animationDuration
                    interpolator = DecelerateInterpolator(DEFAULT_VALUE_ONE_AND_HALF_FLOAT)
                    start()
                }

                animateFadeViews(downView, animationDuration)
            }
            Animation.CLOSE -> {
                ObjectAnimator.ofFloat(
                    fgView, View.TRANSLATION_X, DEFAULT_VALUE_ONE_FLOAT
                ).apply {
                    duration = animationDuration
                    interpolator = DecelerateInterpolator(DEFAULT_VALUE_ONE_AND_HALF_FLOAT)
                    start()
                }

                animateFadeViews(downView, animationDuration)
            }
        }
    }

    private fun animateFG(downView: View?, swipeCloseListener: OnSwipeListener?) {
        var translateAnimator: ObjectAnimator? = null
        if (animateType == Animation.CLOSE) {
            translateAnimator =
                ObjectAnimator.ofFloat(fgView, View.TRANSLATION_X, DEFAULT_VALUE_ZERO_FLOAT)
            translateAnimator.apply {
                duration = animationDuration
                interpolator = DecelerateInterpolator(DEFAULT_VALUE_ONE_AND_HALF_FLOAT)
                start()
            }
            animateFadeViews(downView, animationDuration)
        }

        translateAnimator?.addListener(object : RecyclerSwipeAnimatorListener() {
            override fun onAnimationEnd(animator: Animator?) {
                swipeCloseListener?.let {
                    if (animateType == Animation.OPEN)
                        swipeCloseListener.onSwipeOptionsOpened()
                    else
                        swipeCloseListener.onSwipeOptionsClosed()
                }
                translateAnimator.removeAllListeners()
            }

        })
    }

    private fun handleTouchEvent(motionEvent: MotionEvent): Boolean {
        initTouchedView(motionEvent)

        if (!setSizesForDifferentRows()) {
            return false
        }

        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (paused) {
                    return false
                }
                view.getLocationOnScreen(listViewChords)
                initTouchedView(motionEvent)
                touchedView?.let {
                    touchedX = motionEvent.rawX
                    touchedY = motionEvent.rawY
                    touchedPosition = view.getChildAdapterPosition(it)

                    if (shouldIgnoreAction(touchedPosition)) {
                        touchedPosition = ListView.INVALID_POSITION
                        return false
                    }
                    if (swipeable) {
                        velocityTracker = VelocityTracker.obtain()
                        velocityTracker?.addMovement(motionEvent)
                        fgView = it.findViewById(fgViewID)
                        bgView = it.findViewById(bgViewID)

                        fgView?.let { view ->
                            bgView?.minimumHeight = view.height
                        } ?: return false

                        fgPartialViewClicked = if (bgVisible && fgView != null) {
                            fgView?.getGlobalVisibleRect(rect)
                            rect.contains(
                                motionEvent.rawX.toInt(),
                                motionEvent.rawY.toInt()
                            )
                        } else {
                            false
                        }
                    }
                }

                view.getHitRect(rect)
                if (swipeable && bgVisible && touchedPosition != bgVisiblePosition) {
                    closeVisibleBG(null)
                }
            }

            MotionEvent.ACTION_CANCEL -> {

                if (velocityTracker == null) {
                    return false
                }

                if (swipeable) {
                    if (touchedView != null && isFgSwiping) {
                        animateFG(touchedView, Animation.CLOSE)
                    }
                    velocityTracker?.recycle()
                    velocityTracker = null
                    isFgSwiping = false
                    bgView = null
                }
                touchedX = DEFAULT_VALUE_ZERO_FLOAT
                touchedY = DEFAULT_VALUE_ZERO_FLOAT
                touchedView = null
                touchedPosition = ListView.INVALID_POSITION
            }

            MotionEvent.ACTION_UP -> {
                run {

                    if (bgView == null && fgView == null) {
                        return false
                    }

                    if (velocityTracker == null && swipeable) {
                        return false
                    }
                    if (touchedPosition < DEFAULT_VALUE_ZERO)
                        return false

                    var swipedLeft = false
                    var swipedRight = false

                    var swipedLeftProper = false
                    var swipedRightProper = false

                    if (isFgSwiping) {
                        swipedLeft = motionEvent.rawX - touchedX < DEFAULT_VALUE_ZERO
                        swipedRight = motionEvent.rawX - touchedX > DEFAULT_VALUE_ZERO
                    }

                    if (abs(motionEvent.rawX - touchedX) > bgWidth / 2 && isFgSwiping) {
                        swipedLeftProper = motionEvent.rawX - touchedX < DEFAULT_VALUE_ZERO
                        swipedRightProper = motionEvent.rawX - touchedX > DEFAULT_VALUE_ZERO
                    } else if (swipeable) {
                        velocityTracker?.let {
                            it.apply {
                                addMovement(motionEvent)
                                computeCurrentVelocity(1000)
                            }
                            val absVelocityX = abs(it.xVelocity)
                            val absVelocityY = abs(it.yVelocity)
                            if (minFlingVel <= absVelocityX && absVelocityX <= maxFlingVel
                                && absVelocityY < absVelocityX && isFgSwiping
                            ) {
                                swipedLeftProper =
                                    it.xVelocity < DEFAULT_VALUE_ZERO ==
                                            motionEvent.rawX - touchedX < DEFAULT_VALUE_ZERO
                                swipedRightProper =
                                    it.xVelocity > DEFAULT_VALUE_ZERO ==
                                            motionEvent.rawX - touchedX > DEFAULT_VALUE_ZERO
                            }
                        }

                    }

                    if (swipeable && !swipedRight && swipedLeftProper && touchedPosition
                        != RecyclerView.NO_POSITION
                        && !unSwipeRows.contains(touchedPosition) && !bgVisible
                    ) {
                        touchedPosition
                        ++mDismissAnimationRefCount
                        animateFG(
                            touchedView,
                            Animation.OPEN
                        )
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = touchedPosition
                    } else if (swipeable && !swipedLeft && swipedRightProper && touchedPosition
                        != RecyclerView.NO_POSITION
                        && !unSwipeRows.contains(touchedPosition) && bgVisible
                    ) {
                        ++mDismissAnimationRefCount
                        animateFG(touchedView, Animation.CLOSE)
                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
                    } else if (swipeable && swipedLeft && !bgVisible) {
                        animateFG(touchedView,
                            object :
                                OnSwipeListener {
                                override fun onSwipeOptionsClosed() {
                                    bgView?.visibility = View.VISIBLE
                                }

                                override fun onSwipeOptionsOpened() {}
                            })

                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
                    } else if (swipeable && swipedRight && bgVisible) {
                        animateFG(touchedView, Animation.OPEN)
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = touchedPosition
                    } else if (swipeable && swipedRight && !bgVisible) {
                        animateFG(touchedView, Animation.CLOSE)
                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
                    } else if (swipeable && swipedLeft && bgVisible) {
                        animateFG(touchedView, Animation.OPEN)
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = touchedPosition
                    } else if (!swipedRight && !swipedLeft) {

                        if (swipeable && fgPartialViewClicked) {
                            animateFG(touchedView, Animation.CLOSE)
                            bgVisible = false
                            bgVisibleView = null
                            bgVisiblePosition = DEFAULT_VALUE_MINUS_ONE
                        } else if (clickable && !bgVisible && touchedPosition >= DEFAULT_VALUE_ZERO
                            && !unClickableRows.contains(touchedPosition)
                            && isIndependentViewClicked(motionEvent) && !isRViewScrolling
                        ) {
                            rowClickListener!!.onRowClicked(touchedPosition)
                        } else if (clickable && !bgVisible && touchedPosition >= DEFAULT_VALUE_ZERO
                            && !unClickableRows.contains(touchedPosition)
                            && !isIndependentViewClicked(motionEvent) && !isRViewScrolling
                        ) {
                            val independentViewID = getIndependentViewID(motionEvent)
                            if (independentViewID >= DEFAULT_VALUE_ZERO)
                                rowClickListener?.onIndependentViewClicked(
                                    independentViewID,
                                    touchedPosition
                                )
                        } else if (swipeable && bgVisible && !fgPartialViewClicked) {
                            val optionID = getOptionViewID(motionEvent)
                            if (optionID >= DEFAULT_VALUE_ZERO && touchedPosition >= DEFAULT_VALUE_ZERO) {
                                val downPosition = touchedPosition
                                closeVisibleBG(object :
                                    OnSwipeListener {
                                    override fun onSwipeOptionsClosed() {
                                        bgClickListener?.onSwipeOptionClicked(
                                            optionID,
                                            downPosition
                                        )
                                    }

                                    override fun onSwipeOptionsOpened() {}
                                })
                            }
                        }
                    }
                }

                if (swipeable) {
                    velocityTracker?.recycle()
                    velocityTracker = null
                }
                touchedX = DEFAULT_VALUE_ZERO_FLOAT
                touchedY = DEFAULT_VALUE_ZERO_FLOAT
                touchedView = null
                touchedPosition = ListView.INVALID_POSITION
                isFgSwiping = false
                bgView = null
            }

            MotionEvent.ACTION_MOVE -> {

                if (velocityTracker == null || paused || !swipeable) {
                    return false
                }

                velocityTracker?.addMovement(motionEvent)
                motionEvent.rawY - touchedY

                if (!isFgSwiping && abs(motionEvent.rawX - touchedX) > touchSlop
                    && abs(motionEvent.rawY - touchedY) < abs(
                        motionEvent.rawX - touchedX
                    ) / 2
                ) {
                    isFgSwiping = true
                    swipingSlop =
                        if (motionEvent.rawX - touchedX > DEFAULT_VALUE_ZERO) touchSlop else -touchSlop
                }

                if (swipeable && isFgSwiping && !unSwipeRows.contains(touchedPosition)) {
                    if (bgView == null) {
                        bgView = touchedView?.findViewById(bgViewID)

                        bgView?.let {
                            it.visibility = View.VISIBLE
                        } ?: return false

                    }

                    if (motionEvent.rawX - touchedX < touchSlop && !bgVisible) {
                        val translateAmount = motionEvent.rawX - touchedX - swipingSlop

                        fgView?.let {
                            it.translationX =
                                if (abs(translateAmount) > bgWidth) (-bgWidth).toFloat() else translateAmount
                            if (it.translationX > DEFAULT_VALUE_ZERO) it.translationX =
                                DEFAULT_VALUE_ZERO_FLOAT

                        }

                        for (viewID in fadeViews) {
                            touchedView?.findViewById<View>(viewID)?.alpha =
                                DEFAULT_VALUE_ONE - abs(translateAmount) / bgWidth
                        }
                    } else if (motionEvent.rawX - touchedX > DEFAULT_VALUE_ZERO && bgVisible) {

                        if (bgVisible) {

                            fgView?.translationX =
                                if (motionEvent.rawX - touchedX - swipingSlop - bgWidth > DEFAULT_VALUE_ZERO)
                                    DEFAULT_VALUE_ZERO_FLOAT
                                else motionEvent.rawX - touchedX - swipingSlop - bgWidth

                            for (viewID in fadeViews) {
                                touchedView?.findViewById<View>(viewID)?.alpha =
                                    DEFAULT_VALUE_ONE -
                                            abs(motionEvent.rawX - touchedX - swipingSlop - bgWidth) / bgWidth
                            }
                        } else {

                            fgView?.translationX =
                                if (motionEvent.rawX - touchedX - swipingSlop - bgWidth > DEFAULT_VALUE_ZERO)
                                    DEFAULT_VALUE_ZERO_FLOAT
                                else
                                    motionEvent.rawX - touchedX - swipingSlop - bgWidth

                            for (viewID in fadeViews) {
                                touchedView?.findViewById<View>(viewID)?.alpha =
                                    DEFAULT_VALUE_ONE - abs(motionEvent.rawX - touchedX - swipingSlop - bgWidth) / bgWidth
                            }
                        }// for opening leftOptions
                    }// if fg is being swiped right
                    return true
                } else if (swipeable && isFgSwiping && unSwipeRows.contains(touchedPosition)) {
                    if (motionEvent.rawX - touchedX < touchSlop && !bgVisible) {
                        if (bgView == null)
                            bgView = touchedView?.findViewById(bgViewID)

                        bgView?.let {
                            it.visibility = View.GONE
                        }

                        fgView?.let {
                            it.translationX = motionEvent.rawX - touchedX - swipingSlop / 5
                            if (it.translationX > DEFAULT_VALUE_ZERO) {
                                it.translationX = DEFAULT_VALUE_ZERO_FLOAT
                            }
                        }

                    }
                    return true
                }
            }
        }
        return false
    }

    override fun getTouchCoordinates(ev: MotionEvent) {
        val y = ev.rawY.toInt()
        if (swipeable && bgVisible && ev.actionMasked == MotionEvent.ACTION_DOWN
            && y < heightOutsideRView
        )
            closeVisibleBG(null)
    }

    private fun shouldIgnoreAction(touchedPosition: Int): Boolean {
        return ignoredViewTypes.contains(
            (view.adapter)?.getItemViewType(touchedPosition)
        )
    }

    private fun initTouchedView(motionEvent: MotionEvent) {
        for (i in 0 until view.childCount) {
            view.getChildAt(i).getHitRect(rect)
            if (rect.contains(
                    motionEvent.rawX.toInt() - listViewChords[0],
                    motionEvent.rawY.toInt() - listViewChords[1]
                )
            ) {
                touchedView = view.getChildAt(i)
                break
            }
        }
    }

    private fun setSizesForDifferentRows(): Boolean {
        if (swipeable && bgWidth < 2) {
            heightOutsideRView = screenHeight - view.height
        }
/*
        // for specific layout, where can be different count of actions for each list item
        if (bgViewID == R.id.gift_swipe_row) {
            val actionView = touchedView?.findViewById<LinearLayout>(R.id.action)
            val additionalActionView =
                touchedView?.findViewById<LinearLayout>(R.id.additional_action)
            bgWidth =
                if (actionView?.visibility == View.VISIBLE && additionalActionView?.visibility == View.VISIBLE) {
                    actionView.width + additionalActionView.width
                } else if (actionView?.visibility == View.VISIBLE || additionalActionView?.visibility == View.VISIBLE) {
                    actionView?.width ?: 0
                } else {
                    return false
                }
        } else {
            if (swipeable && bgWidth < 2) {
                activity.findViewById<View>(bgViewID)?.let {
                    bgWidth = it.width
                }
            }
        }*/
        if (swipeable && bgWidth < 2) {
            activity.findViewById<View>(bgViewID)?.let {
                bgWidth = it.width
            }
        }
        return true
    }

    private enum class Animation {
        OPEN, CLOSE
    }

    interface OnRowClickListener {
        fun onRowClicked(position: Int)

        fun onIndependentViewClicked(independentViewID: Int, position: Int)
    }

    interface OnSwipeOptionsClickListener {
        fun onSwipeOptionClicked(viewID: Int, position: Int)
    }

    interface RecyclerTouchListenerHelper {
        fun setOnActivityTouchListener(listener: OnActivityTouchListener)
    }

    interface OnSwipeListener {
        fun onSwipeOptionsClosed()

        fun onSwipeOptionsOpened()
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    abstract class RecyclerSwipeAnimatorListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator?) {}

        override fun onAnimationCancel(animator: Animator?) {}

        override fun onAnimationStart(animator: Animator?) {}
    }
}
