package com.project.pomodoro.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudySessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySession)

    @Query("select * from study_session where studyDate = :date")
    suspend fun getStudyByDate(date: String): StudySession

    @Query("select * from study_session")
    suspend fun getAllData(): List<StudySession>

    @Query("delete from study_session")
    suspend fun deleteAllDataBySession()

    @Query(
        "select studyDate, sum(studyTime) as totalStudyTime " +
                "from study_session " +
                "group by studyDate " +
                "order by studyDate"
    )
    suspend fun getDataGroupByDate(): List<StudyDateTotalStudyTime>?

}


@Dao
interface StudySummaryDao {

    @Query("select * from study_summary where id = 1")
    suspend fun getAllData(): StudySummary

    @Query("delete from study_summary")
    suspend fun deleteAllDataBySummary()

    @Query("select totalStudyTime from study_summary where id = 1")
    suspend fun getTotalTime(): Int?


    @Insert
    suspend fun insertStudySummary(studySummary: StudySummary)

    @Update
    suspend fun updateSummary(studySummary: StudySummary)

    suspend fun addStudyTime(time: Int, modeType: String) {
        var currentData = getAllData()

        if (currentData == null) {

            //데이터가 없을경우 각 튜블 데이터 0으로 초기화
            currentData = StudySummary(1, 0, 0, 0, 0, 0, 0)
            insertStudySummary(currentData)
        }

        currentData.let {
            val updateData = when (modeType) {
                "기본 모드" -> it.copy(
                    totalStudyTime = it.totalStudyTime + time,
                    basicMode = it.basicMode + time
                )

                "롱 포커스 모드" -> it.copy(
                    totalStudyTime = it.totalStudyTime + time,
                    longFocus = it.longFocus + time // 수정됨
                )

                "내 맞춤 모드" -> it.copy(
                    totalStudyTime = it.totalStudyTime + time,
                    coustomMode = it.coustomMode + time // 수정됨
                )

                "짧은 집중 모드" -> it.copy(
                    totalStudyTime = it.totalStudyTime + time,
                    shortFocus = it.shortFocus + time // 수정됨
                )

                else -> {
                    it.copy(
                        totalStudyTime = it.totalStudyTime + time,
                        ultraFocus = it.ultraFocus + time // 수정됨
                    )
                }
            }

            updateSummary(updateData)
        }
    }
}

