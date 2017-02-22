package net.sramanovich.fitnessday.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import net.sramanovich.fitnessday.adapters.TrainingProgramListAdapter;
import net.sramanovich.fitnessday.db.TrainingSet;

public class DragListView extends ListView {

    private ImageView dragImageView;
    private int dragSrcPosition;
    private int dragPosition;
    private int dragEmptyPosition=INVALID_POSITION;
    private TrainingSet dragSrcItem;

    private int mDownX=-1;
    private int mDownY=-1;

    private int dragPoint;
    private int dragOffset;

    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;

    private int scaledTouchSlop;
    private int upScrollBounce;
    private int downScrollBounce;

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnItemLongClickListener(itemLongClickListener);
    }

    private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            dragSrcPosition = dragPosition = position;//pointToPosition(mDownX, mDownY);
            Log.v("onItemLongClick: ", "dragSrcPosition="+position);
            if(dragPosition!=AdapterView.INVALID_POSITION){
                if(dragSrcPosition>=0&&dragSrcPosition<getAdapter().getCount()){
                    TrainingProgramListAdapter adapter = (TrainingProgramListAdapter) getAdapter();
                    dragSrcItem = adapter.getItem(dragSrcPosition);
                    Log.v("onItemLongClick: ", "dragSrcItem="+dragSrcItem);
                    //adapter.remove(dragSrcItem);
                }
            }

            //берем ту позицию на которую мы надавили и берем этот айтем и отслеживаем перемещения пальца или чем вы там тыкаете в экран
            ViewGroup itemView = (ViewGroup) getChildAt(dragPosition-getFirstVisiblePosition());
            dragPoint = mDownY - itemView.getTop();
            //dragOffset = (int) (ev.getRawY() - mDownY);
            //dragOffset = 0;//itemView.getBottom()-mDownY;

            //магия
            //View dragger = itemView.findViewById(R.id.radioButton);
            //if(dragger!=null&&x>dragger.getLeft()-20){
            upScrollBounce = Math.min(mDownY-scaledTouchSlop, getHeight()/3);
            downScrollBounce = Math.max(mDownY+scaledTouchSlop, getHeight()*2/3);

            itemView.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
            startDrag(bm, mDownY);
            return false;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch(action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                Log.v("onToucheEvent:", "UP");
                mDownX=-1;
                mDownY=-1;
                if(dragImageView!=null&&dragPosition!=INVALID_POSITION) {
                    int upY = (int) ev.getY();
                    stopDrag();
                    onDrop(upY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(dragImageView!=null&&dragPosition!=INVALID_POSITION) {
                    int moveY = (int) ev.getY();
                    onDrag(moveY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if(mDownX<0&&mDownY<0) {
                    mDownX = (int) ev.getX();
                    mDownY = (int) ev.getY();
                    dragOffset = (int)(ev.getRawY()-mDownY);
                    Log.v("onToucheEvent:", "down X=" + mDownX + ", down Y=" + mDownY + "offset="+dragOffset);
                    break;
                }
            //default:
            //    return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * Метод обрабатывающий перетаскивание айтемов
     * @param bm
     * @param y
     */
    public void startDrag(Bitmap bm ,int y){
        stopDrag();

        //из кода меняем позици елемента который мы схватили на координаты которые мы получили в onInterceptTouchEvent()
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragOffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    /**
     * удаляем лишний вью и вставляем его ниже
     */
    public void stopDrag(){
        if(dragImageView!=null){
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * перемещаем по экрану айтемы
     * снова куча магии
     * @param y
     */
    public void onDrag(int y){
        if(dragImageView!=null){
            windowParams.alpha = 0.8f;
            windowParams.y = y - dragPoint + dragOffset;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
        int tempPosition = pointToPosition(50, y);
        Log.v("onDrag: ", "tempPosition="+tempPosition);
        if(tempPosition!=INVALID_POSITION){
            dragPosition = tempPosition;
            if(dragEmptyPosition>= 0 && dragPosition!=dragEmptyPosition) {
                TrainingProgramListAdapter adapter = (TrainingProgramListAdapter) getAdapter();
                TrainingSet itemToDelete = adapter.getItem(dragEmptyPosition);
                adapter.remove(itemToDelete);
                Log.v("onDrag: ", "Remove item itemToDelete=="+itemToDelete);
                dragEmptyPosition = INVALID_POSITION;
            }

            if( dragEmptyPosition==INVALID_POSITION &&
                    //dragPosition!=dragSrcPosition &&
                    dragSrcPosition>=0 &&
                    dragSrcPosition<getAdapter().getCount()) {
                TrainingProgramListAdapter adapter = (TrainingProgramListAdapter) getAdapter();
                adapter.remove(dragSrcItem);
                adapter.insert(new TrainingSet(0, ""), dragPosition);
                Log.v("onDrag: ", "emptyPosition="+dragPosition);
                dragEmptyPosition = dragPosition;
            }
            //Log.v("onDrag: ", "dragPosition="+dragPosition);
        }

        int scrollHeight = 0;
        if(y<upScrollBounce){
            scrollHeight = 8;
        }else if(y>downScrollBounce){
            scrollHeight = -8;
        }

        if(scrollHeight!=0){
            setSelectionFromTop(dragPosition, getChildAt(dragPosition-getFirstVisiblePosition()).getTop()+scrollHeight);
        }
    }

    /**
     * что же делать чкогда бросили? верно сохраняем позицию если она валидна
     * куча магии
     * @param y
     */
    public void onDrop(int y){
        //здесь мы замещаем айтем который на данный момент находится
        // на своей позиции вниз что бы вставить тот что тащили и удаляем тот что был вместо него
        // и ставим его ниже
        int tempPosition = pointToPosition(0, y);
        if(tempPosition!=INVALID_POSITION){
            dragPosition = tempPosition;
        }

        Log.v("onDrop: ", "position="+dragPosition);

        /*if(y<getChildAt(1).getTop()){
            dragPosition = 1;
        }else if(y>getChildAt(getChildCount()-1).getBottom()){
            dragPosition = getAdapter().getCount()-1;
        }*/

        //
        if(dragPosition>=0&&dragPosition<getAdapter().getCount()){
            TrainingProgramListAdapter adapter = (TrainingProgramListAdapter) getAdapter();
            //TrainingSet dragItem = adapter.getItem(dragSrcPosition);
            //adapter.remove(dragItem);
            if(dragEmptyPosition>=0) {
                TrainingSet itemToDelete = adapter.getItem(dragEmptyPosition);
                adapter.remove(itemToDelete);
                dragEmptyPosition = -1;
            }

            if(dragSrcItem != null) {
                adapter.insert(dragSrcItem, dragPosition);
            }

            mDownX=-1;
            mDownY=-1;
        }

    }
}
