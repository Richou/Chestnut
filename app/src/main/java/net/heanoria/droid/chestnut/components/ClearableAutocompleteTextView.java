package net.heanoria.droid.chestnut.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import net.heanoria.droid.chestnut.R;

public class ClearableAutocompleteTextView extends AutoCompleteTextView implements View.OnTouchListener{

    private boolean justCleared = false;
    private Drawable imgCloseButton = getResources().getDrawable(R.drawable.ximage);

    public ClearableAutocompleteTextView(Context context) {
        super(context);
        init();
    }

    public ClearableAutocompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ClearableAutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, imgCloseButton, null);
        clearButtonHandler();
        this.setOnTouchListener(this);
    }

    public void clearButtonHandler(){
        if (this.getText() != null && !this.getText().toString().isEmpty()){
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, imgCloseButton, null);
        } else {
            this.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ClearableAutocompleteTextView instance = ClearableAutocompleteTextView.this;

        if(instance.getCompoundDrawables()[2] == null)
            return false;

        if (event.getAction() != MotionEvent.ACTION_UP)
            return false;

        if (event.getX() > instance.getWidth() - instance.getPaddingRight() - imgCloseButton.getIntrinsicWidth()){
            instance.setText("");
            instance.clearButtonHandler();
            justCleared = true;
        }
        return false;
    }

    public boolean isJustCleared() {
        return justCleared;
    }

    public void setJustCleared(boolean justCleared) {
        this.justCleared = justCleared;
    }
}
