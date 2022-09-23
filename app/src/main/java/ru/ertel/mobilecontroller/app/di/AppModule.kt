package ru.ertel.mobilecontroller.app.di

import android.app.Activity
import android.app.ProgressDialog
import android.os.Handler
import android.widget.Toast
import ru.ertel.mobilecontroller.gear.interfaces.TaskCallback
import ru.ertel.mobilecontroller.util.interfaces.NfcReadUtility
import ru.ertel.mobilecontroller.util.sync.NfcReadUtilityImpl
import org.koin.dsl.module
import timber.log.Timber

val moduleNFC = module {

    factory { (activity: Activity) -> ProgressDialog(activity) }
    single { Handler() }

    single<NfcReadUtility> { NfcReadUtilityImpl() }

    fun provideTaskCallback(mainActivity: Activity, mProgressDialog: ProgressDialog) =
            object : TaskCallback {
                override fun onReturn(result: Boolean?) {
                    Timber.d("Received our result : $result")
                    if (mProgressDialog.isShowing) mProgressDialog.dismiss()
                    result?.run {Toast.makeText(mainActivity, "Write has been done!", Toast.LENGTH_SHORT).show()}
                }

                override fun onProgressUpdate(vararg values: Boolean?) {
                    Timber.i("Trying to write!")
                    if (values.isNotEmpty() && values[0] != null) {
                        mProgressDialog.setMessage("Writing")
                        Timber.i("Writing!!!")
                    }
                }

                override fun onError(e: Exception?) {
                    Timber.e("Encountered an error!!")
                    if (mProgressDialog.isShowing)
                        mProgressDialog.dismiss()
                    Toast.makeText(mainActivity, e?.message?: "no message", Toast.LENGTH_SHORT).show()
                }
            }

    single<TaskCallback> { provideTaskCallback(get(), get()) }

}