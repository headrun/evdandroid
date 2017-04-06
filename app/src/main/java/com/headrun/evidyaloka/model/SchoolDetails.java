package com.headrun.evidyaloka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sujith on 21/1/17.
 */

public class SchoolDetails implements Parcelable {

    public static final String TAG_SCHOOLDEATILS = SchoolDetails.class.getSimpleName();

    @SerializedName("current_teachers")
    public Profile[] current_teachers;
    @SerializedName("pcourses_data")
    public CourseData[] demands;
    @SerializedName("sponsor")
    public String sponsor;
    @SerializedName("photo")
    public String school_image;
    @SerializedName("id")
    public String school_id;
    @SerializedName("desc")
    public String school_desc;
    @SerializedName("name")
    public String school_name;
    @SerializedName("latest_classesdetails")
    public ClassDetails[] class_detals;
    @SerializedName("admin")
    public Profile[] school_admin;
    @SerializedName("class_assistant")
    public Profile[] class_assistant;
    @SerializedName("grouped_slots")
    public Map<String, ClassTimings[]> time_sllots;
    @SerializedName("slot_details")
    public SlotDeatils slot_details;

    public static class Profile implements Parcelable {

        @SerializedName("image")
        public String image;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.image);
            dest.writeString(this.name);
            dest.writeString(this.id);
        }

        public Profile() {
        }

        protected Profile(Parcel in) {
            this.image = in.readString();
            this.name = in.readString();
            this.id = in.readString();
        }

        public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
            @Override
            public Profile createFromParcel(Parcel source) {
                return new Profile(source);
            }

            @Override
            public Profile[] newArray(int size) {
                return new Profile[size];
            }
        };
    }

    public static class CourseData implements Parcelable {

        @SerializedName("grade")
        public String grade;
        @SerializedName("id")
        public String id;
        @SerializedName("subject")
        public String subject;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.grade);
            dest.writeString(this.id);
            dest.writeString(this.subject);
        }

        public CourseData() {
        }

        protected CourseData(Parcel in) {
            this.grade = in.readString();
            this.id = in.readString();
            this.subject = in.readString();
        }

        public static final Parcelable.Creator<CourseData> CREATOR = new Parcelable.Creator<CourseData>() {
            @Override
            public CourseData createFromParcel(Parcel source) {
                return new CourseData(source);
            }

            @Override
            public CourseData[] newArray(int size) {
                return new CourseData[size];
            }
        };
    }

    public static class ClassDetails implements Parcelable {

        @SerializedName("class_date")
        public String class_date;
        @SerializedName("image")
        public String image;
        @SerializedName("name")
        public String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.class_date);
            dest.writeString(this.image);
            dest.writeString(this.name);
        }

        public ClassDetails() {
        }

        protected ClassDetails(Parcel in) {
            this.class_date = in.readString();
            this.image = in.readString();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<ClassDetails> CREATOR = new Parcelable.Creator<ClassDetails>() {
            @Override
            public ClassDetails createFromParcel(Parcel source) {
                return new ClassDetails(source);
            }

            @Override
            public ClassDetails[] newArray(int size) {
                return new ClassDetails[size];
            }
        };
    }

    public static class ClassTimings implements Parcelable {
        @SerializedName("id")
        public int class_id;
        @SerializedName("end_date")
        public String end_date;
        @SerializedName("start_date")
        public String start_date;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.class_id);
            dest.writeString(this.end_date);
            dest.writeString(this.start_date);
        }

        public ClassTimings() {
        }

        protected ClassTimings(Parcel in) {
            this.class_id = in.readInt();
            this.end_date = in.readString();
            this.start_date = in.readString();
        }

        public static final Creator<ClassTimings> CREATOR = new Creator<ClassTimings>() {
            @Override
            public ClassTimings createFromParcel(Parcel source) {
                return new ClassTimings(source);
            }

            @Override
            public ClassTimings[] newArray(int size) {
                return new ClassTimings[size];
            }
        };
    }

    public static class SlotDeatils implements Parcelable {

        @SerializedName("center_id")
        public String center_id;
        @SerializedName("center_name")
        public String center_name;
        @SerializedName("slots_data")
        public SlotsData[] slots_data;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.center_id);
            dest.writeString(this.center_name);
            dest.writeTypedArray(this.slots_data, flags);
        }

        public SlotDeatils() {
        }

        protected SlotDeatils(Parcel in) {
            this.center_id = in.readString();
            this.center_name = in.readString();
            this.slots_data = in.createTypedArray(SlotsData.CREATOR);
        }

        public static final Creator<SlotDeatils> CREATOR = new Creator<SlotDeatils>() {
            @Override
            public SlotDeatils createFromParcel(Parcel source) {
                return new SlotDeatils(source);
            }

            @Override
            public SlotDeatils[] newArray(int size) {
                return new SlotDeatils[size];
            }
        };
    }

    public static class SlotsData implements Parcelable {

        @SerializedName("start_time")
        public String start_time;
        @SerializedName("end_time")
        public String end_time;
        @SerializedName("day")
        public String day;
        /*@SerializedName("day_id")
        public String day_id;*/

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.start_time);
            dest.writeString(this.end_time);
            dest.writeString(this.day);
           // dest.writeString(this.day_id);
        }

        public SlotsData() {
        }

        protected SlotsData(Parcel in) {
            this.start_time = in.readString();
            this.end_time = in.readString();
            this.day = in.readString();
           // this.day_id = in.readString();
        }

        public static final Creator<SlotsData> CREATOR = new Creator<SlotsData>() {
            @Override
            public SlotsData createFromParcel(Parcel source) {
                return new SlotsData(source);
            }

            @Override
            public SlotsData[] newArray(int size) {
                return new SlotsData[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.current_teachers, flags);
        dest.writeTypedArray(this.demands, flags);
        dest.writeString(this.sponsor);
        dest.writeString(this.school_image);
        dest.writeString(this.school_id);
        dest.writeString(this.school_desc);
        dest.writeString(this.school_name);
        dest.writeTypedArray(this.class_detals, flags);
        dest.writeTypedArray(this.school_admin, flags);
        dest.writeTypedArray(this.class_assistant, flags);
        dest.writeInt(this.time_sllots.size());

        for (Map.Entry<String, ClassTimings[]> entry : this.time_sllots.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeTypedArray(entry.getValue(), flags);
        }

    }

    public SchoolDetails() {
    }

    protected SchoolDetails(Parcel in) {
        this.current_teachers = in.createTypedArray(Profile.CREATOR);
        this.demands = in.createTypedArray(CourseData.CREATOR);
        this.sponsor = in.readString();
        this.school_image = in.readString();
        this.school_id = in.readString();
        this.school_desc = in.readString();
        this.school_name = in.readString();
        this.class_detals = in.createTypedArray(ClassDetails.CREATOR);
        this.school_admin = in.createTypedArray(Profile.CREATOR);
        this.class_assistant = in.createTypedArray(Profile.CREATOR);
        int time_sllotsSize = in.readInt();
        this.time_sllots = new HashMap<String, ClassTimings[]>(time_sllotsSize);
        if (time_sllots != null)
            for (int i = 0; i < time_sllotsSize; i++) {
                String key = in.readString();
                ClassTimings[] value = in.createTypedArray(ClassTimings.CREATOR);
                this.time_sllots.put(key, value);
            }
    }

    public static final Creator<SchoolDetails> CREATOR = new Creator<SchoolDetails>() {
        @Override
        public SchoolDetails createFromParcel(Parcel source) {
            return new SchoolDetails(source);
        }

        @Override
        public SchoolDetails[] newArray(int size) {
            return new SchoolDetails[size];
        }
    };
}
