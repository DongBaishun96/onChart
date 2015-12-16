package me.zchang.onchart.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import me.zchang.onchart.R;
import me.zchang.onchart.config.MainApp;
import me.zchang.onchart.config.PreferenceManager;
import me.zchang.onchart.student.Lesson;
import me.zchang.onchart.ui.utils.CardToDialog;
import me.zchang.onchart.ui.utils.DialogToCard;

public class DetailActivity extends AppCompatActivity {

    Intent retIntent;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_detail);
        intent = getIntent();
        int startColor = intent.getIntExtra("color", -1);
        final Lesson lesson = intent.getParcelableExtra(getString(R.string.intent_lesson));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            Interpolator easeInOut = new AccelerateDecelerateInterpolator();
            TransitionSet sharedEnterSet = new TransitionSet();
            //CardToDialog sharedEnter = new CardToDialog(startColor);
            ChangeBounds sharedEnter = new ChangeBounds();
            sharedEnter.setPathMotion(arcMotion);
            sharedEnter.setInterpolator(easeInOut);
            //sharedEnter.addTarget(R.id.ll_container);
            sharedEnterSet.addTransition(sharedEnter);
            ChangeImageTransform imgTrans = new ChangeImageTransform();
            imgTrans.addTarget(R.id.iv_label);

            TransitionSet sharedExitSet = new TransitionSet();
            //ChangeBounds sharedExit = new ChangeBounds();
            DialogToCard sharedExit = new DialogToCard(startColor);
            sharedExit.setPathMotion(arcMotion);
            sharedExitSet.addTransition(sharedExit);
            sharedExitSet.addTransition(imgTrans);
            //sharedExit.setInterpolator(easeInOut);
            //sharedExit.addTarget(R.id.ll_container);
            getWindow().setSharedElementEnterTransition(sharedEnterSet);
            getWindow().setSharedElementReturnTransition(sharedExit);
            //getWindow().setSharedElementExitTransition(sharedExit);
        }

        retIntent = new Intent();
        //final Intent intent = getIntent();
        //final Lesson lesson = intent.getParcelableExtra(getString(R.string.intent_lesson));

        setResult(RESULT_CANCELED, retIntent);
        //retIntent.putExtra(getString(R.string.intent_frag_index), intent.getIntExtra(getString(R.string.intent_frag_index), 0));
        retIntent.putExtra(getString(R.string.intent_position), intent.getIntExtra(getString(R.string.intent_position), 0));
        if(lesson != null) {
            retIntent.putExtra(getString(R.string.intent_lesson_id), lesson.getId());
            final TextView lessonNameText = (TextView) findViewById(R.id.tv_lesson_name);
            TextView teacherText = (TextView) findViewById(R.id.tv_teacher);
            TextView classroomText = (TextView) findViewById(R.id.tv_classroom);
            TextView weekText = (TextView) findViewById(R.id.tv_week_cycle);
            TextView creditText = (TextView) findViewById(R.id.tv_credit);
            final ImageView labelImage = (ImageView) findViewById(R.id.iv_label);

            lessonNameText.setText(lesson.getName());
            teacherText.setText(lesson.getTeacher());
            classroomText.setText(lesson.getClassroom());
            weekText.setText(lesson.getStartWeek() + " - " + lesson.getEndWeek() + getString(R.string.weekday_week));
            creditText.setText(lesson.getCredit() + "");
            labelImage.setImageResource(PreferenceManager.labelImgs[lesson.getLabelImgIndex()]);

            labelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lesson.setToNextLabelImg();
                    // update local storage only.
                    ((MainApp) getApplication()).getPreferenceManager().savePicPathIndex(lesson.getId(), lesson.getLabelImgIndex());
                    labelImage.setImageResource(PreferenceManager.labelImgs[lesson.getLabelImgIndex()]);
                    //((MainActivity)getActivity()).getListFragment().adapter.notifyItemChanged(position);
                    //retIntent.putExtra(getString(R.string.intent_frag_index), fragIndex);
                    setResult(RESULT_OK, retIntent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
            dismiss(null);
    }


    @TargetApi(21)
    public void dismiss(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //setResult(RESULT_CANCELED);
            finishAfterTransition();
        }
    }
}
