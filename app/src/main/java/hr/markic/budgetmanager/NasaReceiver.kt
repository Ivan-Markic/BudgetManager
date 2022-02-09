package hr.markic.budgetmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.markic.budgetmanager.framework.startActivity

class NasaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<MainActivity>()
    }
}