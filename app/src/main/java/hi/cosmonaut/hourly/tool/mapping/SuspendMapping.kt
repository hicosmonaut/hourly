package hi.cosmonaut.hourly.tool.mapping

interface SuspendMapping<in I, out O> {
    suspend fun applyTo(input: I): O
}