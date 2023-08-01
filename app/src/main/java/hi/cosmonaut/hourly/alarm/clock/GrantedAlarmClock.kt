package hi.cosmonaut.hourly.alarm.clock

import hi.cosmonaut.hourly.permission.Permission

class GrantedAlarmClock(
    private val permission: Permission,
    private val origin: AlarmClock
) : AlarmClock {

    override fun schedule(millis: Long) {

    }
}