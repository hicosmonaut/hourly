package hi.cosmonaut.hourly.tool.vibration

interface Vibration {
    fun launch(pattern: LongArray, repeatCount: Int)
    fun cancel()
}