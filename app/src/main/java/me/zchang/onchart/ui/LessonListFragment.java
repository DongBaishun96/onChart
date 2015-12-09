package me.zchang.onchart.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.zchang.onchart.R;
import me.zchang.onchart.exception.LessonStartTimeException;
import me.zchang.onchart.student.Lesson;
import me.zchang.onchart.ui.adapter.LessonListAdapter;

import java.util.ArrayList;
import java.util.List;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonListFragment extends Fragment {
    private final static String TAG = "LessonListFragment";
    RecyclerView lessonList;
    LessonListAdapter adapter;

    /**
     * This lesson list is handled by Fragment and shared with adapter
     * for lifecycle consideration.As the data is generated before Fragment attached,
     * the moment the adapter is not instantiated, the data should be stored in
     * Fragment to be passed to adapter later.What's more, the list's pointer
     * points to one area of memory so that you need to maintain this list only.
     */
    List<Lesson> lessons;

    public LessonListFragment() {
        lessons = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            adapter = new LessonListAdapter(context, lessons);
        } catch (LessonStartTimeException e) {
            Toast.makeText(getActivity(), "Unknown lesson time", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lesson_list, container, false);
        lessonList = (RecyclerView) rootView.findViewById(R.id.rv_lessons);
        lessonList.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            adapter.setLessons(lessons);
        } catch (LessonStartTimeException e) {
            Toast.makeText(getActivity(), "Unknown lesson time", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        lessonList.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        return rootView;
    }

    public void clearLesson() {
        if(lessons != null)
            lessons.clear();
    }

    public void addLesson(Lesson lesson) {
        if(lesson != null) {
            lessons.add(lesson);
        }

    }

    public void updateList() {
        //adapter.setList();
        if(adapter != null) {
            try {
                // TODO　bad logic
                adapter.processLessons();
            } catch (LessonStartTimeException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }



}