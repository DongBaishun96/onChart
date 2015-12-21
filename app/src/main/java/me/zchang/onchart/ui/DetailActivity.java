package me.zchang.onchart.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import me.zchang.onchart.R;
import me.zchang.onchart.config.MainApp;
import me.zchang.onchart.config.PreferenceManager;
import me.zchang.onchart.student.Course;
import me.zchang.onchart.ui.utils.DialogToCard;

/*
 *    Copyright 2015 Zhehua Chang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

public class DetailActivity extends AppCompatActivity {

    Intent retIntent;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intent = getIntent();
        int startColor = intent.getIntExtra("color", -1);
        final Course course = intent.getParcelableExtra(getString(R.string.intent_lesson));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            Interpolator easeInOut = new AccelerateDecelerateInterpolator();
            TransitionSet sharedEnterSet = new TransitionSet();
            ChangeBounds sharedEnter = new ChangeBounds();
            sharedEnter.setPathMotion(arcMotion);
            sharedEnter.setInterpolator(easeInOut);
            sharedEnterSet.addTransition(sharedEnter);
            ChangeImageTransform imgTrans = new ChangeImageTransform();
            imgTrans.addTarget(R.id.iv_label);

            TransitionSet sharedExitSet = new TransitionSet();
            DialogToCard sharedExit = new DialogToCard(startColor);
            sharedExit.setPathMotion(arcMotion);
            sharedExitSet.addTransition(sharedExit);
            sharedExitSet.addTransition(imgTrans);
            getWindow().setSharedElementEnterTransition(sharedEnterSet);
            getWindow().setSharedElementReturnTransition(sharedExit);
        }

        retIntent = new Intent();

        setResult(RESULT_CANCELED, retIntent);
        retIntent.putExtra(getString(R.string.intent_position), intent.getIntExtra(getString(R.string.intent_position), 0));
        if(course != null) {
            retIntent.putExtra(getString(R.string.intent_course_id), course.getId());
            final TextView lessonNameText = (TextView) findViewById(R.id.tv_lesson_name);
            TextView teacherText = (TextView) findViewById(R.id.tv_teacher);
            TextView classroomText = (TextView) findViewById(R.id.tv_classroom);
            TextView weekText = (TextView) findViewById(R.id.tv_week_cycle);
            TextView creditText = (TextView) findViewById(R.id.tv_credit);
            final ImageView labelImage = (ImageView) findViewById(R.id.iv_label);

            lessonNameText.setText(course.getName());
            teacherText.setText(course.getTeacher());
            classroomText.setText(course.getClassroom());
            weekText.setText(course.getStartWeek() + " - " + course.getEndWeek() + getString(R.string.weekday_week));
            creditText.setText(course.getCredit() + "");
            labelImage.setImageResource(PreferenceManager.labelImgs[course.getLabelImgIndex()]);

            labelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    course.setToNextLabelImg();
                    // update local storage only.
                    ((MainApp) getApplication()).getPreferenceManager().savePicPathIndex(course.getId(), course.getLabelImgIndex());
                    labelImage.setImageResource(PreferenceManager.labelImgs[course.getLabelImgIndex()]);
                    retIntent.putExtra(getString(R.string.intent_label_image_index), course.getLabelImgIndex());
                    setResult(RESULT_OK, retIntent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
            dismissCompat(null);
    }

    public void dismissCompat(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}