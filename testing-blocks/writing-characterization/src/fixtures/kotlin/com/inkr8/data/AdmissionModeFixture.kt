package com.inkr8.data

// Test-only member of the sealed hierarchy, compiled in the isolated main source set.
class AdmissionModeFixture(private val min: Int?, private val max: Int?, private val trace: MutableList<String>) :
    Gamemode(false, "fixture", "fixture", null, null, min, max, null, null) {
    override val minWords: Int? get() { trace.add("min"); return min }
    override val maxWords: Int? get() { trace.add("max"); return max }
}
