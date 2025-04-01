package com.project.pomodoro.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey


//요일 별 테이블
@Entity(tableName = "study_session")
data class StudySession(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var modeType: String,
    var studyTime: Int,
    var studyDate: String
)


//모드 별 테이블
@Entity(tableName = "study_summary")
data class StudySummary(
    @PrimaryKey var id: Int = 1,
    var totalStudyTime: Int = 0,
    var basicMode: Int = 0,
    var longFocus: Int = 0,
    var shortFocus: Int = 0,
    var ultraFocus: Int = 0,
    var coustomMode: Int = 0
)

