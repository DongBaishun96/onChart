package me.zchang.onchart.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.zchang.onchart.R;
import me.zchang.onchart.student.Course;
import me.zchang.onchart.student.LabelCourse;

/**
 * Created by Administrator on 2016/1/22.
 */
public class CourseSQLiteHelper extends SQLiteOpenHelper {
    private final static String TAG = "CourseSQLiteHelper";
    Context context;
    public final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public CourseSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private enum COURSE_TABLE_INDICES {
        COURSE_ID,
        COURSE_NAME,
        COURSE_DEPARTMENT,
        COURSE_CREDIT,
        COURSE_TEACHER,
        COURSE_CLASSROOM,
        COURSE_WEEKDAY,
        COURSE_START_TIME,
        COURSE_END_TIME,
        COURSE_START_WEEK,
        COURSE_END_WEEK,
        COURSE_WEEK_PARITY,
        COURSE_LABEL_IMG_INDEX
    }

    private final static String [] FIELD_NAMES = {
            "id",
            "name",
            "department",
            "credit",
            "teacher",
            "classroom",
            "weekDay",
            "startTime",
            "endTime",
            "startWeek",
            "endWeek",
            "weekParity",
            "labelImgIndex"
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createStatement =
                "CREATE TABLE "+ context.getString(R.string.course_table_name) +" (" +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_ID.ordinal()] + " INT PRIMARY KEY," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_NAME.ordinal()] + " VARCHAR(100) NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_DEPARTMENT.ordinal()] + " VARCHAR(100)," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_CREDIT.ordinal()] + " FLOAT NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_TEACHER.ordinal()] + " VARCHAR(30)," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_CLASSROOM.ordinal()] + " VARCHAR(30) NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_WEEKDAY.ordinal()] + " INT NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_START_TIME.ordinal()] + " TIME NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_END_TIME.ordinal()] + " TIME NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_START_WEEK.ordinal()] + " INT NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_END_WEEK.ordinal()] + " INT NOT NULL," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_WEEK_PARITY.ordinal()] + " INT," +
                FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_LABEL_IMG_INDEX.ordinal()] + " INT" +
                ")";
        db.execSQL(createStatement);
    }

    public void addCourse(Course course) {
        if (course != null) {
            SQLiteDatabase courseDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_ID.ordinal()], course.getId());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_NAME.ordinal()], course.getName());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_DEPARTMENT.ordinal()], course.getDepartment());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_CREDIT.ordinal()], course.getCredit());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_TEACHER.ordinal()], course.getTeacher());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_CLASSROOM.ordinal()], course.getClassroom());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_WEEKDAY.ordinal()], course.getWeekDay());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_START_WEEK.ordinal()], course.getStartWeek());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_END_WEEK.ordinal()], course.getEndWeek());

            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_START_TIME.ordinal()], timeFormat.format(course.getStartTime()));
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_END_TIME.ordinal()], timeFormat.format(course.getEndTime()));
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_WEEK_PARITY.ordinal()], course.getWeekParity());
            values.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_LABEL_IMG_INDEX.ordinal()], course.getLabelImgIndex());

            courseDatabase.insert(
                    context.getString(R.string.course_table_name),
                    null, values);
        }
    }

    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase courseDatabase = getReadableDatabase();
        Cursor cursor = courseDatabase.rawQuery("SELECT * from " + context.getString(R.string.course_table_name), null);

        while (cursor.moveToNext()) {
            Course newCourse = new LabelCourse();
            String sStartTime = cursor.getString(COURSE_TABLE_INDICES.COURSE_START_TIME.ordinal());
            String sEndTime = cursor.getString(COURSE_TABLE_INDICES.COURSE_END_TIME.ordinal());
            try {
                newCourse.setStartTime(new Time(timeFormat.parse(sStartTime).getTime()));
                newCourse.setEndTime(new Time(timeFormat.parse(sEndTime).getTime()));
                newCourse.setId(cursor.getInt(COURSE_TABLE_INDICES.COURSE_ID.ordinal()));
                newCourse.setName(cursor.getString(COURSE_TABLE_INDICES.COURSE_NAME.ordinal()));
                newCourse.setDepartment(cursor.getString(COURSE_TABLE_INDICES.COURSE_DEPARTMENT.ordinal()));
                newCourse.setCredit(cursor.getFloat(COURSE_TABLE_INDICES.COURSE_CREDIT.ordinal()));
                newCourse.setTeacher(cursor.getString(COURSE_TABLE_INDICES.COURSE_TEACHER.ordinal()));
                newCourse.setClassroom(cursor.getString(COURSE_TABLE_INDICES.COURSE_CLASSROOM.ordinal()));
                newCourse.setWeekDay(cursor.getInt(COURSE_TABLE_INDICES.COURSE_WEEKDAY.ordinal()));
                newCourse.setStartWeek(cursor.getInt(COURSE_TABLE_INDICES.COURSE_START_WEEK.ordinal()));
                newCourse.setEndWeek(cursor.getInt(COURSE_TABLE_INDICES.COURSE_END_WEEK.ordinal()));
                newCourse.setLabelImgIndex(cursor.getInt(COURSE_TABLE_INDICES.COURSE_LABEL_IMG_INDEX.ordinal()));
                newCourse.setWeekParity((byte)cursor.getInt(COURSE_TABLE_INDICES.COURSE_WEEK_PARITY.ordinal()));

                courses.add(newCourse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return courses;
    }

    public void clearCourses() {
        SQLiteDatabase courseDatabase = getWritableDatabase();
        courseDatabase.execSQL("DELETE FROM " + context.getString(R.string.course_table_name));
    }

    public void setImgPathIndex(int id, int resIndex) {
        SQLiteDatabase courseDatabase = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_LABEL_IMG_INDEX.ordinal()], resIndex);
        courseDatabase.update(context.getString(R.string.course_table_name), value, "id = " + id, null);
    }

    public int getImgPathIndex(int id, int defaultResIndex) {

        SQLiteDatabase courseDatabase = getReadableDatabase();

        Cursor cursor =
                courseDatabase.rawQuery(
                        "SELECT "
                                + FIELD_NAMES[COURSE_TABLE_INDICES.COURSE_LABEL_IMG_INDEX.ordinal()]
                                + " FROM "
                                + context.getString(R.string.course_table_name)
                                + " WHERE id = "
                                + id, null);
        int index = defaultResIndex;
        if (cursor.moveToNext()) {
            index = cursor.getInt(0);
        }
        cursor.close();
        return index;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}