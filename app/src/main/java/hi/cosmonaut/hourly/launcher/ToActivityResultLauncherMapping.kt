package hi.cosmonaut.hourly.launcher

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.LifecycleOwner
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToActivityResultLauncherMapping<I, O>(
    private val mapping: Mapping<LifecycleOwner, ActivityResultLauncher<I>>
) : Mapping<LifecycleOwner, ActivityResultLauncher<I>>{

    constructor(
        registry: ActivityResultRegistry,
        name: String,
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): this (
        Mapping { owner ->
            registry.register(
                name,
                owner,
                contract,
                callback
            )
        }
    )

    override fun perform(input: LifecycleOwner?): ActivityResultLauncher<I> = mapping.perform(input)
}