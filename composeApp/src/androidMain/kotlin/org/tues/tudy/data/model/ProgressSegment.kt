package org.tues.tudy.data.model

enum class SegmentType { STUDY, REST }

data class ProgressSegment(
    val index: Int,
    val type: SegmentType,
    val round: Int
)
